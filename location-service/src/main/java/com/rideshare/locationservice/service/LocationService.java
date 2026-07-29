package com.rideshare.locationservice.service;

import com.rideshare.locationservice.dto.DriverLocationRequest;
import com.rideshare.locationservice.dto.NearByDriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private final RedisTemplate<String, String> redisTemplate;

    //Redis key for all driver locations
    private static final String DRIVERS_GEO_KEY = "drivers:locations";

    /**
     * Update driver location in Redis.
     * Called every 3 seconds by driver's phone
     * Maps to Redis GEOADD command
     */

    public void updateDriverLocation(DriverLocationRequest  driverLocationRequest){
        log.info("Updating location for driver: {}", driverLocationRequest.getDriverId());

        // IMPORTANT: longitude FIRST, latitude SECOND - GeoSpatial Standard
        Point driverPoint = new Point(
                driverLocationRequest.getLongitude(),
                driverLocationRequest.getLatitude()
        );

        redisTemplate.opsForGeo().add(
                DRIVERS_GEO_KEY,
                driverPoint,
                driverLocationRequest.getDriverId()
        );

        log.info("Location updated for driver: {}", driverLocationRequest.getDriverId());
    }

    /**
     * Find nearby drivers within given radius.
     * Called by Matching Service on ride request.
     * Maps to Redis GEORADIUS command.
     */

    public List<NearByDriverResponse> findNearbyDrivers(
            double latitude, double longitude, double radiusInKm) {

        log.info("Finding drivers near lat: {} long: {} withing {}Km",
                latitude, longitude, radiusInKm);


        // Creating the search area
        Circle searchArea = new Circle(
                new Point(longitude, latitude), // creating center point
                new Distance(radiusInKm, Metrics.KILOMETERS) // radius = 5 unit kms
        );

    
        // Spring wraps all the objects around Georesults object Georesults has res1, res2, res3, each contains drivers locations
        //
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(  // similar to GEOSEARCH
                        DRIVERS_GEO_KEY, // means search inside drivers:locations (driver1, driver2 ...)
                        searchArea, // within circle
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs() // Think of them as saying Redis,while searching,also do these things.
                                .includeCoordinates() // without this redis return on driver 101, but with it return driver101, lat, long
                                .includeDistance() //  without this redis return on driver 101, but with it return driver101, 2.1km
                                .sortAscending()
                                .limit(10)
                );

        //Redis returns its own internal objects.
        //Your REST API should not expose Redis classes directly.
         //Instead you create your own DTO.

        List<NearByDriverResponse> nearbyDrivers = new ArrayList<>();

        if(results != null){
            results.getContent().forEach(result -> {
                RedisGeoCommands.GeoLocation<String> location = result.getContent(); // u get driver id , location inside reusult
                nearbyDrivers.add(new NearByDriverResponse(
                        location.getName(),
                        location.getPoint().getY(),
                        location.getPoint().getX(),
                        result.getDistance().getValue()
                ));
            });
        }
        log.info("Found {} drivers nearby", nearbyDrivers.size());
        return nearbyDrivers;
    }

    /**
     * Remove driver when they go offline
     * Maps to Redis ZREM command.
     */

    public void removeDriver(String driverId){
        log.info("Removing driver: {}", driverId);
        redisTemplate.opsForGeo().remove(DRIVERS_GEO_KEY, driverId);
    }
}

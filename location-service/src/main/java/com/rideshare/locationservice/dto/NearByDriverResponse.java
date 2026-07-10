package com.rideshare.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearByDriverResponse {
	
	// this is going to send out data to our matching service
	
    private String driverId;
    private double latitude;
    private double longitude;
    private double distanceInKm;
    
}
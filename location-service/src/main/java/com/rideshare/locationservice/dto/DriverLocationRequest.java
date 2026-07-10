package com.rideshare.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationRequest {
	
	//this dto is going to receive the data,  gps coordinate
	
    private String driverId;
    private double latitude;
    private double longitude;
    
}
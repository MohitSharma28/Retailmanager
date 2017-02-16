package com.sample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop {
    @JsonProperty(value="shopName")
    String shopName;
    @JsonProperty(value="shopAddress")
    Address shopAddress;
    @JsonProperty(value="latitude")
    double latitude;
    @JsonProperty(value="longitude")
    double longitude;
    
    
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Address getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(Address shopAddress) {
		this.shopAddress = shopAddress;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
    
    
}

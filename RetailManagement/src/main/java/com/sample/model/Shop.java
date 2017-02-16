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
    @JsonProperty(value="longtitude")
    double longtitude;
    
    
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
	public void setLatitude(double lattitude) {
		this.latitude = lattitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude2) {
		this.longtitude = longtitude2;
	}
    
    
}

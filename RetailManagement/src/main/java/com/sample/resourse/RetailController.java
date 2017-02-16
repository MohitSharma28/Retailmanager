package com.sample.resourse;

import static org.mockito.Matchers.doubleThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sample.model.Shop;

@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class RetailController {

	@RequestMapping(path = { "/shop" }, method = { RequestMethod.GET })
	@ResponseBody
	List<Shop> getShops() {
		return Application.shops;
	}

	@RequestMapping(path = { "/getshop" }, method = { RequestMethod.GET })
	@ResponseBody
	Shop getNearestShop(@RequestParam("lat") double lattitude, @RequestParam("long") double longitude) {
		Shop nearestShop = null;
		double flagDistance=0.0;
		for (Shop shop : Application.shops) {
			double lat = shop.getLatitude();
			double lon = shop.getLongitude();
			final int R = 6371; // Radius of the earth
			Double latDistance = Math.toRadians(lattitude - lat);
			Double lonDistance = Math.toRadians(longitude - lon);
			Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lattitude))
					* Math.cos(Math.toRadians(lat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
			Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double distance = R * c * 1000; // convert to meters
			if(distance == 0){
				return shop;
			}
			if(flagDistance == 0 && nearestShop == null){
				nearestShop = shop;
				flagDistance = distance;
			}
			if(distance < flagDistance){
				nearestShop = shop;
				flagDistance = distance;
			}
		}
		return nearestShop;
	}
	@RequestMapping(path = { "/addshop" }, method = { RequestMethod.POST })
	@ResponseBody
	void addShop(@RequestBody Shop shop) {
		Integer address = shop.getShopAddress().getPostCode();
		HttpURLConnection httpConnection = null;
		int responseCode = 0;
		try {
			String api = "http://maps.googleapis.com/maps/api/geocode/xml?address="
					+ URLEncoder.encode(address.toString(), "UTF-8") + "&sensor=true";
			URL url;
			url = new URL(api);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("Accept", "application/json");
			httpConnection.connect();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String output = "", full = "";
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				full += output;
			}
			// TODO add location values to shop object
			Application.shops.add(shop);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}

	}
}
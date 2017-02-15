package com.sample.resourse;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.model.Shop;

@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class RetailController {

	@RequestMapping(path = { "/shop" }, method = { RequestMethod.GET })
	@ResponseBody
	List<Shop> getShops() {
		List<Shop> shops = null;
		return shops;
	}

	@RequestMapping(path = { "/addshop" }, method = { RequestMethod.POST })
	@ResponseBody
	void addShop(@RequestBody Shop shop) {
		Integer address = shop.getShopAddress().getPostCode();
		int responseCode = 0;
		try {
			String api = "http://maps.googleapis.com/maps/api/geocode/xml?address="
					+ URLEncoder.encode(address.toString(), "UTF-8") + "&sensor=true";
			URL url;
			url = new URL(api);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.connect();
			httpConnection.setRequestProperty("Accept", "application/json");

			if (httpConnection.getResponseCode() != 200) {
			    throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String output = "", full = "";
			while ((output = br.readLine()) != null) {
			    System.out.println(output);
			    full += output;
			}
//			Gson gson = new Gson().fromJson(full, PincodeVerify.class); 
//			response = new IsPincodeSupportedResponse(new PincodeVerifyConcrete(
//			        gson.getResults().get(0).getFormatted_address(), 
//			        gson.getResults().get(0).getGeometry().getLocation().getLat(),
//			        gson.getResults().get(0).getGeometry().getLocation().getLng())) ;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Application.shops.add(shop);
	}
}
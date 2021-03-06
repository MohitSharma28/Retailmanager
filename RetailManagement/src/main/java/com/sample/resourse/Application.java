package com.sample.resourse;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.sample.model.Address;
import com.sample.model.Shop;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static ArrayList<Shop> shops = null;
	public static void main(String args[]) {
		shops = new ArrayList<Shop>();
		Application.addDummyShops();
		SpringApplication.run(Application.class, args);
	}
	
	private static void addDummyShops() {
		for(int i=0; i<=5; i++){
			Shop shop = new Shop();
			Address add = new Address();
			add.setNumber(i);
			add.setPostCode(111057+i);
			shop.setLatitude(11111+i);
			shop.setLongtitude(22222+i);
			shop.setShopAddress(add);
			shop.setShopName("shop"+i);	
			shops.add(shop);
		}	
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Shop shop = restTemplate.getForObject(
					"http://gturnquist-quoters.cfapps.io/api/random", Shop.class);
			log.info(shop.toString());
		};
	}
}
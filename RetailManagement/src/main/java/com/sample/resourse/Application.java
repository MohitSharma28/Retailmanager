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
		Shop shop = new Shop();
		Address add = new Address();
		add.setNumber(1);
		add.setPostCode(111057);
		shop.setLatitude(11111);
		shop.setLongitude(22222);
		shop.setShopAddress(add);
		shop.setShopName("shop1");
		SpringApplication.run(Application.class, args);
		shops.add(shop);
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
	
//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer() {
//        return (container -> {
//            container.setPort(8012);
//        });
//    }
}
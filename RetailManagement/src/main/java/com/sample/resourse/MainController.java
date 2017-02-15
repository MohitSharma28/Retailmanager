package com.sample.resourse;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sample.model.Shop;

@Controller
@EnableAutoConfiguration
public class MainController {
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Welcome to Retail Management!";
    }
    
    @RequestMapping(path={"/shop"}, method={RequestMethod.GET})
    @ResponseBody
    List<Shop> getShops() {
    	List <Shop> shops = null;
        return shops;
    }

    @RequestMapping(path={"/shop"}, method={RequestMethod.POST})
    @ResponseBody
    void addShop(@RequestBody Shop shop) {
    	List <Shop> shops = null;
        shops.add(shop);
    }
}
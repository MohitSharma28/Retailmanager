package com.sample.resourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
	Shop getNearestShop(@RequestParam("lat") double lattitude, @RequestParam("long") double longtitude) {
		Shop nearestShop = null;
		double flagDistance=0.0;
		for (Shop shop : Application.shops) {
			double lat = shop.getLatitude();
			double lon = shop.getLongtitude();
			final int R = 6371; // Radius of the earth
			Double latDistance = Math.toRadians(lattitude - lat);
			Double lonDistance = Math.toRadians(longtitude - lon);
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
			BufferedReader reader = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
//			String output = "", full = "";
//			while ((output = reader.readLine()) != null) {
//				System.out.println(output);
//				full += output;
//			}
            int data = reader.read();
            char[] buffer = new char[1024];
            Writer writer = new StringWriter();
            while ((data = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, data);
            }

            String result = writer.toString();
            System.out.println(result.toString());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader("<"+writer.toString().trim()));
            Document doc = db.parse(is);
			
            String strLatitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lat/text()");
            System.out.println("Latitude:" + strLatitude);

            String strLongtitude = getXpathValue(doc,"//GeocodeResponse/result/geometry/location/lng/text()");
            System.out.println("longtitude:" + strLongtitude);
            if(strLatitude != null || strLongtitude != null){
                double lattitude = Double.parseDouble(strLatitude);
                double longtitude = Double.parseDouble(strLongtitude);
                shop.setLongtitude(longtitude);
                shop.setLatitude(lattitude);            	
            }
			Application.shops.add(shop);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}

	}
    private String getXpathValue(Document doc, String strXpath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = null;
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
        }
        return resultData;


}
}
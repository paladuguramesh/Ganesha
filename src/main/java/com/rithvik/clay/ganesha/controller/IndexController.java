package com.rithvik.clay.ganesha.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.zxing.WriterException;
import com.rithvik.clay.ganesha.entity.Location;
import com.rithvik.clay.ganesha.entity.OrderItem;
import com.rithvik.clay.ganesha.model.OrderDetails;
import com.rithvik.clay.ganesha.repository.LocationRepository;
import com.rithvik.clay.ganesha.repository.OrderItemRepository;
import com.rithvik.clay.ganesha.util.QRCodeGenerator;
import com.rithvik.clay.ganesha.util.RandomStringGenerator;
import com.rithvik.clay.ganesha.util.RandomStringGenerator.Mode;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
	
	@Autowired
	private OrderItemRepository orderRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
    private Environment environment;

	@GetMapping("/")
	public String index(ModelMap model) {
		
		model.addAttribute("order", new OrderItem());
		model.addAttribute("orderDetails", new OrderDetails());
		model.addAttribute("showOrderDetails", false);
		model.addAttribute("message", "");
		
		List<Location> locationList = (List<Location>) locationRepository.findAll();
		model.addAttribute("locations", locationList);
		
		model.addAttribute("costPerUnit", environment.getProperty("costPerUnit"));
		
		model.addAttribute("trackOrder", new OrderItem());
		
		return "index";
	}

	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public String createOrder(@ModelAttribute("order") OrderItem order, ModelMap model, HttpServletRequest request) {
		String orderId = "";
		try {
			
			RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
			orderId = randomStringGenerator.generateRandomString(8, Mode.NUMERIC);
			orderId = "CG" + orderId;

			order.setOrderId(orderId);
			order.setOrderDate(new Date());
			order.setOrderStatus(0);
			order.setPaymentStatus(0);
			order.setQuantity(Integer.parseInt(order.getOrderQuantity()));
			order.setPickupLocation(Long.parseLong(order.getOrderPickupLocation()));
			order.setAmount(new BigDecimal(order.getOrderAmount()));
			order.setPaymentMode("COD");
			
			orderRepository.save(order);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/checkout?orderId="+orderId;
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(@RequestParam("orderId") String orderId, ModelMap model) {
		if(orderId != null && orderId.length()>0) {
			OrderItem order = orderRepository.findOrderItemById(orderId);
			if(order != null) {
				
				model.addAttribute("publicKey", environment.getProperty("stripe.api.publicKey"));
		        model.addAttribute("amount", order.getAmount());
		        model.addAttribute("email", order.getEmail());
		        model.addAttribute("productName", "Purchase of Ganesh Moorthis");
		        model.addAttribute("orderId", orderId);
	
			}
		}
		return "checkout";
	}
	
	@RequestMapping(value = "/payLater", method = RequestMethod.GET)
	public String payLater(@RequestParam("orderId") String orderId, ModelMap model, HttpServletRequest request) {
		if(orderId != null && orderId.length()>0) {
			OrderItem order = orderRepository.findOrderItemById(orderId);
			if(order != null) {
				
		        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
			            .replacePath(null)
			            .build()
			            .toUriString();
		 
					 String orderDetailsUrl = "";
		 
					 if(baseUrl.endsWith("/")) {
						 orderDetailsUrl = baseUrl + "orderDetails?orderId=" + orderId;
					 } else {
						 orderDetailsUrl = baseUrl + "/orderDetails?orderId=" + orderId;
					 }
		 			
					//Generate QR code with order details
					byte[] image = new byte[0];
				    try {
				        // Generate and Return Qr Code in Byte Array
				        image = QRCodeGenerator.getQRCodeImage(orderDetailsUrl,250,250);
				    } catch (WriterException | IOException e) {
				        e.printStackTrace();
				    }
	        
				    // Convert Byte Array into Base64 Encode String
				    String qrcode = "data:image/png;base64," + Base64.getEncoder().encodeToString(image);
				    
				    Location location = locationRepository.findLocationById(order.getPickupLocation());
				    
				    String pickupLocation = "";
				    String contactDetails = "";
				    String timings = "";
				    
				    if(location != null) {
				    	pickupLocation = location.getName() + ", " + location.getAddress();
				    	contactDetails = location.getContactPersonName() + "(" + location.getContactPersonNo() + ")";
				    	timings = location.getContactPersonTimings();
				    }
	    
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setOrderDate(order.getOrderDate());
					orderDetails.setId(orderId);
					orderDetails.setQuantity(order.getQuantity());
					orderDetails.setAmount(order.getAmount());
					orderDetails.setPickupLocation(pickupLocation);
					orderDetails.setContactDetails(contactDetails);
					orderDetails.setTimings(timings);
					orderDetails.setQrcode(qrcode);
					
					model.addAttribute("order", new OrderItem());
					model.addAttribute("orderDetails", orderDetails);
					model.addAttribute("showOrderDetails", true);
					model.addAttribute("message", "Order created successfully");
		        
			}
		}
		return "codOrder";
	}
	
	@GetMapping("/error")
	public String error(ModelMap model) {
		
		model.addAttribute("Something went wrong.", "");
		
		return "error";
	}
}

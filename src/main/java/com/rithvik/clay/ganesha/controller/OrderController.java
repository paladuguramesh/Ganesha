package com.rithvik.clay.ganesha.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rithvik.clay.ganesha.entity.Location;
import com.rithvik.clay.ganesha.entity.OrderItem;
import com.rithvik.clay.ganesha.model.ApiResponse;
import com.rithvik.clay.ganesha.model.OrderDetails;
import com.rithvik.clay.ganesha.repository.LocationRepository;
import com.rithvik.clay.ganesha.repository.OrderItemRepository;

@Controller
public class OrderController {
	
	@Autowired
	private OrderItemRepository orderRepository;
	
	@Autowired
	private LocationRepository locationRepository;

	@RequestMapping(value = "/orderDetails", method = RequestMethod.GET)
	public String orderDetails(@RequestParam("orderId") String orderId, ModelMap model) {
		if(orderId != null && orderId.length()>0) {
			try {
				OrderItem order = orderRepository.findOrderItemById(orderId);
				if(order != null) {
					
					String pickupLocation = "";
				    String contactDetails = "";
				    String timings = "";
				    
				    Location location = locationRepository.findLocationById(order.getPickupLocation());
				    if(location != null) {
				    	pickupLocation = location.getName() + ", " + location.getAddress();
				    	contactDetails = location.getContactPersonName() + "(" + location.getContactPersonNo() + ")";
				    	timings = location.getContactPersonTimings();
				    }
					
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setId(orderId);
					orderDetails.setQuantity(order.getQuantity());
					orderDetails.setAmount(order.getAmount());
					orderDetails.setPickupLocation(pickupLocation);
					orderDetails.setContactDetails(contactDetails);
					orderDetails.setTimings(timings);
					orderDetails.setQrcode("");
					orderDetails.setOrderDate(order.getOrderDate());
					orderDetails.setOrderStatus(order.getOrderStatus());
					orderDetails.setPaymentStatus(order.getPaymentStatus());
					orderDetails.setPaymentDate(order.getPaymentDate());
					orderDetails.setPaymentMode(order.getPaymentMode());
					orderDetails.setDeliveredDate(order.getDeliveredDate());
					orderDetails.setTransactionId(order.getTransactionId());
					
					model.addAttribute("orderDetails", orderDetails);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "orderItem";
	}
	
	@RequestMapping(value = "/processCashPaymentOrder", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponse processCashPaymentOrder(@RequestParam("orderId") String orderId, ModelMap model) {
		ApiResponse apiResponse = new ApiResponse();
		if(orderId != null && orderId.length()>0) {
			try {
				OrderItem order = orderRepository.findOrderItemById(orderId);
				if(order != null) {
					
					order.setPaymentStatus(1);
					order.setPaymentDate(new Date());
					
					order.setOrderStatus(1);
					order.setDeliveredDate(new Date());
					
					orderRepository.save(order);
					
					apiResponse.setStatus(true);
					apiResponse.setMessage("Order " + orderId + " delivered successfully");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return apiResponse;
	}
	
	
	@RequestMapping(value = "/processOnlinePaymentOrder", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponse processOnlinePaymentOrder(@RequestParam("orderId") String orderId, ModelMap model) {
		ApiResponse apiResponse = new ApiResponse();
		if(orderId != null && orderId.length()>0) {
			try {
				OrderItem order = orderRepository.findOrderItemById(orderId);
				if(order != null) {
					
					order.setPaymentStatus(1);
					order.setPaymentDate(new Date());
					
					order.setOrderStatus(1);
					order.setDeliveredDate(new Date());
					
					orderRepository.save(order);
					
					apiResponse.setStatus(true);
					apiResponse.setMessage("Order " + orderId + " delivered successfully");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return apiResponse;
	}
	
	@RequestMapping(value = "/confirmPayment", method = RequestMethod.GET)
	public String confirmPayment(@RequestParam("paymentId") String paymentId, @RequestParam("orderId") String orderId, ModelMap model) {
		if(orderId != null && orderId.length()>0) {
			OrderItem order = orderRepository.findOrderItemById(orderId);
			if(order != null) {
				order.setPaymentStatus(1);
				order.setPaymentDate(new Date());
				order.setPaymentMode("online");
				order.setTransactionId(paymentId);
				
				orderRepository.save(order);
			}
		}
		return "redirect:/orderDetails?orderId=" + orderId;
	}
	
	@RequestMapping(value = "/trackOrder", method = RequestMethod.POST)
	public String trackOrder(@ModelAttribute("trackOrder") OrderItem trackOrder, ModelMap model) {
		if(trackOrder != null) {
			OrderItem order = orderRepository.findOrderItemById(trackOrder.getOrderId());
			if(order != null) {
				return "redirect:/orderDetails?orderId=" + order.getOrderId();
			} else {
				model.addAttribute("message", "Invalid order Id");
			}
		}
		return "/error";
	}
}

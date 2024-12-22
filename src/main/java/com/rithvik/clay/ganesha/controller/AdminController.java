package com.rithvik.clay.ganesha.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rithvik.clay.ganesha.entity.Location;
import com.rithvik.clay.ganesha.entity.OrderItem;
import com.rithvik.clay.ganesha.repository.LocationRepository;
import com.rithvik.clay.ganesha.repository.OrderItemRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@GetMapping("/dashboard")
	public String dashboard(ModelMap model) {
		
		if (IsValidSession()) {
			
			List<OrderItem> totalOrdersList = (List<OrderItem>) orderItemRepository.findAll();
			List<OrderItem> deliveredOrdersList = (List<OrderItem>) orderItemRepository.findDeliveredOrders();
			List<OrderItem> pendingOrdersList = (List<OrderItem>) orderItemRepository.findPendingdOrders();
			
			BigDecimal totalRevenue = new BigDecimal(0);
			for(OrderItem orderItem : totalOrdersList) {
				totalRevenue = totalRevenue.add(orderItem.getAmount());
			}
			
			model.addAttribute("totalSales", totalOrdersList.size());
			model.addAttribute("deliveredOrders", deliveredOrdersList.size());
			model.addAttribute("pendingOrders", pendingOrdersList.size());
			model.addAttribute("totalRevenue", totalRevenue);
			
			return "dashboard";
			
		} else {
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/locations")
	public String locations(ModelMap model) {
		if (IsValidSession()) {
			
			List<Location> locationsList = (List<Location>) locationRepository.findAll();
			model.addAttribute("locationsList", locationsList);
			
			return "locations";
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/orders")
	public String orders(ModelMap model) {
		if (IsValidSession()) {
			
			List<OrderItem> ordersList = (List<OrderItem>) orderItemRepository.findAll();
			model.addAttribute("ordersList", ordersList);
			
			return "orders";
		} else {
			return "redirect:/login";
		}
		
	}
	
	private boolean IsValidSession() {
		
		System.out.println("@@@ checking session...");
		
		HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		if (session != null) {
			System.out.println("@@@ a session exists..." + session.getAttribute("userName"));
			
			return true;
		} else {
			System.out.println("@@@ no session...");
			
			return false;
		}
	}
	
}

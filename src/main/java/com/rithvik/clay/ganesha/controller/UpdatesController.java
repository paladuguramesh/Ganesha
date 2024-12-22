package com.rithvik.clay.ganesha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rithvik.clay.ganesha.entity.Location;
import com.rithvik.clay.ganesha.repository.LocationRepository;

@RestController
public class UpdatesController {
	
	@Autowired
	private LocationRepository locationRepository;

	@GetMapping("/updateLocations")
	@ResponseBody
	public String updateLocations(ModelMap model) {
		
		Location location = new Location();
		location.setName("Redmond Driving License Parking Lot");
		location.setAddress("7225 170th Ave NE, Redmond, WA 98052");
		location.setContactPersonName("Rithvik Palepu");
		location.setContactPersonNo("+1 (630) 999-5845");
		location.setContactPersonTimings("8 AM - 10 AM");
		locationRepository.save(location);
		
		
		location = new Location();
		location.setName("Redmond Town Center");
		location.setAddress("C2 Remdond Parking Lot");
		location.setContactPersonName("Rithvik Palepu");
		location.setContactPersonNo("+1 (630) 999-5845");
		location.setContactPersonTimings("4 PM - 6 PM");
		locationRepository.save(location);
		
		
		location = new Location();
		location.setName("Hindu Temple & Cultural Center");
		location.setAddress("3818 212th St SE, Bothell, WA 98021");
		location.setContactPersonName("Rithvik Palepu");
		location.setContactPersonNo("+1 (630) 999-5845");
		location.setContactPersonTimings("8 PM - 9 PM");
		locationRepository.save(location);
		
		return "Added locations successfully!";
	}
}

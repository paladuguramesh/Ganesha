package com.rithvik.clay.ganesha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rithvik.clay.ganesha.entity.User;
import com.rithvik.clay.ganesha.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/login")
	public String login(ModelMap model) {
		model.addAttribute("userDetails", new User());
		return "login";
	}
	
	@PostMapping("/validateLogin")
	public String validateLogin(@ModelAttribute("userDetails") User userDetails, ModelMap model, HttpServletRequest servletRequest) {
		boolean IsUserAuthenticated = false;
		try {
			if(userDetails != null) {
				User user = userRepository.findUserByUserNameAndPassword(userDetails.getUserName(), userDetails.getPassword());
				if(user != null) {
					
					HttpSession httpSession = servletRequest.getSession();
					httpSession.setAttribute("userName", user.getName());
					
					IsUserAuthenticated = true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(IsUserAuthenticated) {
			model.addAttribute("message", "Login success");
			return "redirect:/dashboard";
		} else {
			model.addAttribute("message", "Login failed, please provide valid username & password.");
			return "login";
		}
	}
	
	
	@GetMapping("/logout")
	public String logout(ModelMap model) {
		
		System.out.println("@@@ removing session...");
		
        HttpSession httpSession = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		if(httpSession != null) {
			httpSession.removeAttribute("userName");
			httpSession.invalidate();
		}
		return "redirect:/login";
	}
}

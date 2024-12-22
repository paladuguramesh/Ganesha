package com.rithvik.clay.ganesha.stripe;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String intentID;
    private String clientSecret;
	
	public String getIntentID() {
		return intentID;
	}
	public void setIntentID(String intentID) {
		this.intentID = intentID;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
    
    
}

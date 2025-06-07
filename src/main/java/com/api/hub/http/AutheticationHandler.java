package com.api.hub.http;

import java.util.Map;

public interface AutheticationHandler {

	void init(String name);
	Map<String,String> getHeaders();
}

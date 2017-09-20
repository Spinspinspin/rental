package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;


import spark.Request;
import spark.Response;
import spark.Route;

public class HomeController {


	
	public static final Route indexjin = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		Jinjava jinjava = new Jinjava();
		List<Apartment> apartments = Apartment.findAll();
		Map<String, Object> context = Maps.newHashMap();
		context.put("apartments", apartments);
		context.put("currentUser", req.session().attribute("currentUser"));		
		String template = Resources.toString(Resources.getResource("templates/home/indexjin.html"), Charsets.UTF_8);
		String renderedTemplate = jinjava.render(template, context);
		return renderedTemplate;
		}
	};
	
}

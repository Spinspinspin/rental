package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id);
			User currentUser = req.session().attribute("currentUser");
			List<User> liker = apartment.getAll(User.class);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("likes", req.session().attribute("likes"));
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("apartment", apartment);
			model.put("liker", liker);
			if (currentUser != null) {
				model.put("owner", (currentUser.getId().toString()).equals(apartment.get("user_id").toString()));
			}
			return MustacheRenderer.getInstance().render("apartment/details.html", model);
		}
	};

	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("apartment/newForm.html", model);

	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment(Integer.parseInt(req.queryParams("rent")),
					Integer.parseInt(req.queryParams("number_of_bedrooms")),
					Double.parseDouble(req.queryParams("number_of_bathrooms")),
					Integer.parseInt(req.queryParams("square_footage")), req.queryParams("address"),
					req.queryParams("city"), req.queryParams("state"), req.queryParams("zip_code"), true);

			User user = req.session().attribute("currentUser");
			user.add(apartment);
			apartment.saveIt();
			res.redirect("/apartments/mine");
			return "";
		}
	};
	public static final Route index = (Request req, Response res) -> {
		User currentUser = req.session().attribute("currentUser");
		
		long id = (long) currentUser.getId();

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> activeApartments = Apartment.where("user_id = ? and is_active = ?", id, true);
			List<Apartment> inactiveApartments = Apartment.where("user_id = ? and is_active = ?", id, false);

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("isActive", activeApartments);
			model.put("notActive", inactiveApartments);
			model.put("currentUser", req.session().attribute("currentUser"));

			return MustacheRenderer.getInstance().render("apartment/index.html", model);
		}

	};

	public static Route activate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", true);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};
	public static Route deactivate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", false);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};

	public static Route like = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			User currentUser = req.session().attribute("currentUser");
			apartment.add(currentUser);
			res.redirect("/apartments/" + id);
			return "";
		}
	};
}

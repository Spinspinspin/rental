package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			new User("1@1.com", encryptedPassword, "Dan", "Spin").saveIt();
			Apartment.deleteAll();
			new Apartment(6000, 1, 0, 350, "123 Main St.", "San Francisco", "CA", "95125").saveIt();
			new Apartment(1459, 5, 6, 4000, "123 Coyboy Way.", "Houston", "TX", "77006").saveIt();

		}
		path("/apartments", () -> {
			before("/new", 		SecurityFilters.isAuthenticated);
			get("/new", 		ApartmentController.newForm);
			
			get("/:id",		 	ApartmentController.details);
			
			before("", 			SecurityFilters.isAuthenticated);
			post("", 			ApartmentController.create);
		});

		get("/", 			HomeController.indexjin);
		get("/login", 		SessionController.newForm);
		post("/login", 		SessionController.create);
		get("/logout", 		SessionController.destroy);
		get("/signUp", 		UserController.newForm);
		post("/signUp", 	UserController.create);

		path("/api", () -> {
			get("/apartments/:id", 		ApartmentApiController.details);
			post("/apartments", 		ApartmentApiController.create);
		});
	}
}

package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;




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



public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User dan = new User("1@1.com", encryptedPassword, "Dan", "Spin");
			dan.saveIt();
			User notDan = new User("2@2.com", encryptedPassword, "Dan", "Spin");
			notDan.saveIt();
			
			
			Apartment.deleteAll();
			Apartment apartment = new Apartment(6000, 1, 0, 350, "123 Main St.", "San Francisco", "CA", "95125", true);
			dan.add(apartment);
			apartment.saveIt();
			
			apartment = new Apartment(1459, 5, 6, 4000, "123 Coyboy Way.", "Houston", "TX", "77006", false);
			dan.add(apartment);
			apartment.saveIt();

		}
		path("/apartments", () -> {
			before("/new", 					SecurityFilters.isAuthenticated);
			get("/new", 					ApartmentController.newForm);
			
			before("/mine", 				SecurityFilters.isAuthenticated);
			get("/mine", 					ApartmentController.index);
			
			get("/:id",		 				ApartmentController.details);
			post("/:id/activations", 		ApartmentController.activate);
			post("/:id/deactivations", 		ApartmentController.deactivate);
			post("/:id/like",  				ApartmentController.like);
			
			before("", 						SecurityFilters.isAuthenticated);
			post("", 						ApartmentController.create);
		});

		get("/", 			HomeController.index);
		get("/login", 		SessionController.newForm);
		post("/login", 		SessionController.create);
		
		post("/logout", 	SessionController.destroy);
		
		get("/users/new", 	UserController.newForm);
		post("/users/new", 	UserController.create);

		path("/api", () -> {
			get("/apartments/:id", 		ApartmentApiController.details);
			post("/apartments", 		ApartmentApiController.create);
		});
	}
}

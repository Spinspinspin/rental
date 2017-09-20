package com.libertymutual.goforcode.spark.app.controllers;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserApiController {

	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("/signUp", null);

	};

	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = User.findFirst("email= ?", email);
			if (user != null && BCrypt.checkpw(password, user.getPassword())) {
				req.session().attribute("currentUser", user);
			}
		}
		res.redirect("/");
		return "";
	};
}

package com.libertymutual.goforcode.spark.app.models;



import org.javalite.activejdbc.Model;

public class Apartment extends Model {

		
		
		public Apartment() {}
		
		public Apartment(int rent, int numberOfBedrooms, double numberOfBathrooms, double squareFootage, String address, String city,
				String state, String zip_code) {
			
			
			setRent(rent);
			setNumberOfBedrooms(numberOfBedrooms);
			setNumberOfBathrooms(numberOfBathrooms);
			setSquareFootage(squareFootage);
			setAddress(address);
			setCity(city);
			setState(state);
			setZipCode(zip_code);
		}
	
		public double getSquareFootage() {
			return getDouble("square_footage");
		}
		public void setSquareFootage(double squareFootage) {
			set ("square_footage", squareFootage);
		}
		
		public String getCity() {
			return getString ("city");
		}
		public void setCity(String city) {
			set ("city", city);
		}
		public String getState() {
			return getString ("state");
		}
		public void setState(String state) {
			set ("state", state);
		}
		public String getZipCode() {
			return getString ("zip_code");
		}
		public void setZipCode(String zip_code) {
			set ("zip_code", zip_code);
		}

		public int getNumberOfBr() {
			return getInteger("number_of_bedrooms");
		}

		

		public double getNumberOfWc() {
			return getInteger("number_of_bathrooms");
		}

		

		public String getAddress() {
			return getString("address");
		}

		public void setAddress(String address) {
			set("address", address);
		}
		

		public int getRent() {
			return getInteger("rent");
		}

		public void setRent(int rent) {
			set("rent", rent);
		}

		public void setNumberOfBedrooms(int numberOfBedrooms) {
			set("number_of_bedrooms", numberOfBedrooms);
		}

		public void setNumberOfBathrooms(double numberOfBathrooms) {
			set("number_of_bathrooms", numberOfBathrooms);
		}
		
		
		
	
}








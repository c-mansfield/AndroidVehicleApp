package com.MMU.vehicleapp;

import java.io.Serializable;

public class Vehicle implements Serializable {
	private int vehicle_id;
	private String make;
	private String model;
	private int year;
	private int price;
	private String license_number;
	private String colour;
	private int number_doors;
	private String transmission;
	private int mileage;
	private String fuel_type;
	private int engine_size;
	private String body_style;
	private String condition;
	private String notes;
	
	/*Vehicle Object with all attributes
	 */
	public Vehicle(int vehicle_id, String make, String model, int year, int price,
				   String license_number, String colour, int number_doors, String transmission,
				   int mileage, String fuel_type, int engine_size, String body_style,
				   String condition, String notes)
	{
		this.set_vehicle_ID(vehicle_id);
		this.set_make(make);
		this.set_model(model);
		this.set_year(year);
		this.set_price(price);
		this.set_license_number(license_number);
		this.set_colour(colour);
		this.set_number_doors(number_doors);
		this.set_transmission(transmission);
		this.set_mileage(mileage);
		this.set_fuel_type(fuel_type);
		this.set_engine_size(engine_size);
		this.set_body_style(body_style);
		this.set_condition(condition);
		this.set_notes(notes);
	}
	
	public int get_vehicle_ID()
	{
		return vehicle_id;
	}
	
	public void set_vehicle_ID(int vehicle_id)
	{
		this.vehicle_id = vehicle_id;
	}
	
	public String get_make()
	{
		return make;
	}
	
	public void set_make(String make)
	{
		this.make = make;
	}
	
	public String get_model()
	{
		return model;
	}
	
	public void set_model(String model)
	{
		this.model = model;
	}
	
	public int get_year()
	{
		return year;
	}
	
	public void set_year(int year)
	{
		this.year = year;
	}
	
	public int get_price()
	{
		return price;
	}
	
	public void set_price(int price)
	{
		this.price = price;
	}
	
	public String get_license_number()
	{
		return license_number;
	}
	
	public void set_license_number(String license_number)
	{
		this.license_number = license_number;
	}

	public String get_colour()
	{
		return colour;
	}
	
	public void set_colour(String colour)
	{
		this.colour = colour;
	}

	public int get_number_doors()
	{
		return number_doors;
	}
	
	public void set_number_doors(int number_doors)
	{
		this.number_doors = number_doors;
	}

	public String get_transmission()
	{
		return transmission;
	}
	
	public void set_transmission(String transmission)
	{
		this.transmission = transmission;
	}

	public int get_mileage()
	{
		return mileage;
	}
	
	public void set_mileage(int mileage)
	{
		this.mileage = mileage;
	}

	public String get_fuel_type()
	{
		return fuel_type;
	}
	
	public void set_fuel_type(String fuel_type)
	{
		this.fuel_type = fuel_type;
	}

	public int get_engine_size()
	{
		return engine_size;
	}
	
	public void set_engine_size(int engine_size)
	{
		this.engine_size = engine_size;
	}

	public String get_body_style()
	{
		return body_style;
	}
	
	public void set_body_style(String body_style)
	{
		this.body_style = body_style;
	}

	public String get_condition()
	{
		return condition;
	}
	
	public void set_condition(String condition)
	{
		this.condition = condition;
	}

	public String get_notes()
	{
		return notes;
	}
	
	public void set_notes(String notes)
	{
		this.notes = notes;
	}

}

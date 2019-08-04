package servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.Vehicle;
import models.VehicleDAO;

public class ServletApi extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	VehicleDAO dao = new VehicleDAO();
	Gson gson = new Gson();
	PrintWriter writer;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ArrayList<Vehicle> allVehicles = null;
		//Get all vehicles from the SQL database
		try {
			allVehicles = dao.getAllVehicles();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		resp.setContentType("application/json");
		writer = resp.getWriter();
		//Converts array list of vehicles into JSON using GSON and sends with request
		String conJSON = gson.toJson(allVehicles);
		writer.write(conJSON);
		writer.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HashMap<String,String> post = new HashMap<String,String>();
		//read the request body
		BufferedReader in = new BufferedReader(req.getReader());
		String line = "";
		String request = "";
		while((line = in.readLine()) != null) {
			request = request + line;
		}
		System.out.println(request);
		
		//individual key=value pairs are delimited by and signs
		String[] pairs = request.split("&");					
		for(int i=0;i<pairs.length;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     i++) {
			//each key=value pair is separated by an equals, and both halves require URL decoding.
			String pair = pairs[i];
			post.put(URLDecoder.decode(pair.split("=")[0],"UTF-8"),URLDecoder.decode(pair.split("=")[1],"UTF-8"));

		}
		
		//Gets vehicle from JSON format from key/value pair
		Vehicle v = gson.fromJson(post.get("json"), Vehicle.class);
		
		//Sets the vehicle id for the db
		try {
			v.set_vehicle_ID(dao.getNextVehicle_ID() + 1);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Inserts vehicle into sql db
		try {
			boolean done = dao.insertVehicle(v);
			System.out.println(done);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		//Gets id passed through and then deletes vehicle in db according to that id
		int id = Integer.parseInt(req.getParameter("id"));
        try {
			dao.deleteVehicle(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		HashMap<String,String> post = new HashMap<String,String>();
		//read the request body
		BufferedReader in = new BufferedReader(req.getReader());
		String line = "";
		String request = "";
		while((line = in.readLine()) != null) {
			request = request + line;
		}
		System.out.println(request);
		
		//individual key=value pairs are delimited by and signs
		String[] pairs = request.split("&");					
		for(int i=0;i<pairs.length;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     i++) {
			//each key=value pair is separated by an equals, and both halves require URL decoding.
			String pair = pairs[i];
			post.put(URLDecoder.decode(pair.split("=")[0],"UTF-8"),URLDecoder.decode(pair.split("=")[1],"UTF-8"));

		}
		
		//Gets vehicle from JSON format from key/value pair
		Vehicle v = gson.fromJson(post.get("json"), Vehicle.class);
		
		//Updates vehicle in db
		try {
			boolean done = dao.updateVehicle(v, v.get_vehicle_ID());
			System.out.println(done);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

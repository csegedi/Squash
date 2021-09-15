package Project.Squash.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import Project.Squash.db.Database;
import Project.Squash.model.Contest;
import Project.Squash.model.Location;
import Project.Squash.model.Player;
import Project.Squash.model.Player_Status;

@Controller
public class AppController_Admin {

	@GetMapping("/")
	public String mainPage(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}

		}

		return "index.html";

	}

	@PostMapping("/admin")
	public String adminPage(Model model,
			@RequestParam(required = false, name = "username") String adminName,
			@RequestParam(required = false, name = "password") String password,
			@CookieValue(required = false, value = "cookie_status") String incomingStatus,
			HttpServletResponse response) {

		Database db = new Database();
		String returnPage = "";
		Player admin=null; 

		/** CHECK THE ADMIN'S ENTRY */

		List<Player> list_of_admins = db.getPlayersByEntry(adminName, password);
		
		for (int listIndex=0; listIndex<list_of_admins.size(); listIndex++) {
			if (list_of_admins.get(listIndex).getStatus()==Player_Status.ADMIN) {
				admin=list_of_admins.get(listIndex); 
				break; 
			}
		}
		

		if (admin==null && (incomingStatus == null)) { // first entry: wrong username or password
			returnPage = "failed_login.html";

		}

		else if (admin==null && (incomingStatus != null)) { // backward from another pages

			returnPage = "admin.html";
		}

		else if (admin!=null) {

			String status = Player_Status.ADMIN.toString(); // first entry: correct username and password
			Cookie cookie = new Cookie("cookie_status", status);
			response.addCookie(cookie);

			returnPage = "admin.html";

		}

		db.close();

		return returnPage;

	}

	@PostMapping("admin/addLocation")
	public String addLocation(Model model,
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "address") String address,
			@RequestParam(required = false, name = "rent") Integer rent,
			@CookieValue(value = "cookie_status") String status) {

		Database db = new Database();
		String returnPage = null;

		if (name.isEmpty() == false && address.isEmpty() == false && rent != null) {

			db.insertLocation(name, address, rent);

			Location location = new Location(name, address, rent);

			RestTemplate restTemplate = new RestTemplate();
			String currency = restTemplate.getForObject("http://localhost:8080/rest", String.class);
			currency.replace(',', '.');
			double changed = Double.parseDouble(currency);
			double rent_in_EUR = (rent / changed);

			model.addAttribute("location", location);
			model.addAttribute("EUR", rent_in_EUR);
			returnPage = "location_confirm.html";

		}

		else {

			returnPage = "wrongInput.html";
		}

		db.close();

		return returnPage;
	}

	@PostMapping("admin/addPlayer")
	public String addPlayer(Model model,
			@RequestParam(name = "playerName") String playerName,
			@CookieValue(value = "cookie_status") String incomingStatus) {

		Database db = new Database();
		String returnPage = null;

		/** SET THE FIRST PROPERTIES OF THE PLAYER BY THE ADMIN */

		if (playerName.isEmpty() == false) {

			int randomNumber = new Random().nextInt(100) + 1; // generating password (the player should this change
																// later)
			String generatedPassword = (playerName + randomNumber);
			Player_Status status = Player_Status.PLAYER;
			boolean initValue = false; // this shows the password flag

			db.insertPlayer(playerName, generatedPassword, status.toString(), initValue);

			Player player = new Player(playerName, generatedPassword, status, initValue);

			model.addAttribute("player", player);
			returnPage = "player_confirm.html";

		}

		else {

			returnPage = "wrongInput.html";

		}

		db.close();

		return returnPage;
	}

	@PostMapping("admin/addContest")
	public String addContest(Model model,
			@RequestParam(required = false, name = "player1_id") Integer player_1_id,
			@RequestParam(required = false, name = "player2_id") Integer player_2_id,
			@RequestParam(required = false, name = "location_id") Integer location_id,
			@RequestParam(required = false, name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam(required = false, name = "result") String result,
			@RequestParam(required = false, name = "winner") Integer winner_id,
			@CookieValue(value = "cookie_status") String incomingStatus) {

		Database db = new Database();
		String returnPage = null;
		Player player_1 = null;
		Player player_2 = null;
		Player winner = null;

		if ((player_1_id != null) && (player_2_id != null) && (location_id != null) && (result.isEmpty() == false)
				&& (winner_id != null)) { // check 1: the admin should fill every field

			player_1 = db.getPlayersById(player_1_id);
			player_2 = db.getPlayersById(player_2_id);

			if ((winner_id == player_1_id) || (winner_id == player_2_id)) { // check the winner: winner should be a
																			// participant

				winner = db.getPlayersById(winner_id);
			}

			else {

				returnPage = "wrongInput.html";
			}

			Location location = db.getLocationsById(location_id);

			if ((player_1 != null) && (player_2 != null) && (winner != null) && (location != null)) { // check 2: for example the id is missing in the Database
				
				db.insertContest(player_1, player_2, location, date, result, winner);

				Contest contest = new Contest(player_1, player_2, location, date, result, winner);
				model.addAttribute("contest", contest);
				returnPage = "contest_confirm.html";

			}

			else {

				returnPage = "wrongInput.html";
			}

		}

		else {

			returnPage = "wrongInput.html";
		}

		db.close();

		return returnPage;

	}

}

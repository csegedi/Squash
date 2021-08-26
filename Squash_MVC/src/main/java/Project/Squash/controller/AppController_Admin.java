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
			@RequestParam(name = "username") String adminName,
			@RequestParam(name = "password") String password,
			HttpServletResponse response) {

		Database db = new Database();
		String returnPage = "";

		List<Player> list_of_admins = db.getAdmins(adminName, password);

		if (list_of_admins.isEmpty()) {
			returnPage = "failed_login.html";
		}

		else {

			Cookie cookie = new Cookie("cookie_username", adminName);
			Cookie cookie2 = new Cookie("cookie_password", password);
			response.addCookie(cookie);
			response.addCookie(cookie2);

			returnPage = "admin.html";

		}

		db.close();

		return returnPage;

	}

	@PostMapping("admin/addLocation")
	public String addLocation(Model model,
			@RequestParam(name = "name") String name, @RequestParam(name = "address") String address,
			@RequestParam(name = "rent") int rent, @CookieValue(value = "cookie_username") String userName,
			@CookieValue(value = "cookie_password") String password) {

		Database db = new Database();

		db.insertLocation(name, address, rent);

		Location location = new Location(name, address, rent);

		RestTemplate restTemplate = new RestTemplate();
		String currency = restTemplate.getForObject("http://localhost:8080/rest", String.class);
		currency.replace(',', '.');
		double changed = Double.parseDouble(currency);
		double rent_in_EUR = (rent / changed);

		model.addAttribute("location", location);
		model.addAttribute("EUR", rent_in_EUR);
		model.addAttribute("name", userName);
		model.addAttribute("pass", password);

		db.close();

		return "location_confirm.html";

	}

	@PostMapping("admin/addPlayer")
	public String addPlayer(Model model, 
			@RequestParam(name = "playerName") String playerName,
			@CookieValue(value = "cookie_username") String userName,
			@CookieValue(value = "cookie_password") String password) {

		Database db = new Database();

		int randomNumber = new Random().nextInt(100) + 1;
		String generatedPassword = (playerName + randomNumber);
		Player_Status status = Player_Status.PLAYER;
		boolean initValue = false;

		db.insertPlayer(playerName, generatedPassword, status.toString(), initValue);

		Player player = new Player(playerName, generatedPassword, status, initValue);

		model.addAttribute("player", player);
		model.addAttribute("name", userName);
		model.addAttribute("pass", password);

		db.close();

		return "player_confirm.html";

	}

	@PostMapping("admin/addContest")
	public String addContest(Model model, 
			@CookieValue(value = "cookie_username") String userName,
			@CookieValue(value = "cookie_password") String password, 
			@RequestParam(name = "player1_id") int player_1_id,
			@RequestParam(name = "player2_id") int player_2_id,
			@RequestParam(name = "location_id") int location_id,
			@RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam(name = "result") String result, 
			@RequestParam(name = "winner") int winner_id) {

		Database db = new Database();

		db.insertContest(player_1_id, player_2_id, location_id, date, result, winner_id);

		Player player_1 = db.getPlayersById(player_1_id);
		Player player_2 = db.getPlayersById(player_2_id);
		Player winner = db.getPlayersById(winner_id);
		Location location = db.getLocationsById(location_id);

		Contest contest = new Contest(player_1, player_2, location, date, result, winner);

		model.addAttribute("contest", contest);
		model.addAttribute("name", userName);
		model.addAttribute("pass", password);

		db.close();

		return "contest_confirm.html";

	}

}

package Project.Squash.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import Project.Squash.db.Database;
import Project.Squash.model.Contest;
import Project.Squash.model.Location;
import Project.Squash.model.Player;
import Project.Squash.model.Player_Status;

@Controller
public class AppController_Player {

	@PostMapping("/player")
	public String playerEnterAndCheck(Model model,
			@RequestParam(required = false, name = "username") String pName,
			@RequestParam(required = false, name = "password") String pPassword,
			@CookieValue (required = false, name="cookie_username") String incomingName,
			@CookieValue (required = false, name="cookie_password") String incomingPassword,
			HttpServletResponse response) {

		String page = null;
		Player player = null;
		Database db = new Database();
	
		List<Player> player_list = db.getPlayersByEntry(pName, pPassword);
		
		for (int player_listIndex=0; player_listIndex<player_list.size(); player_listIndex++) {
			if (player_list.get(player_listIndex).getStatus()==Player_Status.PLAYER) {
				player=player_list.get(player_listIndex); 
				break; 
			}
		}
		
		/**PLAYER'S ENTRY AND CHECKING (NAME, PASSWORD, STATUS, OR COOKIES*/

		if ( (player == null) && (incomingName==null) && (incomingPassword==null)) {
			
			
			page = "failed_login";
			
		} 
		
		/** IF THE PLAYER LOG IN FIRST TIME, HE SHOULD CHANGE THE PASSWORD CREATED BY THE ADMIN*/
		
		else if ( (player!=null)  && (player.isPassword_flag() == false)) {  //if the stand of flag is false, the player never was on the site
			
			model.addAttribute("p_Name", pName);
			model.addAttribute("p_Password", pPassword ); 
			
			page = "new_password.html";
		}
		
		else if ( ( (incomingName!=null) && (incomingPassword!=null) )  ||                                   //check the cookies if he step back from another page
				( (player!=null) ) && (player.isPassword_flag() == true)  ) { //first enter to the page
				
				if ( (incomingName==null) && (incomingPassword==null) ) {
					Cookie cookie = new Cookie("cookie_username", pName);
					Cookie cookie2 = new Cookie("cookie_password", pPassword);
					response.addCookie(cookie);
					response.addCookie(cookie2);
				}
				
				/**SELECTING THE PLAYERS AND THE LOCATIONS FROM THE DATABASE TO CREATE FILTERED LIST*/
				
				List<Contest> contests = db.getAllContests();
				ArrayList<Location> locations = new ArrayList<Location>();
				ArrayList<Player> allPlayers = new ArrayList<Player>();
				ArrayList<String> localCheckList = new ArrayList<String>();
				ArrayList<String> playerCheckList = new ArrayList<String>();

				Collections.sort(contests, Comparator.comparing(Contest::getDate).reversed());
				
				for (int i = 0; i < contests.size(); i++) {

					Location local = contests.get(i).getLocation_id();

					if ((localCheckList.contains(local.getName()) == false)) {  //filter the locations from the database
						localCheckList.add(local.getName());
						locations.add(local);
					}

					Player player1 = contests.get(i).getPlayer_1();

					if ((playerCheckList.contains(player1.getUsername()) == false)) { //filter the player1 from the database
						playerCheckList.add(player1.getUsername());
						allPlayers.add(player1);
					}

					Player player2 = contests.get(i).getPlayer_2(); //filter player2 from the database

					if ((playerCheckList.contains(player2.getUsername()) == false)) {
						playerCheckList.add(player2.getUsername());
						allPlayers.add(player2);
					}

				}
				
				model.addAttribute("list", contests);
				model.addAttribute("local_list", locations);
				model.addAttribute("player_list", allPlayers);

				page = "playerPage.html";
			}

		
			return page;
		}


	@PostMapping("password_confirm")
	public String passwordConfirm(Model model, 
			@RequestParam(name = "newPassword") String newPassword,
			@RequestParam(name = "playerName") String pName,
			@RequestParam (name = "playerPassword") String pPassword ) {

		Database db = new Database();
		boolean value = true;
	
		db.updatePlayerPassword(pName, newPassword, value, pPassword);

		db.close();
		
		return "password_confirm.html";

	}

	@PostMapping("/player/selectedPlayer")
	public String selectedPlayer(Model model, 
			@RequestParam(name = "selected_player") int id,
			@CookieValue(value = "cookie_username") String userName,
			@CookieValue(value = "cookie_password") String password) throws SQLIntegrityConstraintViolationException {

		Database db = new Database();
		List<Contest> contests = null;
		Player player = db.getPlayersById(id);

		contests = db.getContestsByPlayer(player);

		model.addAttribute("contestsList", contests);
		model.addAttribute("name", userName);
		model.addAttribute("pass", password);

		db.close();

		return "selectedPlayer.html";

	}

	@PostMapping("player/selectedLocation")
	public String selectedLocation(Model model, 
			@RequestParam(name = "selected_location") int id,
			@CookieValue(value = "cookie_username") String userName,
			@CookieValue(value = "cookie_password") String password) {

		Database db = new Database();
		List<Contest> contests = null;
		Location location = db.getLocationsById(id);

		RestTemplate restTemplate = new RestTemplate();
		String currency = restTemplate.getForObject("http://localhost:8080/rest", String.class);
		currency.replace(',', '.');
		double changed = Double.parseDouble(currency);
		double rent_in_EUR = (location.getRent() / changed);

		contests = db.getContestsByLocation(location);

		model.addAttribute("local", location);
		model.addAttribute("contestsList", contests);
		model.addAttribute("EUR", rent_in_EUR);
		model.addAttribute("name", userName);
		model.addAttribute("pass", password);

		db.close();

		return "selectedLocation.html";

	}

}

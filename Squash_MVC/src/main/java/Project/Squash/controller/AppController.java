package Project.Squash.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class AppController {

	@GetMapping("/")
	public String mainPage() {
		
		return "index.html";
		
	}
	
	@GetMapping("/admin_login")
	public String adminLogin() {
		
		return "admin_login.html";
		
	}
	
	@GetMapping("/player_login")
	public String playerLogin() {
		
		return "player_login.html";
		
	}
	
	@PostMapping ("/admin")
	public String adminPage(Model model, 
			@RequestParam (name="adminName") String adminName,
			@RequestParam (name="password") String password) {
		
		Database db=new Database();  
		String returnPage=""; 
		Player admin=null; 
		
		List<Player> list_of_admins=db.getAdmins(adminName, password); 
		
		if (list_of_admins.isEmpty()) {
			returnPage="admin_denied.html"; 
		}
		
		else {
			
			returnPage="admin.html"; 
			for (int index=0; index<list_of_admins.size(); index++) {
				admin=list_of_admins.get(index); 
			}
			
			model.addAttribute("admin_Name", admin.getUsername()); 
			model.addAttribute("pass_word", admin.getPassword()); 
			
		}
		
		db.close(); 
		
		return returnPage;
		
	}
	
	@PostMapping("/addLocation")
	public String addLocation (Model model,
			@RequestParam (name="adminName") String adminName,
			@RequestParam (name="password") String password,
			@RequestParam (name="name") String name,
			@RequestParam (name="address") String address,
			@RequestParam (name="rent") int rent) {
		 
		Database db=new Database();
		Location location=null; 
		List<Location> locations=null; 
				
		db.insertLocation(name, address, rent); 
		locations=db.getLocation(name, address, rent);
		
		for (int index=0; index<locations.size(); index++) {
			location=locations.get(index); 
		}
		
		RestTemplate restTemplate=new RestTemplate(); 
		String currency=restTemplate.getForObject("http://localhost:8081/rest",String.class);
		currency.replace(',', '.'); 
		double changed=Double.parseDouble(currency); 
		double rent_in_EUR=(rent/changed); 
		
		model.addAttribute("location", location); 
		model.addAttribute("EUR", rent_in_EUR); 
		model.addAttribute("admin_Name", adminName); 
		model.addAttribute("pass_word", password); 
		
		db.close(); 
	
		return "location_confirm.html";
		
	}
	
	@PostMapping("/addPlayer")
	public String addPlayer (Model model,
			@RequestParam (name="adminName") String adminName,
			@RequestParam (name="password") String password,
			@RequestParam (name="playerName") String playerName)  {
		
		Database db=new Database(); 
		Player player=null; 
		List <Player> playerList=null; 
		int randomNumber=new Random ().nextInt(100)+1; 
		String generatedPassword=(playerName+randomNumber); 
		String status=Player_Status.PLAYER.toString(); 
		
		db.insertPlayer(playerName, generatedPassword, status, false); 
		
		playerList=db.getPlayer(playerName, status); 
		for (int index=0; index<playerList.size(); index++) {
			player=playerList.get(index); 
		}
		
		model.addAttribute("player", player); 
		model.addAttribute("admin_Name", adminName); 
		model.addAttribute("pass_word", password);
		
		db.close(); 
	
		return "player_confirm.html";
		
	}
	
	@PostMapping ("/addContest")
	public String addContest (Model model,
			@RequestParam (name="adminName") String adminName,
			@RequestParam (name="password") String password,
			@RequestParam (name="player1_id") int player_1_id,
			@RequestParam (name="player2_id") int player_2_id,
			@RequestParam (name="location_id") int location_id,
			@RequestParam (name="date") @DateTimeFormat (pattern = "yyyy-MM-dd") Date date,
			@RequestParam (name="result") String result,
			@RequestParam (name="winner") int winner_id
			) {
		
		List<Contest>contests=null; 
		Contest contest=null; 
		Database db=new Database(); 
	
		
		db.insertContest(player_1_id, player_2_id, location_id, date, result, winner_id); 
		contests=db.getContest(player_1_id, player_2_id, location_id, date, result, winner_id); 
		 
		for (int contests_index=0; contests_index<contests.size(); contests_index++ ) {
			contest=contests.get(contests_index);
			
		}
		
		System.out.println (contest.getPlayer_1().getUsername()); 
		
		model.addAttribute("contest", contest); 
		model.addAttribute("admin_Name", adminName); 
		model.addAttribute("pass_word", password);
		
		db.close(); 
	
		return "contest_confirm.html";
		
	}
	
}

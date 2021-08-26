package Project.Squash.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Project.Squash.db.Database;
import Project.Squash.model.Contest;
import Project.Squash.model.Location;
import Project.Squash.model.Player;
import Project.Squash.model.Player_Status;

@Controller
public class AppController_Player {
	
	@PostMapping("/player")
	public String player (Model model, 
			@RequestParam (name="username") String pName,
			@RequestParam (name="password") String pPassword) {
		
		String page=""; 
		Player player=null; 
		Database db=new Database(); 
		
		List<Player>player_list=db.getPlayersByEntry(pName, pPassword); 
		
		for (int i=0; i<player_list.size(); i++) {
			player=player_list.get(i); 
		}
		
		if (player==null) {
			page="failed_login"; 
		}
		else {
			
			if ((player.getStatus().equals(Player_Status.PLAYER)) && player.isPassword_flag()==false  ) {
			model.addAttribute("p_Name", pName); 
			
			page="new_password.html";
			
		}
		else if ((player.getStatus().equals(Player_Status.PLAYER)) && player.isPassword_flag()==true) {
			page="playerPage";
		}
		else {
			page="failed_login.html"; 
		}
			
			
		}
		
		return page;
		
	}
	
	@PostMapping("password_confirm")
	public String passwordConfirm (Model model, 
			@RequestParam (name="newPassword") String newPassword,
			@RequestParam (name="playerName") String pName) {
		
		Database db=new Database(); 
		boolean value=true; 
		db.updatePlayerPassword(pName, newPassword, value); 
		
		return "password_confirm.html";
		
	}
	
	@GetMapping ("/playerPage")
	public String playerPage (Model model,
			@RequestParam (name="playerName") String pName,
			@RequestParam (name="playerPassword") String pPassword) {
		
		Database db=new Database();
		
		Location local=null;
		List<Contest>contests=db.getAllContests(); 
		System.out.println (contests.get(0).getPlayer_1().getUsername()); 
	
		List<Location>locations=null; 
		 
		
		
		for (int i=0; i<contests.size(); i++) {
		 local=contests.get(i).getLocation_id(); 
		 locations.add(local); 
		}
		
		model.addAttribute("list", contests);
		model.addAttribute("local-list", locations);
		
		
		return "playerPage.html";
		
	}

}

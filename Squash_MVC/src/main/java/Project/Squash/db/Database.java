package Project.Squash.db;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import Project.Squash.model.Contest;
import Project.Squash.model.Location;
import Project.Squash.model.Player;
import Project.Squash.model.Player_Status;

public class Database {
	
	private SessionFactory sessionFactory;

	public Database() {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	
	public  List <Player> getAdmins(String username, String password) {
		
		List<Player> list_of_admins=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createQuery("SELECT p FROM Player p WHERE p.username=:pusername AND p.password=:ppassword", Player.class); 
		query.setParameter("pusername", username); 
		query.setParameter("ppassword", password); 
		list_of_admins=query.getResultList(); 
		
		
		session.getTransaction().commit();
		session.close();
		
		
		return list_of_admins;
		
	}
	
	public void insertLocation(String name, String address, int rent) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO locations  (name, address, rent) VALUES (:newName, :newAddress, :newRent) "); 
		query.setParameter("newName", name); 
		query.setParameter("newAddress", address); 
		query.setParameter("newRent", rent); 
		query.executeUpdate(); 
		
		session.getTransaction().commit();
		session.close();
		
	}
	

	public void insertPlayer(String playerName, String generatedPassword, String status, boolean value) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO players  (username, password, status, password_flag) VALUES (:newName, :newPassword, :newStatus, :newPasswordFlag) "); 
		query.setParameter("newName", playerName); 
		query.setParameter("newPassword", generatedPassword); 
		
		
		query.setParameter("newStatus", status);
		
		query.setParameter("newPasswordFlag", value);
		query.executeUpdate(); 
		
		session.getTransaction().commit();
		session.close();
		
	}
	
	
	public Player getPlayersById(int player_1_id) {
		
		Player player=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		player=session.get(Player.class, player_1_id); 
		
		session.getTransaction().commit();
		session.close();
		
		
		return player;
		
	}
	
	public Location getLocationsById(int location_id) {
		
		Location location=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		location=session.get(Location.class, location_id); 
		
		
		session.getTransaction().commit();
		session.close();
		
		return location;
	}
	
	public void insertContest(int player_1_id, int player_2_id, int location_id, Date date, String result, int winner) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO contests  (player_1, player_2, location_id, date, result, winner) VALUES (:newPlayer_1, :newPlayer_2, :newLocation_id, :newDate, :newResult, :newWinner ) "); 
		query.setParameter("newPlayer_1", player_1_id); 
		query.setParameter("newPlayer_2", player_2_id); 
		query.setParameter("newLocation_id", location_id);
		query.setParameter("newDate", date);
		query.setParameter("newResult", result );
		query.setParameter("newWinner", winner);
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();
		session.close();
		
	}
	
	
	public List<Player> getPlayersByEntry(String playerName, String playerPassword) {

		List<Player> list=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM players WHERE username=:newName AND password=:newPassword", Player.class); 
		query.setParameter("newName", playerName); 
		query.setParameter("newPassword", playerPassword);
		list=query.getResultList(); 
		
		session.getTransaction().commit();
		session.close();
		
		return list;
	
	}
	
	public void updatePlayerPassword(String playerName, String newPassword, boolean value) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("UPDATE players SET password=:newPassword, password_flag=:newValue " + "WHERE username=:newName", Player.class); 
		query.setParameter("newName", playerName); 
		query.setParameter("newPassword", newPassword);
		query.setParameter("newValue", value);
	
		query.executeUpdate(); 
		
		session.getTransaction().commit();
		session.close();
		
	}
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	public List<Contest> getAllContests() {
		
		List<Contest> contestList=null; 
	
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
	
		Query query=session.createNativeQuery("SELECT * FROM contests", Contest.class);
		
		contestList=query.getResultList(); 
		for (int i=0; i<contestList.size(); i++) {
			
		System.out.println (contestList.get(i).getPlayer_2()); 
		System.out.println (contestList.get(i).getLocation_id()); 
		System.out.println (contestList.get(i).getWinner());
			
		}
		 
		session.getTransaction().commit();
		session.close();
		
		return contestList;
	}
	
	
	public List <Contest> getContestsByPlayer(Player player) {
		
		List<Contest> contestList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
	
		Query query=session.createNativeQuery("SELECT * FROM contests WHERE player_1=:newPlayer1 OR player_2=:newPlayer2", Contest.class);
		query.setParameter("newPlayer1", player); 
		query.setParameter("newPlayer2", player);
		
		contestList=query.getResultList(); 
		
		for (int i=0; i<contestList.size(); i++) {
				
		System.out.println (contestList.get(i).getPlayer_2()); 
		System.out.println (contestList.get(i).getLocation_id()); 
			System.out.println (contestList.get(i).getWinner());
				
		}
		 
		session.getTransaction().commit();
		session.close();
		
		return contestList;
		
	}
	
	public List<Contest> getContestsByLocation(Location location) {
		
		List<Contest> contestList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
	
		Query query=session.createNativeQuery("SELECT * FROM contests WHERE location_id=:newLocation" , Contest.class);
		query.setParameter("newLocation", location); 
		
		
		contestList=query.getResultList(); 
		
		for (int i=0; i<contestList.size(); i++) {
				
		System.out.println (contestList.get(i).getPlayer_2()); 
		System.out.println (contestList.get(i).getLocation_id()); 
			System.out.println (contestList.get(i).getWinner());
				
		}
		 
		session.getTransaction().commit();
		session.close();
		
		return contestList;
		
	}
	
	
	
	
	public void close () {
		sessionFactory.close();
	}


	


	


	


	


	


	


	


	


	


}

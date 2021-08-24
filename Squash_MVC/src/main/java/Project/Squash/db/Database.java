package Project.Squash.db;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import Project.Squash.model.Contest;
import Project.Squash.model.Location;
import Project.Squash.model.Player;

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
	
	public List <Location> getLocation(String name, String address, int rent) {
		
		List<Location> list=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM locations WHERE name=:newName AND address=:newAddress AND rent=:newRent", Location.class); 
		query.setParameter("newName", name); 
		query.setParameter("newAddress", address); 
		query.setParameter("newRent", rent);
		list=query.getResultList(); 
		
		session.getTransaction().commit();
		session.close();
		
		return list;
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
	
	public List <Player> getPlayer(String playerName, String status) {
		
		List <Player> playerList=null;
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM players WHERE username=:newName AND status=:newStatus", Player.class); 
		query.setParameter("newName", playerName); 
		query.setParameter("newStatus", status);
		playerList=query.getResultList(); 
		
		session.getTransaction().commit();
		session.close();

		return playerList;
	}
	
	public Player getPlayerById(int player_1_id) {
		
		Player player=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		player=session.get(Player.class, player_1_id); 
		
		
		session.getTransaction().commit();
		session.close();
		
		
		return player;
		
	}
	
	public Location getLocationById(int location_id) {
		
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
	
	public List<Contest> getContest(int player_1_id, int player_2_id, int location_id, Date date, String result, int winner) {
		
		List<Contest> contest_list=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		Query query=session.createNativeQuery("SELECT * FROM contests WHERE player_1=:newPlayer_1 AND player_2=:newPlayer_2 AND location_id=:newLocation_id AND date=:newDate AND result=:newResult AND winner=:newWinner", Contest.class);
		query.setParameter("newPlayer_1", player_1_id); 
		query.setParameter("newPlayer_2", player_2_id); 
		query.setParameter("newLocation_id", location_id);
		query.setParameter("newDate", date);
		query.setParameter("newResult", result );
		query.setParameter("newWinner", winner);
		
		contest_list=query.getResultList(); 
		
		session.getTransaction().commit();
		session.close();
		
		return contest_list;
	}
	
	
	
	
	public void close () {
		sessionFactory.close();
	}


	


	


	


	


}

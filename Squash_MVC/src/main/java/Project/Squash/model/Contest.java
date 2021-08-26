package Project.Squash.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="contests")
public class Contest {
	
	@Id
	@Column (name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn (name="player_1")
	private Player player_1; 
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn (name="player_2")
	private Player player_2; 
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn (name="location_id")
	private  Location location_id; 
	
	@Column (name="date")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private Date date; 

	
	@Column (name="result")
	private String result;
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn (name="winner")
	private Player winner; 
	
	
	public Contest () {
		
	}
	
	public Contest(Player player_1, Player player_2, Location location_id, Date date, String result, Player winner) {
		super();
		this.player_1 = player_1;
		this.player_2 = player_2;
		this.location_id = location_id;
		this.date = date;
		this.result = result;
		this.winner = winner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Player getPlayer_1() {
		return player_1;
	}

	public void setPlayer_1(Player player_1) {
		this.player_1 = player_1;
	}

	public Player getPlayer_2() {
		return player_2;
	}

	public void setPlayer_2(Player player_2) {
		this.player_2 = player_2;
	}

	public Location getLocation_id() {
		return location_id;
	}

	public void setLocation_id(Location location_id) {
		this.location_id = location_id;
	}



	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Contest [id=" + id + ", player_1=" + player_1 + ", player_2=" + player_2 + ", location_id="
				+ location_id + ", date=" + date + ", result=" + result + ", winner=" + winner + "]";
	}

}

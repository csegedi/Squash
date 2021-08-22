package Project.Squash.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="players")
public class Player {
	
	@Id
	@Column (name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column(name="username")
	private String username;
	
	@Column (name="password")
	private String password; 
	
	@Column (name="status")
	private String status; 
	
	@Column (name="password_flag")
	private boolean password_flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPassword_flag() {
		return password_flag;
	}

	public void setPassword_flag(boolean password_flag) {
		this.password_flag = password_flag;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", username=" + username + ", password=" + password + ", status=" + status
				+ ", password_flag=" + password_flag + "]";
	} 
	
	

}

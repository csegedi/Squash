package Project.Squash.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="locations")
public class Location {
	
	@Id
	@Column (name="id")
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column (name="name")
	private String name; 
	
	@Column (name="adress")
	private String address; 
	
	@Column (name="rent")
	private int rent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", name=" + name + ", address=" + address + ", rent=" + rent + "]";
	} 
	

}

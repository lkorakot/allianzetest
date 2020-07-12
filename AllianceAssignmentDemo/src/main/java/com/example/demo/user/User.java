package com.example.demo.user;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    private String user;
    private String token;
    private LocalDateTime expired;
    
	public User() {
	}

	public User(String user, String token, LocalDateTime expired) {
		this.user = user;
		this.token = token;
		this.expired = expired;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getExpired() {
		return expired;
	}
	public void setExpired(LocalDateTime expired) {
		this.expired = expired;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", user=" + user + ", token=" + token + ", expired=" + expired + "]";
	}
        
}

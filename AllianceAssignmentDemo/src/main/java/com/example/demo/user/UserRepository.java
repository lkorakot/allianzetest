package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	//find user by name
	User findByUser(String user);
	//find user by token
	User findByToken(String token);
}

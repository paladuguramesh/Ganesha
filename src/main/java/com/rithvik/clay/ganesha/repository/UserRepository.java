package com.rithvik.clay.ganesha.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rithvik.clay.ganesha.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.userName = ?1 and u.password=?2")
	User findUserByUserNameAndPassword(String userName, String password);
	
}
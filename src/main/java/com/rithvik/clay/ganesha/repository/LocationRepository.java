package com.rithvik.clay.ganesha.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rithvik.clay.ganesha.entity.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
	
	@Query("SELECT l FROM Location l WHERE l.id = ?1")
	Location findLocationById(Long id);
	
}
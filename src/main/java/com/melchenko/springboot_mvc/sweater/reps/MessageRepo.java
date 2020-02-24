package com.melchenko.springboot_mvc.sweater.reps;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.melchenko.springboot_mvc.sweater.domain.Message;

public interface MessageRepo extends CrudRepository<Message, Long> {
	
	Page<Message> findAll(Pageable pageable);
	
//	List<Message> findByTag(String tag, Pageable pageable);
	Page<Message> findByTag(String tag, Pageable pageable);
	

}

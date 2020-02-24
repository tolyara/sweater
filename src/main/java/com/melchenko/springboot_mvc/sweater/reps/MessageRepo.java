package com.melchenko.springboot_mvc.sweater.reps;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.melchenko.springboot_mvc.sweater.domain.Message;

public interface MessageRepo extends CrudRepository<Message, Long> {
	
	List<Message> findByTag(String tag, Pageable pageable);

}

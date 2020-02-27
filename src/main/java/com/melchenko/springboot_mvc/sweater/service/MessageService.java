package com.melchenko.springboot_mvc.sweater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.dto.MessageDTO;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepo messageRepo;
	
	public Page<MessageDTO> messageList(Pageable pageable, String filter, User user) {
		if (filter != null && !filter.isEmpty()) {
			return messageRepo.findByTag(filter, pageable, user);
		} else {
			return messageRepo.findAll(pageable, user);
		}
	}

	public Page<MessageDTO> messageListForUser(Pageable pageable, User currentUser, User author) {
		return messageRepo.findByUser(pageable, author, currentUser);
	}
	
//	public static void main(String[] args) {
//		List list = entityManager.createQuery("from Message m").getResultList();
//		System.out.println(list);
//	}

}

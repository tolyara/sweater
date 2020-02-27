package com.melchenko.springboot_mvc.sweater.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.melchenko.springboot_mvc.sweater.dto.MessageDTO;

@Controller
public class TestController {

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/test")
	public String test() {

//		System.out.println(entityManager);
		List list = entityManager.createQuery("select new com.melchenko.springboot_mvc.sweater.dto.MessageDTO("
				+ "m, "
				+ "count(ml), "
				+ "sum(case when ml = :user then 1 else 0 end) > 0 "
				+ "false) "
				+ "from Message m left join m.likes ml "
				+ "group by m "
				+ "order by m.id desc")
				.getResultList();
		for (Object object : list) {
			System.out.println(((MessageDTO) object));
		}

		return "parts/test";
	}

}

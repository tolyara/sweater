package com.melchenko.springboot_mvc.sweater.reps;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.dto.MessageDTO;

public interface MessageRepo extends CrudRepository<Message, Long> {
	
	@Query("select new com.melchenko.springboot_mvc.sweater.dto.MessageDTO("
				+ "m, "
				+ "count(ml), "
				+ "sum(case when ml = :user then 1 else 0 end) > 0 "
				+ ") "
				+ "from Message m left join m.likes ml "
				+ "group by m ")
	Page<MessageDTO> findAll(Pageable pageable, @Param("user") User user);
	
//	List<Message> findByTag(String tag, Pageable pageable);
	@Query("select new com.melchenko.springboot_mvc.sweater.dto.MessageDTO("
			+ "m, "
			+ "count(ml), "
			+ "sum(case when ml = :user then 1 else 0 end) > 0 "
			+ ") "
			+ "from Message m left join m.likes ml "
			+ "where m.tag = :tag "
			+ "group by m")
	Page<MessageDTO> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

//	@Query("from Message as m where m.author = :author")
	@Query("select new com.melchenko.springboot_mvc.sweater.dto.MessageDTO("
			+ "m, "
			+ "count(ml), "
			+ "sum(case when ml = :user then 1 else 0 end) > 0 "
			+ ") "
			+ "from Message m left join m.likes ml "
			+ "where m.author = :author "
			+ "group by m ")
	Page<MessageDTO> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
	

}

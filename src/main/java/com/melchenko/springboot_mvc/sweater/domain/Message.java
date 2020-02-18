package com.melchenko.springboot_mvc.sweater.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "Please fill the messsage text")
	@Length(max = 2048, message = "Message is too long (more than 2kb)") 	// setted in migration
	private String text;
	
	@Length(max = 255, message = "Tag is too long (more than 255)") 	// setted in migration
	private String tag;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User author;
	
	private String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Message() {
		
	}

	public Message(String text, String tag, User author) {
		this.text = text;
		this.tag = tag;
		this.author = author;
	}

	public Message(String text, String tag) {
		this.text = text;
		this.tag = tag;
	}
	
	public String getAuthorName() {
		return author != null ? author.getUsername() : "<none>";
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}

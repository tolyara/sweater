package com.melchenko.springboot_mvc.sweater.util;

import com.melchenko.springboot_mvc.sweater.domain.User;

public abstract class MessageHelper {

	public static String getAuthorName(User author) {
		return author != null ? author.getUsername() : "<none>";
	}

}

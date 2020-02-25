package com.melchenko.springboot_mvc.sweater;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class DumbPasswordEncoderTest {
	
	@Test
	public void encode() {
		DumbPasswordEncoder encoder = new DumbPasswordEncoder();
		Assert.assertEquals("secret: " + "mypwd", encoder.encode("mypwd"));
		Assert.assertThat(encoder.encode("mypwd"), Matchers.containsString("mypwd"));
	}

}

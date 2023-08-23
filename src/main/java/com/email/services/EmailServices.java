package com.email.services;

import org.springframework.web.multipart.MultipartFile;

public interface EmailServices {
	public boolean sendEmail(String from, String to, String subject, String content, MultipartFile multipartFile)
			throws Exception;
}

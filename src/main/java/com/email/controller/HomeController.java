package com.email.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.email.helper.Message;
import com.email.model.EmailData;
import com.email.services.EmailServices;

@Controller
public class HomeController {
	@Autowired
	private EmailServices emailServices;
	@Autowired
	private Environment environment;

	@GetMapping("/")
	public String homeHandler() {
		return "index";
	}

	@PostMapping("/sendMail")
	public String emailHandler(@ModelAttribute EmailData emailData, @RequestParam("emailFile") MultipartFile file,
			HttpSession session) {
		System.out.println(emailData);
		System.out.println(file.getOriginalFilename());
		try {
			String from = environment.getProperty("spring.mail.username");
			String to = "ravikantwaghmare82@gmail.com";
			String content = "First Name: " + emailData.getFirstName();
			content += ",<br>Last Name: " + emailData.getLastName();
			content += ",<br>Email: " + emailData.getEmail();
			content += ",<br>Phone Number: " + emailData.getPhone();
			content += ",<br>Description: " + emailData.getAbout();
			String subject = "Reviews for the project";
			boolean result = this.emailServices.sendEmail(from, to, subject, content, file);
			if (result) {
				session.setAttribute("message", new Message("Email sent successfully!!!", "alert-success"));
			} else {
				throw new Exception("Could not send message");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong mail not sent!!!", "alert-danger"));
		}
		return "redirect:/";
	}
}

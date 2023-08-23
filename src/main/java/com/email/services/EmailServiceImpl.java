package com.email.services;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class EmailServiceImpl implements EmailServices {
    @Autowired
	private JavaMailSender sender;
	@Override
	public boolean sendEmail(String from, String to, String subject, String content, MultipartFile multipartFile)
			throws Exception {
		boolean flag=false;
		try {
	         MimeMessage message=sender.createMimeMessage();
	         message.setFrom(from);
	         message.setRecipients(RecipientType.TO, to);
	         message.setSubject(subject);
	         message.setSentDate(new Date());
             //multipart 
	         Multipart multipart=new MimeMultipart();
	         
	          
            //Body type of email
	         BodyPart bodyPart=new MimeBodyPart();
	         bodyPart.setContent(content,"text/html");
	         multipart.addBodyPart(bodyPart);
           //Attachment
	         MimeBodyPart mimeBodyPart=new MimeBodyPart();
	         FileDataSource fileDataSource=new FileDataSource(convert(multipartFile));
	         mimeBodyPart.setDataHandler(new DataHandler(fileDataSource));
	         mimeBodyPart.setFileName(multipartFile.getOriginalFilename());
	         
	         multipart.addBodyPart(mimeBodyPart);
	         
	         message.setContent(multipart);
	         sender.send(message);
	         flag=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public File convert(MultipartFile multipartFile) throws Exception{
		File file=new File(multipartFile.getOriginalFilename());
		file.createNewFile();
		FileOutputStream  fileOutputStream=new FileOutputStream(file);
		fileOutputStream.write(multipartFile.getBytes());
		fileOutputStream.close();
		return file;
	}

}

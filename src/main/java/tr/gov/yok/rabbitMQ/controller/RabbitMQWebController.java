package tr.gov.yok.rabbitMQ.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.gov.yok.rabbitMQ.model.Attachment;
import tr.gov.yok.rabbitMQ.model.Mail;
import tr.gov.yok.rabbitMQ.service.GenerateIdService;
import tr.gov.yok.rabbitMQ.service.RabbitMQSender;

@RestController
@RequestMapping(value="/sendMail")
public class RabbitMQWebController {
	
	@Autowired	
	RabbitMQSender rabbitMQSender;
	
	@Autowired
	GenerateIdService generateIdService;
	
	@PostMapping(value="/producer")
	public String producer(@RequestBody Mail mail) throws IOException {
		
		mail.setId(generateIdService.generateRandomPassword(10));
		mail.setSendTime(new Date().toString());
		
		// create logo attch
		Attachment attchLogo = new Attachment(); 					
		attchLogo.setFileBase64("templates/logo.png");
		attchLogo.setCid(true);
		attchLogo.setFileName("logo.png");
		
		if(mail.getAttachments().size() >0) {
			mail.getAttachments().add(attchLogo);
			
			for(Attachment attch : mail.getAttachments()) {
				attch.setId(generateIdService.generateRandomPassword(7));
				if(attch.getFilePath() != null) {
					attch.setFileBase64(FileToBase64(attch.getFilePath()));
					attch.setCid(false);
				}				
			}
		}else {				// hiç ek yoksa bile yokLogo eklensin
			attchLogo.setId(generateIdService.generateRandomPassword(7));
			mail.setAttachments(Arrays.asList(attchLogo));
		}
		
		rabbitMQSender.send(mail);
		
		System.out.println("Send mail to RabbitMQ " + mail);
		return "Mailiniz başarıyla RabbitMQ ya gönderildi..";
	}
	
	
	public String FileToBase64(String filePath) throws IOException {
		File file = new File(filePath);
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}

}

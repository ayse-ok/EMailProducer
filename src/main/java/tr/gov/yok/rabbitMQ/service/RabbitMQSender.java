package tr.gov.yok.rabbitMQ.service;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tr.gov.yok.rabbitMQ.model.Mail;

@Service
public class RabbitMQSender {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	@Value("${tr.gov.yok.rabbitMQ.exchange}")
	private String exchange;
	
	@Value("${tr.gov.yok.rabbitMQ.routingkey}")
	private String routingkey;
	
	String topic = "sendMail_rabbitMQ";
	
	public void send(Mail mail) {	
		amqpTemplate.convertAndSend(exchange, routingkey, mail);
		latch.countDown();
		
		logger.info("Sended mail = " + mail);		
		System.out.println("Sended mail..." + mail);	
	}
	
    public CountDownLatch getLatch() {
       return latch;
    }

}

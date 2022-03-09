package tr.gov.yok.rabbitMQ.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
@ComponentScan(basePackages = { "tr.gov.yok.rabbitMQ" })
public class RabbitMQConfig {
	
	@Value("${tr.gov.yok.rabbitMQ.queue}")
	String queueName;

	@Value("${tr.gov.yok.rabbitMQ.exchange}")
	String exchange;

	@Value("${tr.gov.yok.rabbitMQ.routingkey}") 
	private String routingkey;
	
	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}
	
	@Bean	
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}

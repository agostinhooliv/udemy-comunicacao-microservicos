package br.com.curso.udemy.product.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingkey.product-stock}")
    private String productStockKey;

    @Value("${app-config.rabbit.routingkey.sales-confirmation}")
    private String salesConfimationKey;

    @Value("${app-config.rabbit.queue.product-stock}")
    private String productStockKeyMq;

    @Value("${app-config.rabbit.queue.sales-confirmation}")
    private String salesConfimationMq;

    @Bean
    public TopicExchange productTopicExchange(){
        return new TopicExchange(productTopicExchange);
    }

    @Bean
    public Queue productStockKeyMq(){
        return new Queue(productStockKeyMq, true);
    }

    @Bean
    public Queue salesConfimationMq(){
        return new Queue(salesConfimationMq, true);
    }

    @Bean
    public Binding productStockKeyMqBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(productStockKeyMq())
                .to(topicExchange)
                .with(productStockKey);
    }

    @Bean
    public Binding salesConfimationMqBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(salesConfimationMq())
                .to(topicExchange)
                .with(salesConfimationKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

package br.com.curso.udemy.product.api.rabbitmq;

import br.com.curso.udemy.product.api.dto.SalesConfirmationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SalesConfirmationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingkey.sales-confirmation}")
    private String salesConfimationKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDTO salesConfirmationMessage){
        try {
            log.info("Sending message: {}", new ObjectMapper().writeValueAsString(salesConfirmationMessage));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfimationKey, salesConfirmationMessage);
            log.info("Message was sent successfully!");
        } catch (Exception ex){
            log.error("Error while trying to send sales confirmation message: ", ex);
        }
    }
}

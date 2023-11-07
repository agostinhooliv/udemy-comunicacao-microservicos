import amqp from "amqplib/callback_api.js";
import { RABBITMQ_URL } from "../secrets/secrets.js";
import { PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_ROUTING_KEY } from './queue.js';

export function sendProductStockUpdateQueue(message) {
    amqp.connect(RABBITMQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }

        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }

            let jsonStringMessage = JSON.stringify(message);
            console.info(`Sending message to product update stock: ${jsonStringMessage}`);

            try {
                channel.publish(
                    PRODUCT_TOPIC,
                    PRODUCT_STOCK_UPDATE_ROUTING_KEY,
                    Buffer.from(jsonStringMessage)
                );
                console.info("Message sent successfuly");

            } catch (error) {
                console.log("error");
            }

        });
    });
}

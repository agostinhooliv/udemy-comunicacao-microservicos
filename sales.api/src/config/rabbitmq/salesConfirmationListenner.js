import amqp from "amqplib/callback_api.js";
import { RABBITMQ_URL } from "../secrets/secrets.js";
import { SALES_CONFIRMATION_QUEUE } from './queue.js';
import OrderService from "../../modules/sales/service/OrderService.js";

export function listenConfirmationQueue() {
    amqp.connect(RABBITMQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }

        console.info("Listenner Sales confirmation queue");

        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }

            channel.consume(SALES_CONFIRMATION_QUEUE, (message) => {
                console.info(`Recieving message from queue: ${message.content.toString()}`);
                OrderService.updateOrder(message.content.toString());
            });
        });
    });
}
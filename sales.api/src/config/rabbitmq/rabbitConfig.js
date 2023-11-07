import amqp from "amqplib/callback_api.js";
import {
    PRODUCT_TOPIC,
    PRODUCT_STOCK_UPDATE_QUEUE,
    PRODUCT_STOCK_UPDATE_ROUTING_KEY,
    SALES_CONFIRMATION_QUEUE,
    SALES_CONFIRMATION_ROUTING_KEY
} from './queue.js';
import { RABBITMQ_URL } from "../secrets/secrets.js";
import { listenConfirmationQueue } from "../rabbitmq/salesConfirmationListenner.js";

const HALF_SECCOND = 500;
const HALF_MINUTE = 30000;
const CONTAINER_ENV = "container";


export async function connectRabbitMq() {
    const env = process.env.NODE_ENV;

    if (CONTAINER_ENV === env) {
        console.info("Waiting for RabbitMQ to start...");

        setInterval(() => {
            connectRabbitMqAndCreateQueues();
        }, HALF_MINUTE);
    } else {
        connectRabbitMqAndCreateQueues();
    }
}

async function connectRabbitMqAndCreateQueues() {
    amqp.connect(RABBITMQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }

        console.info("Starting rabbitmq");

        createQueue(
            connection,
            PRODUCT_STOCK_UPDATE_QUEUE,
            PRODUCT_STOCK_UPDATE_ROUTING_KEY,
            PRODUCT_TOPIC
        );

        createQueue(
            connection,
            SALES_CONFIRMATION_QUEUE,
            SALES_CONFIRMATION_ROUTING_KEY,
            PRODUCT_TOPIC
        );

        setTimeout(function () {
            connection.close();
        }, HALF_SECCOND);
    });

    listenConfirmationQueue();
}

async function createQueue(connection, queue, routingKey, topic) {
    connection.createChannel((error, channel) => {
        if (error) {
            throw error;
        }

        console.info("Queues and topic created");
        channel.assertExchange(topic, "topic", { durable: true });
        channel.assertQueue(queue, { durable: true });
        channel.bindQueue(queue, topic, routingKey);
    });
}   
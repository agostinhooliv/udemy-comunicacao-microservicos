import orderRepository from "../repository/orderRepository.js";
import { sendProductStockUpdateQueue } from "../../../config/rabbitmq/productStockUpdateSender.js";
import { ACCEPTED, REJECTED, PENDING } from "../status/orderStatus.js";
import OrderException from "../exception/OrderException.js";
import productClient from "../../client/productClient.js";

class OrderService {

    async createOrder(req) {

        try {

            let orderData = req.body;

            const { transactionid, serviceid } = req.headers;

            console.info(
                `Request to POST new Order with data ${JSON.stringify(
                    orderData
                )} 
                | [transactionID: ${transactionid} 
                    | serviceID: ${serviceid}]`
            );

            this.validateOrderData(orderData);
            const { authUser } = req;
            const { authorization } = req.headers;

            console.log(req.headers);


            let order = {
                status: PENDING,
                user: authUser,
                createAt: new Date(),
                updatedAt: new Date(),
                products: orderData.products
            };

            let stockisOut = await productClient.checkProductStock(order, authorization, transactionid);
            if (stockisOut) {
                throw new OrderException(400, "The stock is out.");
            }

            let createdOrder = await orderRepository.save(order);
            this.sendMessage(createdOrder);

            let response = {
                status: 200,
                createdOrder,
            };

            console.info(
                `Response to POST new Order with data ${JSON.stringify(
                    response
                )} 
                | [transactionID: ${transactionid} 
                    | serviceID: ${serviceid}]`
            );

            return response;

        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
        }
    }

    async updateOrder(orderMessage) {
        try {
            const order = JSON.parse(orderMessage);
            if (order.salesId && order.status) {
                let existingOrder = await orderRepository.findById(order.salesId);

                if (existingOrder && order.status !== existingOrder.status) {
                    existingOrder.status = order.status;
                    existingOrder.updatedAt = new Date();
                    await orderRepository.save(existingOrder);
                }
            } else {
                console.warn("The order message was not complete.");
            }
        } catch (error) {
            console.error("Could not parse order message from queue.");
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException("The products must be informed.");
        }
    }

    sendMessage(createdOrder) {
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products
        };
        sendProductStockUpdateQueue(message);
    }

    async findById(req) {

        try {

            const { id } = req.params;
            this.validateInformedId(id);

            const existingOrder = await orderRepository.findById(id);

            if (!existingOrder) {
                throw new OrderException(401, "The order was not found!");
            }

            return {
                status: 200,
                existingOrder
            }
        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
        }
    }

    async findAll() {

        try {

            const existingOrders = await orderRepository.findAll();

            if (!existingOrders) {
                throw new OrderException(401, "No order was not found!");
            }

            return {
                status: 200,
                existingOrders
            }
        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
        }
    }

    async findByProductId(req) {

        try {

            const { id } = req.params;
            this.validateInformedId(id);

            const existingOrders = await orderRepository.findByProductId(id);

            if (!existingOrders) {
                throw new OrderException(401, "No order was not found!");
            }

            return {
                status: 200,
                salesIds: existingOrders.map((order) => {
                    return order.id;
                }),
                // existingOrders
            };
        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
        }
    }

    validateInformedId(id) {
        if (!id) {
            throw new OrderException(500, "The id must be informed!");
        }
    }
}


export default new OrderService();
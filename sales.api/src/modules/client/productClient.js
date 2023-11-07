import axios from "axios";

import { PRODUCT_API_URL } from "../../config/secrets/secrets.js";

class ProductClient {
    async checkProductStock(products, token, transactionid) {
        try {
            const headers = {
                Authorization: `Bearer ${token}`,
                transactionid,
            };

            console.info(`Sending request to product API with data: ${JSON.stringify(products)} and transactionId ${transactionid}`);

            await axios
                .post(`${PRODUCT_API_URL}/check-stock`, { headers }, products)
                .then((res) => {
                    console.info(
                        `Sucess response from Product-API. TransactionId: ${transactionid}`
                    );
                    return true;
                })
                .catch((error) => {
                    console.error(
                        `Error response from Product-API. TransactionId: ${transactionid} \n ${error.message.error}`
                    );
                    return false;
                });
        } catch (error) {
            console.error(
                `Error response from Product-API. TransactionId: ${transactionid} \n ${error.message.error}`
            );
            return false;
        }
    }
}

export default new ProductClient();
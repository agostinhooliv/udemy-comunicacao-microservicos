import axios from "axios";

import { PRODUCT_API_URL } from "../../config/secrets/secrets.js";

class ProductClient {
    async checkProductStock(products, token) {
        try {
            const headers = {
                Authorization: `Bearer ${token}`
            };

            console.info(`Sending request to product API with data: ${JSON.stringify(products)}`);

            await axios
                .post(`${PRODUCT_API_URL}/check-stock`, { headers }, products)
                .then((res) => {
                    return true;
                })
                .catch((error) => {
                    console.error(error.response.message);
                    return false;
                });
        } catch (error) {
            return false;
        }
    }
}

export default new ProductClient();
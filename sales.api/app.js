import express from "express";
import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/initialData.js";
import CheckToken from "./src/config/auth/CheckToken.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";
import { sendProductStockUpdateQueue } from "./src/config/rabbitmq/productStockUpdateSender.js";
import orderRoutes from "./src/modules/sales/routes/OrderRoute.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

app.use(express.json());
app.use(CheckToken);
app.use(orderRoutes);


app.get('/api/teste', (req, res) => {
    try {
        sendProductStockUpdateQueue(
            [
                {
                    productId: 1,
                    quantity: 1
                },
                {
                    productId: 2,
                    quantity: 1
                },
                {
                    productId: 3,
                    quantity: 1
                },
            ]
        );
        return res.status(200).json({ status: 200 });

    } catch (error) {
        console.log(error);
        return res.status(500).json({ error: true });
    }
});

app.get('/api/status', async (req, res) => {
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200,
    });
});

app.listen(PORT, () => {
    console.info(`Server started sucessfuly at port ${PORT}`);
});
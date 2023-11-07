const env = process.env;

export const MONGO_DB_URL = env.MONGO_DB_URL ? env.MONGO_DB_URL : "mongodb://localhost:27017/sales-db";

export const RABBITMQ_URL = env.RABBITMQ_URL ? env.RABBITMQ_URL : "amqp://localhost:5672";

export const apiSecret = env.API_SECRET ? env.API_SECRET : "YXV0aC5hcGktc2VjcmV0LWRldmVsb3AxMjM0NTY=";

export const PRODUCT_API_URL = env.PRODUCT_API_URL ? env.PRODUCT_API_URL : "http://localhost:8081/api/product";
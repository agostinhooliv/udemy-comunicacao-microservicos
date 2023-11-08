

const env = process.env;

export const apiSecret = env.API_SECRET ? env.API_SECRET : "YXV0aC5hcGktc2VjcmV0LWRldmVsb3AxMjM0NTY=";

export const DB_HOST = env.DB_HOST ? env.DB_HOST : "localhost";
export const DB_NAME = env.DB_NAME ? env.DB_NAME : "auth-db";
export const DB_USER = env.DB_USER ? env.DB_USER : "admin";
export const DB_PASSWORD = env.DB_PASSWORD ? env.DB_PASSWORD : "123456";
export const DB_PORT = env.DB_PORT ? env.DB_PORT : "5432";
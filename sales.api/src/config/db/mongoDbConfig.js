import mongoose from "mongoose";
import { MONGO_DB_URL } from "../../config/secrets/secrets.js";

export function connectMongoDb() {
    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true,
    });
    mongoose.connection.on("connected", function () {
        console.info("The application connected to MongoDB sucessefully!");
    })
}
mongoose.connection.on("error", function () {
    console.info("The application connected to MongoDB error!");
})

import express from "express";
import * as db from "../auth.api/src/config/db/initialData.js";
import userRoutes from "./src/modules/routes/UserRoutes.js";
import checkToken from "./src/config/auth/checkToken.js";
import trace from "./src/config/trace.js";

db.createInitialData();

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

app.get("/api/status", (req, res) => {
    return res.status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200,
    });
});

app.use(trace);
app.use(express.json());
app.use(userRoutes);
app.use(checkToken);


app.listen(PORT, () => {
    console.info(`Server started sucessfuly at port ${PORT}`);
});
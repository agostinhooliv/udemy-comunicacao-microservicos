import { Router } from "express";
import OrderController from "../controller/OrderController.js";

const router = new Router();

router.get("/api/order/:id", OrderController.findById);
router.post("/api/order/create", OrderController.createOrder);
router.get("/api/orders", OrderController.findByAll);
router.get("/api/orders/product/:id", OrderController.findByProductId);

export default router;
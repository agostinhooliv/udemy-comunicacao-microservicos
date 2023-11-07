import { v4 as uuidv4 } from "uuid";

export default (req, res, next) => {

    let { transactionid } = req.headers;

    if (!transactionid) {
        return res.status(500).json({
            status: 500,
            message: "The transactionId header is required"
        });
    }

    req.headers.serviceid = uuidv4();

    return next();

};
import jwt from "jsonwebtoken";
import { promisify } from "util";

import * as secrets from "../../config/secrets/secrets.js";
import AuthException from "./AuthException.js";

const bearer = "Bearer";
const emptySpace = " ";

export default async (req, res, next) => {

    const { authorization } = req.headers;

    try {
        if (!authorization) {
            throw new AuthException(401, "AcessToken was not informed.");
        }

        let acessToken = authorization;

        if (acessToken.includes(emptySpace)) {
            const tokenType = acessToken.split(emptySpace)[0];
            if (tokenType !== bearer) {
                throw new AuthException(401, "Token type is not allowed.");
            }
            acessToken = acessToken.split(emptySpace)[1];
        }
        const decoded = await promisify(jwt.verify)(
            acessToken,
            secrets.apiSecret
        );
        req.authUser = decoded.authUser;

        return next();

    } catch (error) {
        const status = error.status ? error.status : 500;
        return res.status(status).json({
            status: error.status ? error.status : 500,
            message: error.message,
        }
        );
        console.error(error.message);
    }
};
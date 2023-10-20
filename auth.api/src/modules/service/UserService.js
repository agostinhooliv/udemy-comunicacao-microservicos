import userRepository from "../repository/userRepository.js";
import UserException from "../user/Exception/UserException.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import * as secrets from "../../config/constants/Secrets.js";

class UserService {

    async findByEmail(req) {
        try {
            const { email } = req.params;
            this.validarDadosRequisicao(email);
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);
            this.validateAuthenticatedUser(user, req.authUser);
            if (!user) {

            }
            return {
                status: 200,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                }
            }

        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
            console.error(error.message);
        }
    }

    validarDadosRequisicao(email) {
        if (!email) {
            throw new UserException(500, "User email was not informed");
        }
    }

    validateUserNotFound(user) {
        if (!user) {
            throw new UserException(400, "User was not found.");
        }
    }

    async getAcessToken(req) {

        try {
            const { email, password } = req.body;
            this.validateAcessTokenData(email, password);
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);
            await this.validatePassword(password, user.password);

            const authUser = { id: user.id, name: user.name };
            const acessToken = jwt.sign({ authUser }, secrets.apiSecret, {
                expiresIn: "1d",
            });

            return {
                status: 200,
                acessToken,
            }


        } catch (error) {
            return {
                status: error.status ? error.status : 500,
                message: error.message,
            }
            console.error(error.message);

        }
    }

    validateAcessTokenData(email, password) {
        if (!email || !password) {
            throw new UserException(401, "Email or passaword must be informed.");
        }
    }

    validateAuthenticatedUser(user, authUser) {
        if (!authUser || user.id !== authUser.id) {
            throw new UserException(403, "You cannot see this user data!");
        }
    }

    async validatePassword(password, hasPassword) {
        if (!await bcrypt.compare(password, hasPassword)) {
            throw new UserException(401, "Password invalid!");
        }
    }
}

export default new UserService();
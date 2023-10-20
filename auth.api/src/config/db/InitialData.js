import bcrypt from "bcrypt";
import user from "../../modules/user/User.js";

export async function createInitialData() {

    try {

        await user.sync({ force: true });

        let password = await bcrypt.hash("123456", 10);

        await user.create({
            name: "User teste1",
            email: "teste1@gmail.com",
            password: password,
        });

        await user.create({
            name: "User teste2",
            email: "teste2@gmail.com",
            password: password,
        });

    } catch (err) {
        console.log(err);
    }
}
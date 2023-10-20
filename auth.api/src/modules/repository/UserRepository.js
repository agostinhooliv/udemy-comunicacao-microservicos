import user from "../user/User.js";

class UserRepository {

    async findByEmail(email) {
        try {
            return await user.findOne({ where: { email } });
        } catch (error) {
            console.error(error.message);
            return null;
        }
    }

    async findById(id) {
        try {
            return await user.findOne({ where: { id } });
        } catch (error) {
            console.error(error.message);
            return null;
        }
    }

}

export default new UserRepository();
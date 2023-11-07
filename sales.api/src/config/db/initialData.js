import order from "../../modules/sales/model/order.js";

export async function createInitialData() {
    await order.collection.drop();

    let firstOrder = await order.create({
        products: [
            {
                productId: 1,
                quantity: 10
            },
            {
                productId: 2,
                quantity: 5
            },
            {
                productId: 3,
                quantity: 1
            }
        ],
        user: {
            id: "sdda4ds4ad4a",
            name: "Agostinho",
            email: "agostinhooliv@hotmail.com"
        },
        status: "APPROVED",
        createdAt: new Date(),
        updatedAt: new Date()
    });

    let secondOrder = await order.create({
        products: [
            {
                productId: 1,
                quantity: 10
            },
            {
                productId: 3,
                quantity: 1
            }
        ],
        user: {
            id: "sdda4ds4ad4a",
            name: "Agostinho",
            email: "agostinhooliv@hotmail.com"
        },
        status: "REJECTED",
        createdAt: new Date(),
        updatedAt: new Date()
    });

    let teste = await order.find();
    console.log(teste);
}
package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.Without_SRP;

public class Main {
    static void main() {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.addProduct(new Product("Laptop", 50000));
        shoppingCart.addProduct(new Product("Mouse", 2000));

        shoppingCart.printInvoice();
        String messageFromDB = shoppingCart.saveToDatabase();

        System.out.println(messageFromDB);
    }
}

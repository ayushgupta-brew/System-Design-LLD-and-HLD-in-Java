package Low_Level_Design.SOLID_Principles.Open_Closed_Principles.Without_OCP;

public class Main {
    static void main() {
        ShoppingCart cart = new ShoppingCart();

        cart.addProduct(new Product("Laptop", 50000));
        cart.addProduct(new Product("Mouse", 2000));

        ShoppingCartStorage storeToDB = new ShoppingCartStorage(cart);
        storeToDB.saveToSQLDatabase();
    }
}

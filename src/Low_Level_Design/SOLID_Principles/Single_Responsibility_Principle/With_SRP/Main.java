package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.With_SRP;

public class Main {
    static void main() {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.addProduct(new Product("Laptop", 50000));
        shoppingCart.addProduct(new Product("Mouse", 5000));

        ShoppingCartPrinter shoppingCartPrinter = new ShoppingCartPrinter(shoppingCart);
        shoppingCartPrinter.printInVoice();

        ShoppingCartStorage shoppingCartStorage = new ShoppingCartStorage(shoppingCart);
        String messageFromDB = shoppingCartStorage.saveToDataBase();
        System.out.println(messageFromDB);
    }
}

package Low_Level_Design.SOLID_Principles.Open_Closed_Principles.With_OCP;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final List<Product> products= new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public List<Product> getProducts() {
        return products;
    }

    // Calculates total price in cart.
    public double calculateTotal() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }
}

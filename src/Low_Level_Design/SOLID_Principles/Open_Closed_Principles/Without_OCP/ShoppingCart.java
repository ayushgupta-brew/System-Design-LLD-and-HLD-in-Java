package Low_Level_Design.SOLID_Principles.Open_Closed_Principles.Without_OCP;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final List<Product> products = new ArrayList<>();
    void addProduct(Product p) {
        products.add(p);
    }

    List<Product> getProducts() {
        return products;
    }

    double calculateTotal() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }
}
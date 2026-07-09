package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.With_SRP;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product){
        products.add(product);
    }
    public List<Product> getProducts(){
        return products;
    }
    public double calculateTotal(){
        double total = 0;
        for(Product product : products) {
            total += product.price;
        }
        return total;
    }
}

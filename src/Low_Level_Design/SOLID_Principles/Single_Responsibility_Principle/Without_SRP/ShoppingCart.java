package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.Without_SRP;

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
        for(Product product : products){
            total += product.price;
        }
        return total;
    }
    public void printInvoice() {
        System.out.println("Shopping Cart Invoice:");
        for (Product p : products) {
            System.out.println(p.name + " - Rs " + p.price);
        }
        System.out.println("Total: Rs " + calculateTotal());
    }

    public String saveToDatabase() {
        return "Saving shopping cart to database...";
    }
}

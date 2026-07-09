package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.With_SRP;

public class ShoppingCartPrinter {
    private final ShoppingCart cart;

    public ShoppingCartPrinter(ShoppingCart cart){
        this.cart = cart;
    }

    public void printInVoice(){
        System.out.println("Shopping Cart Invoice:");
        for (Product product : cart.getProducts()) {
            System.out.println(product.name + " - Rs " + product.price);
        }
        System.out.println("Total: Rs " + cart.calculateTotal());
    }
}

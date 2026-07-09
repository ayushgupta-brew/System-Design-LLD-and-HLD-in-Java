package Low_Level_Design.SOLID_Principles.Single_Responsibility_Principle.With_SRP;

public class ShoppingCartStorage {

    private final ShoppingCart cart;

    public ShoppingCartStorage(ShoppingCart cart){
        this.cart = cart;
    }

    public String saveToDataBase(){
        return "saving shopping cart to database...";
    }
}

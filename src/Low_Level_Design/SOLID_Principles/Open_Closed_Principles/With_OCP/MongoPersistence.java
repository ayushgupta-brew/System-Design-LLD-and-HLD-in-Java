package Low_Level_Design.SOLID_Principles.Open_Closed_Principles.With_OCP;

public class MongoPersistence implements Persistence{
    @Override
    public void save(ShoppingCart cart) {
        System.out.println("Saving shopping cart to MongoDB...");
    }
}

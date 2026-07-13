package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.Without_DIP;

// Low-level module
public class MongoDatabase {
    public void saveToMongo(String data) {
        System.out.println(
                "Executing MongoDB Function: db.users.insert({name: '"
                        + data + "'})"
        );
    }
}

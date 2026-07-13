package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.With_DIP;

public class MongoDatabase implements Database{
    @Override
    public void save(String data) {
        System.out.println(
                "Executing MongoDB Function: db.users.insert({name: '"
                        + data + "'})"
        );
    }
}

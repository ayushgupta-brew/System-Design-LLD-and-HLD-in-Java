package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.Without_DIP;

// High-level module (Tightly coupled)
public class UserService {
    private final MySQLDatabase mySQLDatabase = new MySQLDatabase();
    private final MongoDatabase mongoDatabase = new MongoDatabase();

    public void storeUserToSQL(String user) {
        // MySQL-specific code
        mySQLDatabase.saveToSQL(user);
    }

    public void storeUserToMongo(String user) {
        // MongoDB-specific code
        mongoDatabase.saveToMongo(user);
    }
}

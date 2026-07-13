package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.With_DIP;

// High-level module (Now loosely coupled via Dependency Injection)
public class UserService {
    private final Database db;


    public UserService(Database db) {
        this.db = db;
    }

    public void storeUser(String user) {
        db.save(user);
    }
}

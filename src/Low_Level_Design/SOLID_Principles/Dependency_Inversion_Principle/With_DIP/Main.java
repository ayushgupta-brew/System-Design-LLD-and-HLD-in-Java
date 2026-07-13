package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.With_DIP;

public class Main {
    static void main() {

        MySQLDatabase mysql = new MySQLDatabase();
        MongoDatabase mongodb = new MongoDatabase();

        UserService service1 = new UserService(mysql);
        service1.storeUser("Ayush");

        UserService service2 = new UserService(mongodb);
        service2.storeUser("Nisha");
    }
}

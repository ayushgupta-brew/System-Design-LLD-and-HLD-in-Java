package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.Without_DIP;

public class Main {
    static void main() {
        UserService userService = new UserService();

        userService.storeUserToSQL("Ayush");
        userService.storeUserToMongo("Nisha");
    }
}

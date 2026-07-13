package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.Without_DIP;

// Low-level module
public class MySQLDatabase {
    public void saveToSQL(String data) {
        System.out.println(
                "Executing SQL Query: INSERT INTO users VALUES('"
                        + data + "');"
        );
    }
}

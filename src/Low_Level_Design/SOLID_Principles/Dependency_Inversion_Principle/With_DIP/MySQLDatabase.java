package Low_Level_Design.SOLID_Principles.Dependency_Inversion_Principle.With_DIP;

public class MySQLDatabase implements Database{
    @Override
    public void save(String data) {
        System.out.println(
                "Executing SQL Query: INSERT INTO users VALUES('"
                        + data + "');"
        );
    }
}

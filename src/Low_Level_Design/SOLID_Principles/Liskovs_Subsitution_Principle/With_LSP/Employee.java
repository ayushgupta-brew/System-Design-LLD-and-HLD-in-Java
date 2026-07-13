package Low_Level_Design.SOLID_Principles.Liskovs_Subsitution_Principle.With_LSP;

public abstract class Employee implements EmployeeInterface {

    private String name;
    private Integer id;

    public Employee(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

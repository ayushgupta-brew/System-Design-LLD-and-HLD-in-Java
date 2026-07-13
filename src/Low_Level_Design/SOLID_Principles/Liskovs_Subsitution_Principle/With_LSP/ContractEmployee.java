package Low_Level_Design.SOLID_Principles.Liskovs_Subsitution_Principle.With_LSP;


public class ContractEmployee  extends Employee {


    public ContractEmployee(Integer id, String name) {
        super(id, name);
    }

    @Override
    public Double getSalary() {
        return 25000d;
    }
}
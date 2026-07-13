package Low_Level_Design.SOLID_Principles.Liskovs_Subsitution_Principle.Without_LSP;

import java.util.List;

public interface EmployeeInterface {

    Double calculateBonus();
    Double getSalary();
    List<Perks> getPerks();
}

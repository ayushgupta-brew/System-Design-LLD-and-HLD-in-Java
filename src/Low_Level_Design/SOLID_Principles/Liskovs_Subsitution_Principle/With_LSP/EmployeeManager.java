package Low_Level_Design.SOLID_Principles.Liskovs_Subsitution_Principle.With_LSP;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    public static void main(String[] args){
        List<Employee> employeeList = new ArrayList<>();
        PermanentEmployee permanentEmployeeAyush = new PermanentEmployee(1, "Ayush");
        TemporaryEmployee temporaryEmployeeNisha= new TemporaryEmployee(2, "Nisha");
        ContractEmployee contractEmployeeRajesh = new ContractEmployee(3, "Rajesh");

        employeeList.add(permanentEmployeeAyush);
        employeeList.add(temporaryEmployeeNisha);
        employeeList.add(contractEmployeeRajesh);

        System.out.println("Total Salary of all employees is " + fetchTotalSalary(employeeList));

        List<EmployeeInterfaceSpecialAllowances> employeeListWithSpecialAllowances = new ArrayList<>();
        employeeListWithSpecialAllowances.add(permanentEmployeeAyush);
        employeeListWithSpecialAllowances.add(temporaryEmployeeNisha);
//      employeeListWithSpecialAllowances.add(contractEmployeeRahul);

        System.out.println("Total bonus budget of all employees is " + fetchTotalBonusBudget(employeeListWithSpecialAllowances));
        printPerksOfEachEmployee(employeeListWithSpecialAllowances);
    }

    private static Double fetchTotalBonusBudget(List<EmployeeInterfaceSpecialAllowances> employeeList) {
        Double totalBonus = 0d;
        for(EmployeeInterfaceSpecialAllowances employee : employeeList){
            totalBonus += employee.calculateBonus();
        }
        return totalBonus;
    }

    private static Double fetchTotalSalary(List<Employee> employeeList) {
        Double totalSalary = 0d;
        for(Employee employee : employeeList){
            totalSalary += employee.getSalary();
        }
        return totalSalary;

    }


    private static void printPerksOfEachEmployee(List<EmployeeInterfaceSpecialAllowances> employeeList) {

        for(EmployeeInterfaceSpecialAllowances employee : employeeList){

            String perkList = new String();
            if(employee.getPerks() !=null) {
                for (Perks perk : employee.getPerks()) {
                    perkList += perk + " ";
                }
                System.out.println("Perks are " + perkList);
            }
        }
    }
}

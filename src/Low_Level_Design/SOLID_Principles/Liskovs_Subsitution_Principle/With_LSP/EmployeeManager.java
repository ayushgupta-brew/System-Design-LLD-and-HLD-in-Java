package Low_Level_Design.SOLID_Principles.Liskovs_Subsitution_Principle.Without_LSP;

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

        System.out.println("Total salary of all employees is " + fetchTotalSalary(employeeList));
        System.out.println("Total bonus budget of all employees is " + fetchTotalBonusBudget(employeeList));
        printPerksOfEachEmployee(employeeList);
    }

    private static Double fetchTotalSalary(List<Employee> employeeList) {
        Double totalSalary = 0d;
        for(Employee employee : employeeList){
            totalSalary += employee.getSalary();
        }
        return totalSalary;
    }

    private static Double fetchTotalBonusBudget(List<Employee> employeeList) {
        Double totalBonus = 0d;
        for(Employee employee : employeeList){
            if(!(employee instanceof ContractEmployee)) {
                totalBonus += employee.calculateBonus();
            }
        }
        return totalBonus;
    }

    private static void printPerksOfEachEmployee(List<Employee> employeeList) {
        for(Employee employee : employeeList){

            String perkList = new String();
            if(employee.getPerks() !=null) {
                for (Perks perk : employee.getPerks()) {
                    perkList += perk + " ";
                }
                System.out.println("Employee id " + employee.getId() + " with name " + employee.getName() + " has perks as " + perkList);
            }
        }
    }
}

package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.Without_ISP;

// Single interface for all shapes (Violates ISP)
public interface Shape {
    double area();
    double volume();
}

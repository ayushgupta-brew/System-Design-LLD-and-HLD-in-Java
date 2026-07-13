package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.With_ISP;

public class Main {
    static void main() {

        TwoDimensionalShape square = new Square(5);
        TwoDimensionalShape rectangle = new Rectangle(4, 6);
        ThreeDimensionalShape cube     = new Cube(3);

        System.out.println("Square Area: "    + square.area());
        System.out.println("Rectangle Area: " + rectangle.area());
        System.out.println("Cube Area: "      + cube.area());
        System.out.println("Cube Volume: "    + cube.volume());
    }
}

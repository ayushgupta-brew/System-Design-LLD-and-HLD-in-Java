package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.Without_ISP;

public class Main {
    static void main() {

        Shape square    = new Square(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape cube      = new Cube(3);

        System.out.println("Square Area: "    + square.area());
        System.out.println("Rectangle Area: " + rectangle.area());
        System.out.println("Cube Area: "      + cube.area());
        System.out.println("Cube Volume: "    + cube.volume());

        try {
            System.out.println("Square Volume: " + square.volume()); // Will throw an exception
        } catch (UnsupportedOperationException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

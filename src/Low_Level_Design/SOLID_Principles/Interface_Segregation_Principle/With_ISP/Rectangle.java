package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.Without_ISP;

// Rectangle is also a 2D shape but is forced to implement volume()
public class Rectangle implements Shape{

    private final double length;
    private final double width;

    public Rectangle(double l, double w) {
        this.length = l;
        this.width  = w;
    }

    @Override
    public double area() {
        return length * width;
    }

    @Override
    public double volume() {
        throw new UnsupportedOperationException("Volume not applicable for Rectangle"); // Unnecessary method
    }
}

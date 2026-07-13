package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.With_ISP;

import Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.Without_ISP.Shape;

// Rectangle is also a 2D shape but is forced to implement volume()
public class Rectangle implements TwoDimensionalShape {

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
}

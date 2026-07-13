package Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.With_ISP;

import Low_Level_Design.SOLID_Principles.Interface_Segregation_Principle.Without_ISP.Shape;

// Square is a 2D shape but is forced to implement volume()
public class Square implements TwoDimensionalShape {

    private final double side;

    public Square(double s) {
        this.side = s;
    }

    @Override
    public double area() {
        return side * side;
    }
}

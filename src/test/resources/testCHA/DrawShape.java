// DrawShape.java
public class DrawShape {
    public static void main(String[] args) {
        Shape circle = new Circle();
        Shape square = new Square();

        drawShape(circle);
        drawShape(square);
    }

    public static void drawShape(Shape shape) {
        shape.draw();
    }
}
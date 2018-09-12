package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.Turtle;
import org.gannacademy.cdf.turtlelogo.docs.SavableTerrarium;

public class Hearts {
    public static void arc(Turtle turtle, double radius, double arcLength) {
        for (int i = 0; i < arcLength; i++) {
            turtle.rt(1);
            turtle.fd(2.0 * Math.PI * radius / 360.0);
        }
    }

    public static void heart(Turtle turtle, double size) {
        double heading = turtle.getHeadingInDegrees();
        double  radius = size / 4.0,
                side = size / 2.0 - (radius - Math.cos(Math.PI / 4.0) * radius),
                diagonal = Math.sqrt(2 * Math.pow(side, 2));
        turtle.lt(45);
        arc(turtle, radius, 225);
        turtle.head(heading);
        arc(turtle, radius, 225);
        turtle.fd(diagonal);
        turtle.rt(90);
        turtle.fd(diagonal);
        turtle.head(heading);
    }

    public static void main(String[] args) {
        SavableTerrarium terrarium = new SavableTerrarium();
        Turtle clarence = new Turtle(terrarium);
        clarence.head(Turtle.NORTH);
        heart(clarence, 100);
        clarence.tp(100, 100);
        heart(clarence, 50);
        clarence.tp(200, 150);
        heart(clarence, 300);
        terrarium.drawTo("hearts.png");
    }
}

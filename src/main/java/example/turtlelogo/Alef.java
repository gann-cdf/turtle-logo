package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.Turtle;
import org.gannacademy.cdf.turtlelogo.docs.SavableTerrarium;

public class Alef {
    public static void square(Turtle t, double size) {
        for (int i = 0; i < 4; i++) {
            t.rt(90);
            t.fd(size);
        }
    }

    public static void main(String[] args) {
        SavableTerrarium terrarium = new SavableTerrarium();
        Turtle alef = new Turtle(terrarium);
        square(alef, 100);
        alef.tp(100, 100);
        square(alef, 20);
        alef.tp(550, 50);
        square(alef, 300);
        terrarium.drawTo("alef.png");
    }
}

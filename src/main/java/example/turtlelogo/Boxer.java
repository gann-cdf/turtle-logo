package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.Turtle;
import org.gannacademy.cdf.turtlelogo.docs.SavableTerrarium;

public class Boxer {
    public static void main(String[] args) {
        SavableTerrarium terrarium = new SavableTerrarium();
        Turtle boxer = new Turtle(terrarium);

        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);

        terrarium.drawTo("square.png");
        terrarium.clear();

        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                boxer.teleport(row * 20, col * 20);
                for (int side = 0; side < 4; side++) {
                    boxer.rt(90);
                    boxer.fd(10);
                }
            }
        }

        terrarium.drawTo("squares.png");
    }
}

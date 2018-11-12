package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.Turtle;

public class Squares {
    public static void main(String[] args) {
        Turtle boxer = new Turtle();

        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);
        boxer.rt(90);
        boxer.fd(10);

        boxer.getTerrarium().drawTo("square.png");
        boxer.getTerrarium().clear();

        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                boxer.teleport(row * 20, col * 20);
                for (int side = 0; side < 4; side++) {
                    boxer.rt(90);
                    boxer.fd(10);
                }
            }
        }

        boxer.getTerrarium().drawTo("squares.png");
    }
}

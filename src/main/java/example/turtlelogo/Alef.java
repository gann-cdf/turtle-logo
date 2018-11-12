package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.Turtle;

public class Alef {
    public static void square(Turtle t, double size) {
        for (int i = 0; i < 4; i++) {
            t.rt(90);
            t.fd(size);
        }
    }

    public static void main(String[] args) {
        Turtle alef = new Turtle();
        square(alef, 100);
        alef.tp(100, 100);
        square(alef, 20);
        alef.tp(550, 50);
        square(alef, 300);
        alef.getTerrarium().drawTo("alef.png");
    }
}

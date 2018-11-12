package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.AnimatedTurtle;
import org.gannacademy.cdf.turtlelogo.Turtle;

public class Boxer implements Runnable {
    public static void main(String[] args) {
        new Boxer();
    }

    private Turtle boxer;

    public Boxer() {
        boxer = new AnimatedTurtle(1);
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                boxer.teleport(col * 20, row * 20);
                for (int side = 0; side < 4; side++) {
                    boxer.rt(90);
                    boxer.fd(10);
                }
            }
        }
    }
}

package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.AnimatedTurtle;

public class AnimatedRelativeTeleport {
    public static void main(String[] args) {
        AnimatedTurtle t = new AnimatedTurtle();

        t.fd(100);
        t.rt(90);
        t.fd(100);
        t.teleport(t.getX() + 100, 100);
        t.fd(100);
    }
}

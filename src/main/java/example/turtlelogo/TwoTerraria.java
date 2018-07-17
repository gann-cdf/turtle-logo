package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.AnimatedTurtle;
import org.gannacademy.cdf.turtlelogo.Terrarium;
import org.gannacademy.cdf.turtlelogo.Turtle;

import java.awt.*;

public class TwoTerraria {
  public static void main(String[] args) {
    Turtle red = new AnimatedTurtle();
    Terrarium other = new Terrarium();
    Turtle blue = new AnimatedTurtle(other);

    red.getTerrarium().setBackground(new Color(255, 220, 220));
    blue.getTerrarium().setBackground(new Color(190, 240, 255));

    red.getTerrarium().setPosition(10, 100);
    blue.getTerrarium().setPosition(red.getTerrarium().getWidth() + 20, 100);

    red.pc(Color.red);
    blue.pc(Color.blue);

    red.fd(100);
    blue.lt(90);
    blue.fd(100);
  }
}

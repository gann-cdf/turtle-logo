package example.turtlelogo;


import gann.turtlelogo.AnimatedTurtle;
import gann.turtlelogo.Turtle;

import java.awt.*;

public class TwoTurtles {
  public static void main(String[] args) {
    Turtle
            michelangeo = new AnimatedTurtle(),
            leonardo = new AnimatedTurtle();

    michelangeo.getTerrarium().setSize(800, 400);

    michelangeo.tp(200, 200);
    michelangeo.pc(Color.orange);
    michelangeo.lt(120);
    michelangeo.fd(100);
    michelangeo.lt(120);
    michelangeo.fd(100);
    michelangeo.lt(120);
    michelangeo.fd(100);

    leonardo.tp(600, 200);
    leonardo.pc(Color.blue);
    leonardo.fd(100);
    leonardo.lt(90);
    leonardo.fd(100);
    leonardo.lt(90);
    leonardo.fd(100);
    leonardo.lt(90);
    leonardo.fd(100);
  }
}

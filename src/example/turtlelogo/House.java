package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.AnimatedTurtle;
import org.gannacademy.cdf.turtlelogo.Turtle;

import java.awt.*;

public class House {
  public static void main(String[] args) {
    Turtle rafael = new AnimatedTurtle();
    int size = 100;
    rafael.pu();
    rafael.bk(size / 2.0);
    rafael.pd();
    rafael.pw(4);
    rafael.pc(Color.red);
    rafael.fd(size);
    rafael.lt(135);
    rafael.fd(Math.sqrt(2) * size);
    rafael.lt(135);
    rafael.fd(size);
    rafael.lt(135);
    rafael.fd(Math.sqrt(2) * size);
    rafael.lt(90);
    rafael.fd(Math.sqrt(2) * size / 2.0);
    rafael.lt(90);
    rafael.fd(Math.sqrt(2) * size / 2.0);
    rafael.lt(135);
    rafael.fd(size);
    rafael.rt(90);
    rafael.fd(size);
  }
}
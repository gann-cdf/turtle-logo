package example.turtlelogo;

import gann.turtlelogo.Turtle;

import java.awt.*;

public class Test {
  public static void main(String[] args) {
    // headed east in center of screen
    Turtle a = new Turtle();
    a.pc(Color.red);

    // blue horizontal line in top-left screen, headed south
    Turtle b = new Turtle();
    b.pc(Color.blue);
    b.tp(10, 10);
    b.hd(Turtle.EAST);
    b.fd(100);
    b.rt(90);

    // green vertical zig zag on left screen, headed SE
    Turtle c = new Turtle();
    c.pc(Color.green);
    c.tp(10, 100);
    c.hd(Turtle.SOUTH);
    c.lt(45);
    c.fd(20);
    c.rt(90);
    c.fd(20);
    c.lt(90);
    c.fd(20);

    // orange circle on right screen, no turtle
    Turtle d = new Turtle();
    d.ht();
    d.pc(Color.orange);
    d.tp(500, 100);
    for (int i = 0; i < 360; i++) {
      d.fd(Math.PI * 2.0 * 25.0 / 360.0);
      d.rt(1);
    }

    // fat purple box in bottom screen, turtle headed north
    Turtle e = new Turtle();
    e.pc(new Color(200, 0, 180));
    e.pw(5);
    e.tp(300, 300);
    e.hd(Turtle.EAST);
    e.fd(40);
    e.rt(90);
    e.fd(40);
    e.rt(90);
    e.fd(40);
    e.rt(90);
    e.fd(40);

  }
}

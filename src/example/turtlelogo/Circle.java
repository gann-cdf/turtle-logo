package example.turtlelogo;

import gann.turtlelogo.Turtle;

import java.awt.*;

public class Circle {
  private static Turtle donatello;

  public static void main(String[] args) {
    donatello = new Turtle();
    donatello.getTerrarium().setSize(500, 500);
    donatello.tp(donatello.getTerrarium().getWidth() / 2, donatello.getTerrarium().getHeight() / 2);
    donatello.pw(10);
    donatello.pc(new Color(200, 0, 200));
    donatello.pu();
    donatello.lt(90);
    donatello.fd(200);
    donatello.rt(90);
    donatello.pd();
    for (int i = 0; i < 360; i++) {
      donatello.fd(Math.PI * 2.0 * 200 / 360.0);
      donatello.rt(1);
    }
    donatello.pu();
    donatello.rt(90);
    donatello.fd(200);
    donatello.lt(90);
    donatello.pd();
  }
}

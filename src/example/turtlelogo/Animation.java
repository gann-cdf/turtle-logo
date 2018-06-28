package example.turtlelogo;

import gann.turtlelogo.AnimatedTurtle;

import java.awt.*;

public class Animation {
  public static void main(String[] args) {
    AnimatedTurtle churchy = new AnimatedTurtle();
    churchy.fd(100);
    churchy.lt(90);
    churchy.pc(Color.red);
    churchy.fd(50);
    churchy.rt(225);
    churchy.pw(5);
    churchy.bk(50);
    churchy.pw(1);
    churchy.lt(45);
    churchy.bk(100);
  }
}

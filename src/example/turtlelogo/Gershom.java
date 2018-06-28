package example.turtlelogo;

import gann.turtlelogo.AnimatedTurtle;
import gann.turtlelogo.Turtle;

import java.awt.*;

public class Gershom {
  public static void main(String[] args) {
    AnimatedTurtle gersh = new AnimatedTurtle();
    gersh.getTerrarium().setSize(1000, 800);
    gersh.tp(500, 400);
    gersh.pc(Color.green);
    for (int i = 0; i < 100; i++) {
      gersh.rt(15);
      gersh.fd(i);
    }
    gersh.rt(90);
    gersh.fd(500);
    gersh.pc(new Color(255, 190, 100));
    gersh.pw(10);
    gersh.rt(115);
    gersh.fd(40);
    gersh.bk(60);
    gersh.fd(25);
    gersh.rt(90);
    gersh.fd(25);
    gersh.rt(90);
    gersh.fd(25);
    gersh.sp(25);
    gersh.tp(425, 485);
    gersh.hd(300);
  }
}

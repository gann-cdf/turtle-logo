package example.turtlelogo;

import org.gannacademy.cdf.turtlelogo.AnimatedTurtle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardControl implements KeyListener {
  private AnimatedTurtle scooter;

  public KeyboardControl() {
    scooter = new AnimatedTurtle(1);
    scooter.getTerrarium().addKeyListener(this);
    scooter.getTerrarium().requestFocus();
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_A:
        scooter.left(15);
        break;
      case KeyEvent.VK_D:
        scooter.right(15);
        break;
      case KeyEvent.VK_W:
        scooter.forward(10);
        break;
      case KeyEvent.VK_S:
        scooter.back(10);
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  public static void main(String[] args) {
    new KeyboardControl();
  }
}

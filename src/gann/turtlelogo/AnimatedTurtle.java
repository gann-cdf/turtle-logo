package gann.turtlelogo;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

public class AnimatedTurtle extends Turtle implements Runnable {

  public static final long
          DEFAULT_FRAME_DELAY = 10, // milliseconds
          TURN_SPEED_CUTOFF = 25; // milliseconds

  private enum Verb {
    MOVE, TURN, HEAD,
    PEN_UP, PEN_DOWN, PEN_COLOR, PEN_WIDTH,
    HIDE, SHOW,
    MOVE_TO, TELEPORT, HOME,
    SPEED
  }

  private class Instruction {
    private Verb verb;
    private Object parameter;

    public Instruction(Verb verb) {
      this.verb = verb;
    }

    public Instruction(Verb verb, double value) {
      this.verb = verb;
      this.parameter = value;
    }

    public Instruction(Verb verb, long value) {
      this.verb = verb;
      this.parameter = value;
    }

    public Instruction(Verb verb, double x, double y) {
      this.verb = verb;
      this.parameter = new Point2D.Double(x, y);
    }

    public Instruction(Verb verb, Color color) {
      this.verb = verb;
      this.parameter = color;
    }

    public Verb getVerb() {
      return verb;
    }

    public double getDoubleParam() {
      return (double) parameter;
    }

    public long getLongParam() {
      return (long) parameter;
    }

    public Point2D getPointParam() {
      return (Point2D) parameter;
    }

    public Color getColorParam() {
      return (Color) parameter;
    }

    public void convertTo(Verb verb) {
      this.verb = verb;
    }
  }

  private Queue<Instruction> instructions;
  private Instruction active;
  private double steps, targetSteps, degrees, targetDegrees, tempHeadingInRadians;
  private long frameDelay, tick;

  public AnimatedTurtle() {
    this(DEFAULT_FRAME_DELAY);
  }

  public AnimatedTurtle(long frameDelay) {
    super();
    this.frameDelay = frameDelay;
    tick = System.currentTimeMillis();
    instructions = new LinkedList<>();
    new Thread(this).start();
  }

  public void move(double steps) {
    instructions.add(new Instruction(Verb.MOVE, steps));
  }

  public void turn(double angle) {
    instructions.add(new Instruction(Verb.TURN, angle));
  }

  public void head(double heading) {
    instructions.add(new Instruction(Verb.HEAD, heading));
  }

  public void penUp() {
    instructions.add(new Instruction(Verb.PEN_UP));
  }

  public void penDown() {
    instructions.add(new Instruction(Verb.PEN_DOWN));
  }

  public void penColor(Color color) {
    instructions.add(new Instruction(Verb.PEN_COLOR, color));
  }

  public void penWidth(double width) {
    instructions.add(new Instruction(Verb.PEN_WIDTH, width));
  }

  public void hide() {
    instructions.add(new Instruction(Verb.HIDE));
  }

  public void show() {
    instructions.add(new Instruction(Verb.SHOW));
  }

  public void teleport(double x, double y) {
    instructions.add(new Instruction(Verb.TELEPORT, x, y));
  }

  public void moveTo(double x, double y) {
    instructions.add(new Instruction(Verb.MOVE_TO, x, y));
  }

  public void home() {
    instructions.add(new Instruction(Verb.HOME));
  }

  public void sp(long delay) {
    speed(delay);
  }

  public void speed(long delay) {
    instructions.add(new Instruction(Verb.SPEED, delay));
  }

  @Override
  public void run() {
    while (true) {
      if (active == null) {
        if (!instructions.isEmpty()) {
          active = instructions.remove();
          switch (active.getVerb()) {
            case MOVE:
              targetSteps = active.getDoubleParam();
              steps = 0;
              break;
            case MOVE_TO:
              double dx = active.getPointParam().getX() - x,
                      dy = active.getPointParam().getY() - y;
              targetSteps = Math.hypot(dx , dy);
              steps = 0;
              tempHeadingInRadians = Math.atan2(dy, dx);
              break;
            case TURN:
              targetDegrees = active.getDoubleParam();
              degrees = 0;
              break;
            case HEAD:
              active.convertTo(Verb.TURN);
              setShortestTurnAngle();
              degrees = 0;
              break;
            case PEN_UP:
              super.penUp();
              active = null;
              break;
            case PEN_DOWN:
              super.penDown();
              active = null;
              break;
            case PEN_COLOR:
              super.penColor(active.getColorParam());
              active = null;
              break;
            case PEN_WIDTH:
              super.penWidth(active.getDoubleParam());
              active = null;
              break;
            case HIDE:
              super.hide();
              active = null;
              break;
            case SHOW:
              super.show();
              active = null;
              break;
            case TELEPORT:
              super.teleport(active.getPointParam().getX(), active.getPointParam().getY());
              active = null;
              break;
            case HOME:
              super.home();
              active = null;
              break;
            case SPEED:
              frameDelay = active.getLongParam();
              active = null;
              break;
          }
        }
      } else if (System.currentTimeMillis() > tick + frameDelay) {
        tick = System.currentTimeMillis();
        switch (active.getVerb()) {
          case MOVE:
          case MOVE_TO:
            if (Math.abs(steps) >= Math.abs(targetSteps)) {
              if (active.getVerb() == Verb.MOVE) {
                super.move(targetSteps);
              } else {
                super.moveTo(active.getPointParam().getX(), active.getPointParam().getY());
              }
              active = null;
            } else {
              steps += (targetSteps > 0.0 ? 1 : -1);
            }
            break;
          case TURN:
            if (Math.abs(degrees) >= Math.abs(targetDegrees)) {
              super.turn(targetDegrees);
              active = null;
            } else {
              degrees += (targetDegrees > 0.0 ? 1 : -1) * (frameDelay >= TURN_SPEED_CUTOFF ? 1 : TURN_SPEED_CUTOFF - frameDelay);
            }
            break;
        }
        getTerrarium().repaint();

      }
    }
  }

  private void setShortestTurnAngle() {
    if (Math.abs(headingInDegrees - active.getDoubleParam()) > 180.0) {
      targetDegrees = (360 - Math.abs(headingInDegrees - active.getDoubleParam())) * (headingInDegrees > active.getDoubleParam() ? 1 : -1);
    } else {
      targetDegrees = headingInDegrees - active.getDoubleParam();
    }
  }

  public void draw(Graphics2D graphics) {
    if (active == null) {
      super.draw(graphics);
    } else {
      graphics.setPaint(penColor);
      graphics.setStroke(penStroke);
      switch (active.getVerb()) {
        case MOVE:
        case MOVE_TO:
          double moveHeadingInRadians = tempHeadingInRadians;
          if (active.getVerb() == Verb.MOVE) {
            moveHeadingInRadians = headingInRadians();
          }
          double tempX = x + Math.cos(moveHeadingInRadians) * steps;
          double tempY = y + Math.sin(moveHeadingInRadians) * steps;
          if (penDown) {
            graphics.draw(new Line2D.Double(x, y, tempX, tempY));
          }
          if (!hidden) {
            drawIcon(tempX, tempY, headingInRadians(), graphics);
          }
          break;
        case TURN:
          if (!hidden) {
            drawIcon(x, y, Math.toRadians(headingInDegrees + degrees), graphics);
          }
          break;
      }
    }
  }
}

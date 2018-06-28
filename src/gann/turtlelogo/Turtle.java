package gann.turtlelogo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Turtle {

  public static final int
          DEFAULT_CANVAS_WIDTH = 600, // pixels
          DEFAULT_CANVAS_HEIGHT = 400; // pixels
  public static final double DEFAULT_HEADING_IN_DEGREES = 0.0; // degrees
  public static final Color DEFAULT_COLOR = Color.black;
  public static final Stroke DEFAULT_STROKE = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  public static final double
          NORTH = 270,
          SOUTH = 90,
          EAST = 0,
          WEST = 180;

  private static Terrarium terrarium;
  private static BufferedImage icon;

  protected double x, y;
  protected double headingInDegrees;
  protected Color penColor;
  protected Stroke penStroke;
  protected boolean penDown;
  protected boolean hidden;

  public Turtle() {
    this(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT);
  }

  public Turtle(int width, int height) {
    this.x = width / 2.0;
    this.y = height / 2.0;
    this.headingInDegrees = DEFAULT_HEADING_IN_DEGREES;
    this.penColor = DEFAULT_COLOR;
    this.penStroke = DEFAULT_STROKE;
    this.penDown = true;
    this.hidden = false;

    initializeSingletonIcon();
    initializeSingletonTerrarium();
    terrarium.addTurtle(this);
  }

  private void initializeSingletonIcon() {
    if (icon == null) {
      try {
        icon = ImageIO.read(getClass().getResource("/turtlelogo/turtle.png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void initializeSingletonTerrarium() {
    if (terrarium == null) {
      terrarium = new Terrarium();
    }
  }

  protected double headingInRadians() {
    return Math.toRadians(headingInDegrees);
  }

  public void bk(double steps) {
    back(steps);
  }

  public void back(double steps) {
    move(-1 * steps);
  }

  public void fd(double steps) {
    forward(steps);
  }

  public void forward(double steps) {
    move(steps);
  }

  public void move(double steps) {
    double newX = x + Math.cos(headingInRadians()) * steps,
            newY = y + Math.sin(headingInRadians()) * steps;
    if (penDown) {
      terrarium.addTrack(new Track(x, y, newX, newY, penColor, penStroke));
    }
    x = newX;
    y = newY;
  }

  public void to(double x, double y) {
    moveTo(x, y);
  }

  public void moveTo(double x, double y) {
    if (penDown) {
      terrarium.addTrack(new Track(this.x, this.y, x, y, penColor, penStroke));
    }
    this.x = x;
    this.y = y;
  }

  public void tp(double x, double y) {
    teleport(x, y);
  }

  public void teleport(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void home() {
    teleport(getTerrarium().getWidth() / 2.0, getTerrarium().getHeight() / 2.0);
    head(EAST);
  }

  public void rt(double angle) {
    right(angle);
  }

  public void right(double angle) {
    turn(angle);
  }

  public void lt(double angle) {
    left(angle);
  }

  public void left(double angle) {
    turn(-1 * angle);
  }

  public void turn(double angle) {
    headingInDegrees = (headingInDegrees + angle) % 360;
  }

  public void hd(double heading) {
    head(heading);
  }
  public void head(double heading) {
    this.headingInDegrees = heading % 360;
  }

  public void pu() {
    penUp();
  }

  public void penUp() {
    penDown = false;
  }

  public void pd() {
    penDown();
  }

  public void penDown() {
    penDown = true;
  }

  public void pc(Color color) {
    penColor(color);
  }
  public void penColor(Color color) {
    penColor = color;
  }

  public void pw(double width) {
    penWidth(width);
  }

  public void penWidth(double width) {
    penStroke = new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }

  public void ht() {
    hide();
  }

  public void hide() {
    hidden = true;
  }

  public void st() {
    show();
  }

  public void show() {
    hidden = false;
  }

  public Terrarium getTerrarium() {
    initializeSingletonTerrarium();
    return terrarium;
  }

  public void draw(Graphics2D graphics) {
    drawIcon(x, y, headingInRadians(), graphics);
  }

  protected void drawIcon(double x, double y, double headingInRadians, Graphics2D graphics) {
    if (!hidden) {
      AffineTransform transform = new AffineTransform(); // transformations are applied in reverse order
      transform.translate(x, y); // move turtle to location
      transform.rotate(headingInRadians); // orient turtle to heading
      transform.translate(-1 * icon.getWidth(), icon.getHeight() / -2.0); // move icon origin to turtle nose
      graphics.drawImage(icon, transform, null);
    }
  }
}

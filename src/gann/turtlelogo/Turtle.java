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
  public static final BasicStroke DEFAULT_STROKE = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  public static final boolean DEFAULT_PEN_DOWN = true, DEFAULT_HIDDEN = false;
  public static final double
          NORTH = 270,
          SOUTH = 90,
          EAST = 0,
          WEST = 180;

  private Terrarium terrarium;
  private static BufferedImage icon;

  private double x, y;
  private double headingInDegrees;
  private Color penColor;
  private BasicStroke penStroke;
  private boolean penDown;
  private boolean hidden;

  public Turtle() {
    this(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT);
  }

  public Turtle(int width, int height) {
    this.x = width / 2.0;
    this.y = height / 2.0;
    this.headingInDegrees = DEFAULT_HEADING_IN_DEGREES;
    this.penColor = DEFAULT_COLOR;
    this.penStroke = DEFAULT_STROKE;
    this.penDown = DEFAULT_PEN_DOWN;
    this.hidden = DEFAULT_HIDDEN;
    getTerrarium();
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getHeadingInDegrees() {
    return headingInDegrees;
  }

  public double getHeadingInRadians() {
    return Math.toRadians(headingInDegrees);
  }

  public Color getPenColor() {
    return penColor;
  }

  public double getPenWidth() {
    return penStroke.getLineWidth();
  }

  protected BasicStroke getPenStroke() {
    return penStroke;
  }

  public boolean isPenDown() {
    return penDown;
  }

  public boolean isHidden() {
    return hidden;
  }

  private BufferedImage getIcon() {
    if (icon == null) {
      try {
        icon = ImageIO.read(getClass().getResource("/turtlelogo/turtle.png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return icon;
  }

  public Terrarium getTerrarium() {
    if (terrarium == null) {
      terrarium = Terrarium.getInstance();
      terrarium.add(this);
    }
    return terrarium;
  }

  public void setTerrarium(Terrarium terrarium) {
    if (this.terrarium != null) {
      this.terrarium.remove(this);
    }
    this.terrarium = terrarium;
    terrarium.add(this);
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
    double newX = x + Math.cos(getHeadingInRadians()) * steps,
            newY = y + Math.sin(getHeadingInRadians()) * steps;
    if (penDown) {
      getTerrarium().add(new Track(x, y, newX, newY, penColor, penStroke));
    }
    x = newX;
    y = newY;
  }

  public void to(double x, double y) {
    moveTo(x, y);
  }

  public void moveTo(double x, double y) {
    if (penDown) {
      getTerrarium().add(new Track(this.x, this.y, x, y, penColor, penStroke));
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

  public void draw(Graphics2D graphics) {
    drawIcon(x, y, getHeadingInRadians(), graphics);
  }

  protected void drawIcon(double x, double y, double headingInRadians, Graphics2D graphics) {
    if (!hidden) {
      AffineTransform transform = new AffineTransform(); // transformations are applied in reverse order
      transform.translate(x, y); // move turtle to location
      transform.rotate(headingInRadians); // orient turtle to heading
      transform.translate(-1 * getIcon().getWidth(), getIcon().getHeight() / -2.0); // move icon origin to turtle nose
      graphics.drawImage(getIcon(), transform, null);
    }
  }
}

package org.gannacademy.cdf.turtlelogo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>A turtles lives in a {@link Terrarium}. We imagine that turtles all hold a pen in their mouth. As they walk around
 * the terrarium, they leave a trail (a series of {@link Track} segments) behind them. The turtles can avoid leaving
 * a track if they pick up their pen.</p>
 *
 * <p style="text-align: center"><img src="doc-files/trail.png" alt="Turtle leaving a trail"></p>
 *
 * <p>Turtles understand a limited number of instructions:</p>
 *
 * <table style="margin-left: 4em;">
 * <tr>
 * <td><img src="doc-files/move.png" alt="move() example"></td>
 * <td><b>move(</b>
 * <i>steps</i><b>)</b> (a.k.a <i>forward</i> or <i>fd</i> and <i>back</i> or <i>bk</i>) &mdash; the turtle will step
 * forward, in the direction that it is currently facing, some number of steps (i.e. pixels).</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/turn.png" alt="turn() example"></td>
 * <td><b>turn(</b><i>degrees</i><b>)</b>
 * (a.k.a. <i>left</i> or <i>lt</i> and <i>right</i> or <i>rt</i>) &mdash; the turtle will turn from its current heading
 * some number of degrees.</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/moveTo.png" alt="moveTo() example"></td>
 * <td><b>moveTo(</b><i>x</i>, <i>y</i><b>)</b> (a.k.a. <i>to</i>) &mdash; the turtle will move from its current
 * location to the coordinates given, without changing its heading.</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/teleport.png" alt="teleport() example"></td>
 * <td><b>teleport(</b><i>x</i>, <i>y</i><b>)</b> (a.k.a. <i>tp</i>) &mdash; the turtle will teleport (like in Star
 * Trek) from its current location to the new coordinates, without changing its heading <i>and</i> without allowing the
 * pen to drag between locations.</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/penColor.png" alt="penColor() example"></td>
 * <td><b>penColor(</b><i>color</i><b>)</b> (a.k.a. <i>pc</i>) &mdash; the turtle will change the color of its pen
 * (initially the pen is black)</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/penWidth.png" alt="penWidth() example"></td>
 * <td><b>penWidth(</b><i>width</i><b>)</b> (a.k.a. <i>pw</i>) &mdash; the turtle will change the width of its pen
 * stroke (measured in pixels)</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/hide.png" alt="hide() example"></td>
 * <td><b>hide()</b> (a.k.a. <i>ht</i>) &mdash; the turtle will hide itself (but remain at its current location and
 * heading)</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/show.png" alt="show() example"></td>
 * <td><b>show()</b> (a.k.a. <i>st</i>) &mdash; the turtle, if hidden, will show itself again</td>
 * </tr>
 * <caption>&nbsp;</caption>
 * </table>
 */
public class Turtle {
  /**
   * The parts of the turtle that are "under the shell" are not meant to be used by students. This mechanism
   * (inspired by <a href="https://stackoverflow.com/a/18634125">this awesome Stack Overflow answer</a>) recreates a
   * version of the C++ <code>friend</code> concept: a public method that is only available to <i>some</i> other
   * objects, rather than <i>all</i> other objects.
   *
   * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
   */
  public static final class UnderTheShell {
    private UnderTheShell() {
    }
  }

  protected static final UnderTheShell UNDER_THE_SHELL = new UnderTheShell();

  /**
   * 270&deg;
   */
  public static final double NORTH = 270;

  /**
   * 90&deg;
   */
  public static final double SOUTH = 90;

  /**
   * 0&deg;
   */
  public static final double EAST = 0;

  /**
   * 180&deg;
   */
  public static final double WEST = 180;

  /**
   * {@link #EAST}
   */
  public static final double DEFAULT_HEADING_IN_DEGREES = EAST; // degrees

  /**
   * {@link java.awt.Color#BLACK}
   */
  public static final Color DEFAULT_PEN_COLOR = Color.BLACK;

  /**
   * 1.0 pixels
   */
  public static final float DEFAULT_PEN_WIDTH = 1;

  /**
   * <code>true</code>
   */
  public static final boolean DEFAULT_PEN_DOWN = true;

  /**
   * <code>false</code>
   */
  public static final boolean DEFAULT_HIDDEN = false;


  private Terrarium terrarium;
  private static BufferedImage icon;

  private double x, y;
  private double headingInDegrees;
  private Color penColor;
  private BasicStroke penStroke;
  private boolean penDown;
  private boolean hidden;

  /**
   * Construct a turtle in the default terrarium
   */
  public Turtle() {
    this(Terrarium.getInstance());
  }

  /**
   * Construct a turtle in a custom terrarium
   *
   * @param terrarium to house the turtle
   */
  public Turtle(Terrarium terrarium) {
    this.x = terrarium.getWidth() / 2.0;
    this.y = terrarium.getHeight() / 2.0;
    this.headingInDegrees = DEFAULT_HEADING_IN_DEGREES;
    this.penColor = DEFAULT_PEN_COLOR;
    this.penStroke = new BasicStroke(DEFAULT_PEN_WIDTH);
    this.penDown = DEFAULT_PEN_DOWN;
    this.hidden = DEFAULT_HIDDEN;
    this.terrarium = terrarium;
    this.terrarium.add(this, UNDER_THE_SHELL);
  }

  /**
   * @return X-coordinate of turtle
   */
  public double getX() {
    return x;
  }

  /**
   * @return Y-coordinate of turtle
   */
  public double getY() {
    return y;
  }

  /**
   * @return Current turtle heading in degrees
   */
  public double getHeadingInDegrees() {
    return headingInDegrees;
  }

  /**
   * @return Current turtle heading in radians
   */
  public double getHeadingInRadians() {
    return Math.toRadians(headingInDegrees);
  }

  /**
   * @return Current pen color
   */
  public Color getPenColor() {
    return penColor;
  }

  /**
   * @return Current pen width
   */
  public double getPenWidth() {
    return penStroke.getLineWidth();
  }

  protected BasicStroke getPenStroke() {
    return penStroke;
  }

  /**
   * @return <code>true</code> if the pen is down, <code>false</code> otherwise
   */
  public boolean isPenDown() {
    return penDown;
  }

  /**
   * @return <code>true</code> if the turtle is hidden, <code>false</code> otherwise
   */
  public boolean isHidden() {
    return hidden;
  }

  private BufferedImage getIcon() {
    if (icon == null) {
      try {
        icon = ImageIO.read(getClass().getResource("/turtlelogo/turtle.png"));
      } catch (IOException e) {
        System.err.println("The image file containing the turtle icon could not be found and/or opened.");
        e.printStackTrace();
      }
    }
    return icon;
  }

  /**
   * @return Terrarium currently housing the turtle
   */
  public Terrarium getTerrarium() {
    return terrarium;
  }

  /**
   * Move the turtle to another terrarium
   *
   * @param terrarium to house the turtle
   */
  public void setTerrarium(Terrarium terrarium) {
    if (this.terrarium != null) {
      this.terrarium.remove(this, UNDER_THE_SHELL);
    }
    this.terrarium = terrarium;
    terrarium.add(this, UNDER_THE_SHELL);
  }

  /**
   * Alias for {@link #back(double)}
   *
   * @param steps in pixels
   */
  public void bk(double steps) {
    back(steps);
  }

  /**
   * Alias for {@link #move(double)}
   *
   * @param steps in pixels
   */
  public void back(double steps) {
    move(-1 * steps);
  }

  /**
   * Alias for {@link #forward(double)}
   *
   * @param steps in pixels
   */
  public void fd(double steps) {
    forward(steps);
  }

  /**
   * Alias for {@link #move(double)}
   *
   * @param steps in pixels
   */
  public void forward(double steps) {
    move(steps);
  }

  /**
   * <p>Move the turtle in the direction of its current heading</p>
   * <p>A positive value for <code>steps</code> is interpreted as forward movement and a negative value as backward
   * movement</p>
   *
   * @param steps in pixels
   */
  public void move(double steps) {
    double newX = x + Math.cos(getHeadingInRadians()) * steps,
            newY = y + Math.sin(getHeadingInRadians()) * steps;
    if (penDown) {
      getTerrarium().add(new Track(x, y, newX, newY, penColor, penStroke, UNDER_THE_SHELL), UNDER_THE_SHELL);
    }
    x = newX;
    y = newY;
  }

  /**
   * Alias for {@link #moveTo(double, double)}
   *
   * @param x coordinate
   * @param y coordinate
   */
  public void to(double x, double y) {
    moveTo(x, y);
  }

  /**
   * <p>Move the turtle to a particular location</p>
   * <p>The turtle moves directly to the window coordinates (<code>x</code>, <code>y</code>). Note that the origin of
   * the wind ow is in the top, left corner and that, while the X-axis increases from left to right, the Y-axis
   * increases <i>from to to bottom</i>.</p>
   * <p>If the turtle's pen is currently down, the move will create a track from the old location to the new location.</p>
   *
   * @param x coordinate
   * @param y coordinate
   */
  public void moveTo(double x, double y) {
    if (penDown) {
      getTerrarium().add(new Track(this.x, this.y, x, y, penColor, penStroke, UNDER_THE_SHELL), UNDER_THE_SHELL);
    }
    this.x = x;
    this.y = y;
  }

  /**
   * Alias for {@link #teleport(double, double)}
   *
   * @param x coordinate
   * @param y coordinate
   */
  public void tp(double x, double y) {
    teleport(x, y);
  }

  /**
   * <p>Move the turtle instantaneously to a particular location</p>
   * <p>The turtle moves directly to the window coordinates (<code>x</code>, <code>y</code>). Note that the origin of
   * the wind ow is in the top, left corner and that, while the X-axis increases from left to right, the Y-axis
   * increases <i>from to to bottom</i>.</p>
   * <p>No track is left by a teleportation.</p>
   *
   * @param x coordinate
   * @param y coordinate
   */
  public void teleport(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * <p>Reset the turtle to its home position</p>
   * <p>The turtle's home position is at the center of the window, heading {@link #EAST}</p>
   */
  public void home() {
    teleport(getTerrarium().getWidth() / 2.0, getTerrarium().getHeight() / 2.0);
    head(EAST);
  }

  /**
   * Alias for {@link #right(double)}
   *
   * @param angle in degrees
   */
  public void rt(double angle) {
    right(angle);
  }

  /**
   * Alias for {@link #turn(double)}
   *
   * @param angle in degrees
   */
  public void right(double angle) {
    turn(angle);
  }

  /**
   * Alias for {@link #left(double)}
   *
   * @param angle in degrees
   */
  public void lt(double angle) {
    left(angle);
  }

  /**
   * Alias for {@link #turn(double)}
   *
   * @param angle in degrees
   */
  public void left(double angle) {
    turn(-1 * angle);
  }

  /**
   * <p>Turn the turtle from its current heading</p>
   * <p>A positive angle is interpreted as a <i>right</i> turn and a negative angle is a left turn. This is mildly
   * surprising if you know about the unit circle, but is because the Y-axis of the window increases from top to bottom,
   * which results in a mirrored unit circle around the X-axis, with 90&deg; at the {@link #SOUTH} and270&deg; at the
   * {@link #NORTH}</p>
   *
   * @param angle in degrees
   */
  public void turn(double angle) {
    headingInDegrees = (headingInDegrees + angle) % 360;
    getTerrarium().repaint();
  }

  /**
   * Alias for {@link #head(double)}
   *
   * @param heading in degrees
   */
  public void hd(double heading) {
    head(heading);
  }

  /**
   * <p>Turn the turtle to a particular heading</p>
   * <p>Note that, because the Y-axis of the window increases from top to bottom, the usual angles of the unit circle
   * have been mirrored around the X-axis, with 90&deg; at the {@link #SOUTH} and 270%deg; at the {@link #NORTH}.
   * Convenience constants have been provided for the cardinal directions.</p>
   *
   * @param heading [0..360) in degrees
   */
  public void head(double heading) {
    this.headingInDegrees = heading % 360;
  }

  /**
   * Alis for {@link #penUp()}
   */
  public void pu() {
    penUp();
  }

  /**
   * Lift the turtle's pen up, causing it not to leave a track
   */
  public void penUp() {
    penDown = false;
  }

  /**
   * Alias for {@link #penDown()}
   */
  public void pd() {
    penDown();
  }

  /**
   * Lower the turtle's pen, causing it to leave a trail
   */
  public void penDown() {
    penDown = true;
  }

  /**
   * Alias for {@link #penColor(Color)}
   *
   * @param color to use
   */
  public void pc(Color color) {
    penColor(color);
  }

  /**
   * <p>Set the color of the turtle's pen</p>
   * <p>Color values are given as {@link Color} values. {@link Color} has a number of helpful constants like
   * {@link Color#GREEN} or {@link Color#GREEN}. Custom colors can also be constructed. Refer to the
   * <a href="https://docs.oracle.com/javase/10/docs/api/java/awt/Color.html" target="_blank">j<code>ava.awt.Color</code>
   * API documentation</a> more details (including how to create transparent colors!)</p>
   *
   * @param color to use
   */
  public void penColor(Color color) {
    penColor = color;
  }

  /**
   * Alias for {@link #penWidth(double)}
   *
   * @param width in pixels
   */
  public void pw(double width) {
    penWidth(width);
  }

  /**
   * Set the width of the turtle's pen
   *
   * @param width in pixels
   */
  public void penWidth(double width) {
    penStroke = new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }

  /**
   * Alias for {@link #hide()}
   */
  public void ht() {
    hide();
  }

  /**
   * <p>Hide the turtle</p>
   * <p>Hiding the turtle causes it to become invisible &mdash; it can still be moved and create tracks, but the turtle
   * itself is not visible</p>
   */
  public void hide() {
    hidden = true;
    getTerrarium().repaint();
  }

  /**
   * Alias for {@link #show()}
   */
  public void st() {
    show();
  }

  /**
   * Show the turtle (if it was hidden)
   */
  public void show() {
    hidden = false;
    getTerrarium().repaint();
  }

  /**
   * <p>Draw the turtle</p>
   * <p>May only be called by {@link Terrarium} and its subclasses, enforced by {@link Terrarium.UnderTheSurface}</p>
   *
   * @param context for drawing commands
   * @param key     to authenticate "Terrarium-iality"
   */
  public void draw(Graphics2D context, Terrarium.UnderTheSurface key) {
    key.hashCode();
    drawIcon(x, y, getHeadingInRadians(), context);
  }

  protected void drawIcon(double x, double y, double headingInRadians, Graphics2D context) {
    if (!hidden) {
      AffineTransform transform = new AffineTransform(); // transformations are applied in reverse order
      transform.translate(x, y); // move turtle to location
      transform.rotate(headingInRadians); // orient turtle to heading
      transform.translate(-1 * getIcon().getWidth(), getIcon().getHeight() / -2.0); // move icon origin to turtle nose
      context.drawImage(getIcon(), transform, null);
    }
  }
}

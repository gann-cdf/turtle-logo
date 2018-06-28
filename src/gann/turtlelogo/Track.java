package gann.turtlelogo;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * A turtle track that is visible onscreen.
 * This is really just a line segment that connects two turtle positions, encapsulating the pen color and width at the
 * time the turtle moved from point A to point B.
 *
 * @author Seth Battis
 */
public class Track {
  /*
   * TODO In truth, it might be more intellectually elegant to replace the Terrarium's list of tracks with a single
   * Path2D that would track a turtle's movements.
   */

  /**
   * Visual representation of the turtle track
   */
  private Line2D.Double segment;

  /**
   * Stroke style associated with this track
   */
  private Stroke stroke;

  /**
   * Paint color associated with this track
   */
  private Color color;

  /**
   * Construct a new turtle track
   *
   * @param x1     X-coordinate of starting point
   * @param y1     Y-coordinate of starting point
   * @param x2     X-coordinate of ending point
   * @param y2     Y-coordinate of ending point
   * @param color  Turtle's pen color at the time it left the track
   * @param stroke Turtle's pen stroke (width) at the time it left the track
   */
  public Track(double x1, double y1, double x2, double y2, Color color, Stroke stroke, Turtle.UnderTheShell key) {
    key.hashCode();
    segment = new Line2D.Double(x1, y1, x2, y2);
    this.color = color;
    this.stroke = stroke;
  }

  public void draw(Graphics2D context, Terrarium.UnderTheSurface key) {
    key.hashCode();
    context.setPaint(color);
    context.setStroke(stroke);
    context.draw(segment);
  }
}

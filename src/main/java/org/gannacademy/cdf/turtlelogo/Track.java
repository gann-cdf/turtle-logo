package org.gannacademy.cdf.turtlelogo;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Turtles make visible tracks in terraria (by dragging their pens, of course)
 *
 * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
 */
public class Track {
  private final Line2D.Double segment;
  private final Stroke stroke;
  private final Color color;

  /**
   * <p>Construct a new track segment</p>
   *
   * <p>May only be called by {@link Turtle} and its subclasses, enforced via {@link Turtle.UnderTheShell}.</p>
   *
   * @param x1     coordinate of start
   * @param y1     coordinate of start
   * @param x2     coordinate of end
   * @param y2     coordinate of end
   * @param color  of track
   * @param stroke style of track
   * @param key    to authenticate "Turtleness"
   */
  public Track(double x1, double y1, double x2, double y2, Color color, Stroke stroke, Turtle.UnderTheShell key) {
    assert key != null;
    segment = new Line2D.Double(x1, y1, x2, y2);
    this.color = color;
    this.stroke = stroke;
  }

  /**
   * <p>Draw the track in the terrarium</p>
   *
   * <p>May only be called by {@link Terrarium} and its subclasses, enforced via {@link Terrarium.UnderTheSurface}.</p>
   *
   * @param context for drawing commands
   * @param key     to authenticate "Terrarium-iality"
   */
  public void draw(Graphics2D context, Terrarium.UnderTheSurface key) {
    assert key != null;
    context.setPaint(color);
    context.setStroke(stroke);
    context.draw(segment);
  }
}

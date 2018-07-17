package org.gannacademy.cdf.turtlelogo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A {@link Turtle} lives (and draws) inside a <code>Terrarium</code>.</p>
 *
 * <p><img src="doc-files/trail.png" alt="Turtle leaving a trail"></p>
 *
 * <p>A terrarium can be resized using the {@link #setSize(int, int)} method and repositioned on the screen using the
 * {@link #setPosition(int, int)}. The terrarium can also have a custom background color (set via the
 * {@link #setBackground(Color)} method &mdash; it defaults to white.</p>
 *
 * <p><img src="doc-files/setBackground.png" alt="setBackground() example"></p>
 *
 * <p>Initially, there is only a single terrarium, into which all new turtles are added. However, it is possible to
 * instantiate additional terraria, and to direct Turtles to them using the {@link Turtle#setTerrarium(Terrarium)}
 * method. From a technical standpoint, the initial terrarium is a quasi-singleton, and will continue to be treated as
 * a singleton by any new turtles as they are instantiated. The singleton terrarium instance can be accessed statically
 * via the {@link #getInstance()} method. When additional terraria have been instantiated, they may also be accessed
 * statically via their index (in instantiation order) using the {@link #getInstance(int)} method.</p>
 *
 * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
 */
public class Terrarium extends JPanel {
  /**
   * The parts of the terrarium that are "under the surface" are not meant to be used by students. This mechanism
   * (inspired by <a href="https://stackoverflow.com/a/18634125">this awesome Stack Overflow answer</a>) recreates a
   * version of the C++ <code>friend</code> concept: a public method that is only available to <i>some</i> other
   * objects, rather than <i>all</i> other objects.
   *
   * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
   */
  public static final class UnderTheSurface {
    private UnderTheSurface() {
    }
  }

  protected static final UnderTheSurface UNDER_THE_SURFACE = new UnderTheSurface();

  /**
   * 600 pixels
   */
  public static final int DEFAULT_WIDTH = 600;

  /**
   * 400 pixels
   */
  public static final int DEFAULT_HEIGHT = 400;

  /**
   * {@link Color#WHITE}
   */
  public static final Color DEFAULT_BACKGROUND = Color.WHITE;

  private static List<Terrarium> terraria;
  private JFrame frame;
  private List<Turtle> turtles;
  private List<Track> tracks;

  /**
   * Construct a new terrarium of default dimensions, centered on the screen in its own window
   */
  public Terrarium() {
    super();
    setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    setBackground(DEFAULT_BACKGROUND);
    turtles = new ArrayList<>();
    tracks = new ArrayList<>();
    getFrame();
    addInstance(this);
  }

  private static void addInstance(Terrarium terrarium) {
    if (terraria == null) {
      terraria = new ArrayList<>();
    }
    terraria.add(terrarium);
  }

  /**
   * Get the default terrarium instance (instantiating it, if necessary)
   *
   * @return The default terrarium
   */
  public static Terrarium getInstance() {
    return getInstance(0);
  }

  /**
   * Get a particular Terrarium instance
   *
   * @param index [0..<i>n</i>) if there are <i>n</i> terraria, sequenced by instantiation
   *              order
   * @return The terrarium at this index
   */
  public static Terrarium getInstance(int index) {
    if (terraria == null) {
      terraria = new ArrayList<>();
      new Terrarium();
    }
    return terraria.get(index);
  }

  private JFrame getFrame() {
    if (frame == null) {
      frame = new JFrame("Turtle Logo");
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.add(this);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    }
    return frame;
  }

  /**
   * <p>Adds a new turtle track to the terrarium</p>
   *
   * <p>May only be called by {@link Turtle} and its subclasses, enforced via {@link Turtle.UnderTheShell}.</p>
   *
   * @param track to be added
   * @param key   to authenticate "Turtleness"
   */
  public synchronized void add(Track track, Turtle.UnderTheShell key) {
    key.hashCode();
    tracks.add(track);
    repaint();
  }

  /**
   * Clear all turtle tracks from the terrarium
   */
  public synchronized void clear() {
    tracks.clear();
    repaint();
  }

  /**
   * <p>Adds a new turtle to the terrarium</p>
   *
   * <p>May only be called by {@link Turtle} and its subclasses, enforced via {@link Turtle.UnderTheShell}.</p>
   *
   * @param turtle to be added
   * @param key    to authenticate "Turtleness"
   */
  public synchronized void add(Turtle turtle, Turtle.UnderTheShell key) {
    key.hashCode();
    turtles.add(turtle);
    repaint();
  }

  /**
   * <p>Remove a turtle from the terrarium</p>
   *
   * <p>May only be called by {@link Turtle} and its subclasses, enforced via {@link Turtle.UnderTheShell}.</p>
   *
   * @param turtle to be removed
   * @param key    to authenticate "Turtleness"
   */
  public synchronized void remove(Turtle turtle, Turtle.UnderTheShell key) {
    key.hashCode();
    turtles.remove(turtle);
    repaint();
  }

  /**
   * Adjust the dimensions of the terrarium view
   *
   * @param width  in pixels
   * @param height in pixels
   */
  public void setSize(int width, int height) {
    setPreferredSize(new Dimension(width, height));
    getFrame().pack();
    getFrame().repaint();
  }

  /**
   * Adjust the location of the terrarium window. Note that the screen origin is in the top, left corner of the display
   * and that, while the X-axis increases from left to right, the Y-axis <i>increases</i> from top to bottom. The
   * coordinates given are for the origin of the window (the top, left corner)
   *
   * @param x coordinate
   * @param y coordinate
   */
  public void setPosition(int x, int y) {
    getFrame().setLocation(x, y);
    getFrame().repaint();
  }

  /**
   * <p>Repaint the contents of the terrarium (tracks and turtles) as-needed</p>
   *
   * <p>This method is called automatically by the enclosing {@link JFrame} to repaint the terrarium as-needed. It is
   * not meant to be called at will. If the terrarium needs to be updated, a {@link #repaint()} request will schedule
   * the update.</p>
   *
   * @param context for drawing commands
   */
  @Override
  public synchronized void paintComponent(Graphics context) {
    super.paintComponent(context);
    Graphics2D context2D = (Graphics2D) context;
    draw(context2D);
  }

  /**
   * For synchronous drawing requests (e.g. saving images)
   *
   * @param context for drawing commands
   */
  protected void draw(Graphics2D context) {
    context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    for (Track track : tracks) {
      track.draw(context, UNDER_THE_SURFACE);
    }
    for (Turtle turtle : turtles) {
      turtle.draw(context, UNDER_THE_SURFACE);
    }
  }
}

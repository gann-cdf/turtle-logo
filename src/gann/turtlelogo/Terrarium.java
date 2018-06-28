package gann.turtlelogo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A turtle lives (and draws) inside a Terrarium;
 */
public class Terrarium extends JPanel {
  /**
   * The parts of the terrarium that are "under the surface" are not meant to be used by students. This mechanism
   * (inspired by <a href="https://stackoverflow.com/a/18634125">this awesome Stack Overflow answer</a>) recreates a
   * version of the C++ <code>friend</code> concept: a public method that is only available to <i>some</i> other
   * objects, rather than <i>all</i> other objects.
   *
   * @author <a href="https://github.com/battis">Seth Battis</a>
   */
  public static final class UnderTheSurface {
    private UnderTheSurface() {}
  }
  protected static final UnderTheSurface UNDER_THE_SURFACE = new UnderTheSurface();

  public static final int
          DEFAULT_WIDTH = 600,
          DEFAULT_HEIGHT = 400;

  private static List<Terrarium> terraria;
  private JFrame frame;
  private List<Turtle> turtles;
  private List<Track> tracks;

  public Terrarium() {
    super();
    setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    setBackground(Color.white);
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

  public static Terrarium getInstance() {
    return getInstance(0);
  }

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

  public synchronized void add(Track track, Turtle.UnderTheShell key) {
    key.hashCode();
    tracks.add(track);
  }

  public synchronized void clear() {
    tracks.clear();
  }

  public synchronized void add(Turtle turtle, Turtle.UnderTheShell key) {
    key.hashCode();
    turtles.add(turtle);
  }

  public synchronized void remove(Turtle turtle, Turtle.UnderTheShell key) {
    key.hashCode();
    turtles.remove(turtle);
  }

  public void setSize(int width, int height) {
    setPreferredSize(new Dimension(width, height));
    getFrame().pack();
    getFrame().repaint();
  }

  public void setPosition(int x, int y) {
    getFrame().setLocation(x, y);
    getFrame().repaint();
  }

  @Override
  public synchronized void paintComponent(Graphics reverseCompatibleContext) {
    super.paintComponent(reverseCompatibleContext);
    Graphics2D context = (Graphics2D) reverseCompatibleContext;
    context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    for (Track track : tracks) {
      track.draw(context, UNDER_THE_SURFACE);
    }
    for (Turtle turtle : turtles) {
      turtle.draw(context, UNDER_THE_SURFACE);
    }
  }
}

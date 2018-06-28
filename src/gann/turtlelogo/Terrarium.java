package gann.turtlelogo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A turtle lives (and draws) inside a Terrarium;
 */
public class Terrarium extends JPanel {
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
    register(this);
  }

  private static void register(Terrarium terrarium) {
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
      terraria.add(new Terrarium());
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

  public synchronized void add(Track track) {
    tracks.add(track);
  }

  public synchronized void clear() {
    tracks.clear();
  }

  public synchronized void add(Turtle turtle) {
    turtles.add(turtle);
  }

  public synchronized void remove(Turtle turtle) {
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
  public synchronized void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D graphics2d = (Graphics2D) graphics;
    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    for (Track track : tracks) {
      track.draw(graphics2d);
    }
    for (Turtle turtle : turtles) {
      turtle.draw(graphics2d);
    }
  }
}

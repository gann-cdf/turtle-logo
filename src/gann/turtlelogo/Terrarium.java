package gann.turtlelogo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrarium extends JPanel {
  public static final int
          DEFAULT_WIDTH = 600,
          DEFAULT_HEIGHT = 400;

  private static JFrame frame;
  private List<Turtle> turtles;
  private List<Track> tracks;

  public Terrarium() {
    super();
    setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    setBackground(Color.white);
    initializeSingletonFrame();
    turtles = new ArrayList<>();
    tracks = new ArrayList<>();
  }

  private void initializeSingletonFrame() {
    if (frame == null) {
      frame = new JFrame("Turtle Logo");
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.add(this);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    }
  }

  public synchronized void addTrack(Track track) {
    tracks.add(track);
  }

  public synchronized void addTurtle(Turtle turtle) {
    turtles.add(turtle);
  }

  public void setSize(int width, int height) {
    setPreferredSize(new Dimension(width, height));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.repaint();
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

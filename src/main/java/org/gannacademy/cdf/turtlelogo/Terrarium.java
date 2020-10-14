package org.gannacademy.cdf.turtlelogo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

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
public class Terrarium extends JPanel implements KeyListener {
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

    private final List<Turtle> turtles;
    private final List<Track> tracks;

    private JFrame frame;
    public Semaphore ready;

    private static final int CONTROL_KEY_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

    /**
     * Construct a new terrarium of default dimensions, centered on the screen in its own window
     */
    public Terrarium() {
        super();
        turtles = new Vector<>();
        tracks = new Vector<>();
        ready = new Semaphore(0);
        addInstance(this);
        Terrarium self = this;
        SwingUtilities.invokeLater(() -> {
            setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
            setBackground(DEFAULT_BACKGROUND);
            getFrame();
            self.addKeyListener(self);
            self.requestFocus();
        });
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
            Terrarium self = this;
            SwingUtilities.invokeLater(() -> {
                frame = new JFrame("Turtle Logo");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.add(self);
                frame.pack();
                ready.release();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
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
        assert key != null;
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
        assert key != null;
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
        assert key != null;
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

    /**
     * Draw the contents of the terrarium to a file
     *
     * @param path Path to the file to be saved as a PNG
     */
    public void drawTo(String path) {
        drawTo(path, "PNG");
    }

    private class ImageFileRenderer extends SwingWorker<Void, Void> {

        private final String path;
        private final String format;

        public ImageFileRenderer(String path, String format) {
            assert path != null;
            assert format != null;
            this.path = path;
            this.format = format;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D context = image.createGraphics();
                context.setPaint(getBackground());
                context.fillRect(0, 0, image.getWidth(), image.getHeight());
                draw(context);
                File file = new File(path);
                ImageIO.write(image, format, file);
                System.out.println(image.getWidth() + "x" + image.getHeight() + " pixel image saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Draw the contents of the terrarium to a file
     *
     * @param path   Path to the file to be saved
     * @param format Format in which to save the file (anyting accepted by <a href="https://docs.oracle.com/javase/10/docs/api/javax/imageio/ImageIO.html#write(java.awt.image.RenderedImage,java.lang.String,java.io.File)">ImageIO.write()</a>)
     */
    public void drawTo(String path, String format) {
        new ImageFileRenderer(path, format).execute();
    }

    /**
     * Handle keyboard input
     *
     * @param e keyboard event descriptor
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handle keyboard input
     *
     * @param e keyboard event descriptor
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Handle keyboard input
     *
     * @param e keyboard event descriptor
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && ((e.getModifiersEx() | CONTROL_KEY_MASK) == CONTROL_KEY_MASK)) {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter pngFilter = new FileNameExtensionFilter("PNG files", "png");
            fileChooser.addChoosableFileFilter(pngFilter);
            fileChooser.setFileFilter(pngFilter);
            fileChooser.setDialogTitle("Save terrarium contents…");
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".png")) {
                    filePath = filePath + ".png";
                }
                drawTo(filePath);
            }
        }
    }
}

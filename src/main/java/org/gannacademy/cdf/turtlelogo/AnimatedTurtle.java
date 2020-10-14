package org.gannacademy.cdf.turtlelogo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Animated turtles are turtles that move more slowly, so that we can observe them following their instructions. Their
 * speed can be adjusted either when they are instantiated or as a separate instruction ({@link #speed(long)})
 *
 * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
 */
public class AnimatedTurtle extends Turtle implements Runnable {

    /**
     * 10 milliseconds
     */
    public static final long DEFAULT_FRAME_DELAY = 10; // milliseconds

    /**
     * <p>25 milliseconds</p>
     * <p>This is an internal measure, sued to determine how and when to animate turtle turns (frame delays longer than
     * this cutoff will have the turns animated in larger steps</p>
     */
    public static final long TURN_SPEED_CUTOFF = 25; // milliseconds

    private enum Verb {
        MOVE, TURN, HEAD,
        PEN_UP, PEN_DOWN, PEN_COLOR, PEN_WIDTH,
        HIDE, SHOW,
        MOVE_TO, TELEPORT, HOME,
        SPEED
    }

    private static int nextSerial = 1;

    private static class Instruction {
        private Verb verb;
        private Object parameter;
        private final int serial = nextSerial++;

        public Instruction(Verb verb) {
            this.verb = verb;
        }

        public Instruction(Verb verb, double value) {
            this.verb = verb;
            this.parameter = value;
        }

        public Instruction(Verb verb, long value) {
            this.verb = verb;
            this.parameter = value;
        }

        public Instruction(Verb verb, double x, double y) {
            this.verb = verb;
            this.parameter = new Point2D.Double(x, y);
        }

        public Instruction(Verb verb, Color color) {
            this.verb = verb;
            this.parameter = color;
        }

        public Verb getVerb() {
            return verb;
        }

        public double getDoubleParam() {
            return (double) parameter;
        }

        public long getLongParam() {
            return (long) parameter;
        }

        public Point2D getPointParam() {
            return (Point2D) parameter;
        }

        public Color getColorParam() {
            return (Color) parameter;
        }

        public void convertTo(Verb verb) {
            this.verb = verb;
        }

        @Override
        public String toString() {
            return verb + "." + serial + "(" + parameter + ")";
        }
    }

    private static class CountDownAndUpLatch {
        private CountDownLatch latch;
        private final Object lock = new Object();

        public CountDownAndUpLatch(int count) {
            this.latch = new CountDownLatch(count);
        }

        public void countUp() {
            synchronized (lock) {
                while (latch.getCount() == Long.MAX_VALUE) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                latch = new CountDownLatch((int) (latch.getCount() + 1));
                lock.notifyAll();
            }
        }

        public void countDown() {
            synchronized (lock) {
                while(latch.getCount() == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
                lock.notifyAll();
            }
        }

        public void await() {
            synchronized (lock) {
                while (latch.getCount() != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public long getCount() {
            synchronized (lock) {
                return latch.getCount();
            }
        }
    }

    private Queue<Instruction> instructions;
    private Instruction activeInstruction;
    private double MOVE_steps, MOVE_targetSteps, TURN_degrees, TURN_targetDegrees, MOVE_TO_tempHeadingInRadians;
    private long frameDelay, tick;
    private boolean threadStarted = false;
    private CountDownAndUpLatch contextUpdated;

    /**
     * Construct an animated turtle with {@link #DEFAULT_FRAME_DELAY}
     */
    public AnimatedTurtle() {
        this(DEFAULT_FRAME_DELAY);
    }

    /**
     * Construct an animated turtle with a custom frame delay
     *
     * @param frameDelay between frames of animation, measured in milliseconds
     */
    public AnimatedTurtle(long frameDelay) {
        this(frameDelay, Terrarium.getInstance());
    }

    /**
     * Construct an animated turtle with {@link #DEFAULT_FRAME_DELAY} in a specific terrarium
     *
     * @param terrarium to house the turtle
     */
    public AnimatedTurtle(Terrarium terrarium) {
        this(DEFAULT_FRAME_DELAY, terrarium);
    }

    /**
     * Construct an animated turtle with a custom frame delay in a specific terrarium
     *
     * @param frameDelay between frames of animation, measured in milliseconds
     * @param terrarium  to house the turtle
     */
    public AnimatedTurtle(long frameDelay, Terrarium terrarium) {
        super(terrarium);
        this.frameDelay = frameDelay;
        tick = System.currentTimeMillis();
        instructions = new ConcurrentLinkedQueue<>(); // thread-safe
        contextUpdated = new CountDownAndUpLatch(1);
        new Thread(this, "AnimatedTurtle").start();
        contextUpdated.await();
    }

    private void addInstruction(Instruction instruction) {
        contextUpdated.countUp();
        instructions.add(instruction);
    }

    @Override
    public double getX() {
        if (!SwingUtilities.isEventDispatchThread()) {
            contextUpdated.await();
        } // FIXME else... WTF?
        return super.getX();
    }

    @Override
    public void move(double steps) {
        addInstruction(new Instruction(Verb.MOVE, steps));
    }

    @Override
    public void turn(double angle) {
        addInstruction(new Instruction(Verb.TURN, angle));
    }

    @Override
    public void head(double heading) {
        addInstruction(new Instruction(Verb.HEAD, heading));
    }

    @Override
    public void penUp() {
        addInstruction(new Instruction(Verb.PEN_UP));
    }

    @Override
    public void penDown() {
        addInstruction(new Instruction(Verb.PEN_DOWN));
    }

    @Override
    public void penColor(Color color) {
        addInstruction(new Instruction(Verb.PEN_COLOR, color));
    }

    @Override
    public void penWidth(double width) {
        addInstruction(new Instruction(Verb.PEN_WIDTH, width));
    }

    @Override
    public void hide() {
        addInstruction(new Instruction(Verb.HIDE));
    }

    @Override
    public void show() {
        addInstruction(new Instruction(Verb.SHOW));
    }

    @Override
    public void teleport(double x, double y) {
        addInstruction(new Instruction(Verb.TELEPORT, x, y));
    }

    @Override
    public void moveTo(double x, double y) {
        addInstruction(new Instruction(Verb.MOVE_TO, x, y));
    }

    @Override
    public void home() {
        addInstruction(new Instruction(Verb.HOME));
    }

    /**
     * Alias for {@link #speed(long)}
     *
     * @param frameDelay in milliseconds
     */
    public void sp(long frameDelay) {
        speed(frameDelay);
    }

    /**
     * <p>Set the speed of the turtle's animation</p>
     * <p>The frame delay is the time between individual frames of animation. Shorter frame delays
     * (below {@link #TURN_SPEED_CUTOFF}) will cause turn animations to be animated in more detail to allow them to remain
     * visible to the naked eye</p>
     *
     * @param frameDelay in milliseconds
     */
    public void speed(long frameDelay) {
        addInstruction(new Instruction(Verb.SPEED, frameDelay));
    }

    private void instructionFinished() {
        Instruction i = activeInstruction;
        activeInstruction = null;
        contextUpdated.countDown();
    }

    /**
     * <p>Thread execution</p>
     * <p>This method is the control loop that allows animation to be updated in a separate control loop for each turtle.
     * This method should not be called by students (although calling it manually should do nothing).</p>
     */
    @Override
    public void run() {
        if (!threadStarted) {
            threadStarted = true;
            contextUpdated.countDown();
            while (true) {
                if (activeInstruction == null) {
                    if (!instructions.isEmpty()) {
                        activeInstruction = instructions.remove();
                        switch (activeInstruction.getVerb()) {
                            case MOVE:
                                MOVE_targetSteps = activeInstruction.getDoubleParam();
                                MOVE_steps = 0;
                                break;
                            case MOVE_TO:
                                double dx = activeInstruction.getPointParam().getX() - getX(),
                                    dy = activeInstruction.getPointParam().getY() - getY();
                                MOVE_targetSteps = Math.hypot(dx, dy);
                                MOVE_steps = 0;
                                MOVE_TO_tempHeadingInRadians = Math.atan2(dy, dx);
                                break;
                            case TURN:
                                TURN_targetDegrees = activeInstruction.getDoubleParam();
                                TURN_degrees = 0;
                                break;
                            case HEAD:
                                activeInstruction.convertTo(Verb.TURN);
                                if (Math.abs(getHeadingInDegrees() - activeInstruction.getDoubleParam()) > 180.0) {
                                    TURN_targetDegrees = (360 - Math.abs(getHeadingInDegrees() - activeInstruction.getDoubleParam())) * (getHeadingInDegrees() > activeInstruction.getDoubleParam() ? 1 : -1);
                                } else {
                                    TURN_targetDegrees = getHeadingInDegrees() - activeInstruction.getDoubleParam();
                                }
                                TURN_degrees = 0;
                                break;
                            case PEN_UP:
                                super.penUp();
                                instructionFinished();
                                break;
                            case PEN_DOWN:
                                super.penDown();
                                instructionFinished();
                                break;
                            case PEN_COLOR:
                                super.penColor(activeInstruction.getColorParam());
                                instructionFinished();
                                break;
                            case PEN_WIDTH:
                                super.penWidth(activeInstruction.getDoubleParam());
                                instructionFinished();
                                break;
                            case HIDE:
                                super.hide();
                                instructionFinished();
                                break;
                            case SHOW:
                                super.show();
                                instructionFinished();
                                break;
                            case TELEPORT:
                                super.teleport(activeInstruction.getPointParam().getX(), activeInstruction.getPointParam().getY());
                                instructionFinished();
                                break;
                            case HOME:
                                super.home();
                                instructionFinished();
                                break;
                            case SPEED:
                                frameDelay = activeInstruction.getLongParam();
                                instructionFinished();
                                break;
                        }
                    }
                } else if (System.currentTimeMillis() > tick + frameDelay) {
                    tick = System.currentTimeMillis();
                    switch (activeInstruction.getVerb()) {
                        case MOVE:
                        case MOVE_TO:
                            if (Math.abs(MOVE_steps) >= Math.abs(MOVE_targetSteps)) {
                                if (activeInstruction.getVerb() == Verb.MOVE) {
                                    super.move(MOVE_targetSteps);
                                } else {
                                    super.moveTo(activeInstruction.getPointParam().getX(), activeInstruction.getPointParam().getY());
                                }
                                instructionFinished();
                            } else {
                                MOVE_steps += (MOVE_targetSteps > 0.0 ? 1 : -1);
                            }
                            break;
                        case TURN:
                            if (Math.abs(TURN_degrees) >= Math.abs(TURN_targetDegrees)) {
                                super.turn(TURN_targetDegrees);
                                instructionFinished();
                            } else {
                                TURN_degrees += (TURN_targetDegrees > 0.0 ? 1 : -1) * (frameDelay >= TURN_SPEED_CUTOFF ? 1 : TURN_SPEED_CUTOFF - frameDelay);
                            }
                            break;
                    }
                    getTerrarium().repaint();
                }
            }
        }
    }

    public void draw(Graphics2D context, Terrarium.UnderTheSurface key) {
        key.hashCode();
        if (activeInstruction == null) {
            super.draw(context, key);
        } else {
            context.setPaint(getPenColor());
            context.setStroke(getPenStroke());
            switch (activeInstruction.getVerb()) {
                case MOVE:
                case MOVE_TO:
                    double moveHeadingInRadians = MOVE_TO_tempHeadingInRadians;
                    if (activeInstruction.getVerb() == Verb.MOVE) {
                        moveHeadingInRadians = getHeadingInRadians();
                    }
                    double tempX = getX() + Math.cos(moveHeadingInRadians) * MOVE_steps;
                    double tempY = getY() + Math.sin(moveHeadingInRadians) * MOVE_steps;
                    if (isPenDown()) {
                        context.draw(new Line2D.Double(getX(), getY(), tempX, tempY));
                    }
                    if (!isHidden()) {
                        drawIcon(tempX, tempY, getHeadingInRadians(), context);
                    }
                    break;
                case TURN:
                    if (!isHidden()) {
                        drawIcon(getX(), getY(), Math.toRadians(getHeadingInDegrees() + TURN_degrees), context);
                    }
                    break;
            }
        }
    }
}

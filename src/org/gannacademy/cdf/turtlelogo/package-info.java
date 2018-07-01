/**
 * <p>Turtle logo is an introduction to computer programming. We imagine that we are working with a turtle that is
 * holding a pen &mdash; presumably in its mouth. As the turtle roams around its terrarium, it drags the pen, leaving
 * tracks wherever it goes.</p>
 *
 * <h3>Sample Program</h3>
 *
 * <p>A simple example of a program to control a turtle might look something like this:</p>
 *
 * <pre>
 *   import org.gannacademy.cdf.turtlelogo.*;
 *
 *   public class DrawASquare{
 *     public static void main(String[] args) {
 *       Turtle cecil = new Turtle(); // instantiate a new turtle
 *
 *       // give the turtle some instructions
 *       cecil.fd(100);
 *       cecil.lt(90);
 *       cecil.fd(100);
 *       cecil.lt(90);
 *       cecil.fd(100);
 *       cecil.lt(90);
 *       cecil.fd(100);
 *     }
 *   }
 * </pre>
 *
 * <p>The output of this program would be:</p>
 *
 * <p style="text-align: center"><img src="doc-files/cecil.png" alt="Cecil's output" style="border: solid 1px #ccc"></p>
 *
 * @author <a href="https://github.com/gann-cdf/turtlelogo/issues">Seth Battis</a>
 */
package org.gannacademy.cdf.turtlelogo;
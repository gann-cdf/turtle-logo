package org.gannacademy.cdf.turtlelogo.docs;

import org.gannacademy.cdf.turtlelogo.Terrarium;
import org.gannacademy.cdf.turtlelogo.Turtle;

import java.awt.*;
import java.io.File;

public class CreateDocsImages {
  public static final String BASE_PATH = "src/org/gannacademy/cdf/turtlelogo/doc-files";

  private static void deleteContents(File directory) {
    File[] contents = directory.listFiles();
    if (contents != null) {
      for (File file : contents) {
        if (file.isDirectory()) {
          deleteContents(file);
        } else {
          file.delete();
        }
      }
    }
  }

  private static void resetDirectory(String path) {
    if (!new File(BASE_PATH).mkdirs()) {
      deleteContents(new File(BASE_PATH));
    }
  }

  private static String site(String name) {
    return BASE_PATH + "/" + name + ".png";
  }

  public static void main(String[] args) {
    Turtle turtle = new Turtle();
    Terrarium terrarium = turtle.getTerrarium();
    resetDirectory(BASE_PATH);

    // cecil.png
    turtle.fd(100);
    turtle.lt(90);
    turtle.fd(100);
    turtle.lt(90);
    turtle.fd(100);
    turtle.lt(90);
    turtle.fd(100);
    terrarium.drawTo(site("cecil"));

    // resize for small figures
    terrarium.setSize(148, 92); // 148 is min width, 92:148 is golden ratio

    // trail.png
    terrarium.clear();
    turtle.home();
    turtle.hd(Turtle.WEST);
    for (double i = 0; i < 44; i++) {
      turtle.rt(15);
      turtle.fd(i / 3.0);
    }
    terrarium.drawTo(site("trail"));

    // move.png
    terrarium.clear();
    turtle.home();
    turtle.lt(36);
    turtle.tp(5, terrarium.getHeight() - 5);
    turtle.to(terrarium.getWidth() - 5, 5);
    terrarium.drawTo(site("move"));

    // turn.png
    terrarium.clear();
    Turtle[] turtles = new Turtle[8];
    int width = 115;
    for (int i = 0; i < turtles.length; i++) {
      turtles[i] = new Turtle();
      turtles[i].tp((terrarium.getWidth() - width) / 2 + width / turtles.length * i, terrarium.getHeight() / 2);
      turtles[i].lt(180 / turtles.length * i);
    }
    turtle.tp((terrarium.getWidth() - width) / 2 + width, terrarium.getHeight() / 2);
    turtle.hd(180);
    terrarium.drawTo(site("turn"));
    for (int i = 0; i < turtles.length; i++) {
      turtles[i].tp(-10, -10);
    }

    // penColor.png
    terrarium.clear();
    turtle.home();
    turtle.tp(20, terrarium.getHeight() / 2);
    turtle.pc(Color.red);
    turtle.to(terrarium.getWidth() - 20, terrarium.getHeight() / 2);
    terrarium.drawTo(site("penColor"));

    // penWidth
    terrarium.clear();
    turtle.tp(20, terrarium.getHeight() / 2);
    turtle.pc(Color.green);
    turtle.pw(15);
    turtle.to(terrarium.getWidth() - 20, terrarium.getHeight() / 2);
    terrarium.drawTo(site("penWidth"));

    // setBackground
    terrarium.clear();
    turtle.home();
    terrarium.setBackground(Color.pink);
    terrarium.drawTo(site("setBackground"));

    // moveTo
    terrarium.clear();
    terrarium.setBackground(Color.white);
    turtle.pc(Color.black);
    turtle.pw(1);
    turtle.home();
    turtle.lt(36);
    turtle.lt(90);
    turtle.tp(15, terrarium.getHeight() - 10);
    turtle.to(terrarium.getWidth() - 15, 10);
    terrarium.drawTo(site("moveTo"));

    // teleport
    terrarium.clear();
    turtle.home();
    turtle.tp(15, terrarium.getHeight() - 10);
    turtles[0].home();
    turtles[0].tp(terrarium.getWidth() - 5, 10);
    terrarium.drawTo(site("teleport"));

    // hide
    terrarium.clear();
    turtles[0].tp(-10, -10);
    turtle.home();
    double side = terrarium.getWidth() / 3;
    turtle.tp(side, terrarium.getHeight() - (side / 2));
    turtle.lt(60);
    turtle.fd(side);
    turtle.rt(120);
    turtle.fd(side);
    turtle.rt(120);
    turtle.fd(side);
    turtle.ht();
    terrarium.drawTo(site("hide"));

    turtle.st();
    terrarium.drawTo(site("show"));

    System.exit(0);
  }
}

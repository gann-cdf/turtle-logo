package org.gannacademy.cdf.turtlelogo.docs;

import org.gannacademy.cdf.turtlelogo.Terrarium;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SavableTerrarium extends Terrarium {
  public void drawTo(String path) {
    drawTo(path, "PNG");
  }

  public void drawTo(String path, String format) {
    try {
      BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D context = image.createGraphics();
      context.setPaint(getBackground());
      context.fillRect(0, 0, image.getWidth(), image.getHeight());
      draw(context);
      ImageIO.write(image, format, new File(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

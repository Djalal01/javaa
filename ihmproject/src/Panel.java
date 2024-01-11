import java.awt.image.BufferedImage;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.io.IOException;

import javax.imageio.ImageIO;


import javax.swing.*;


import java.awt.*;

import java.io.File;




public class Panel extends JPanel {
  private BufferedImage image;
   
  public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
}  

    
 public Panel() {
  try {                
     image = resize(ImageIO.read(new File("images/7895078.jpg")),800,600);
     
  } catch (IOException ex) {
       // handle exception...
  }
}
    @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
}
    }



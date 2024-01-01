
import java.awt.BorderLayout;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JPanel;




public class panel extends JPanel {
  
    BufferedImage image;
    public void Panel() {
        setLayout(new BorderLayout());
        try {
            image = ImageIO.read(getClass().getResource("thomas-ensley-sdu0mylxmEM-unsplash.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }



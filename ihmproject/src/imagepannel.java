import java.awt.*;

import java.net.MalformedURLException;
import javax.swing.*;
import java.net.URL;
class ImagePanel extends JPanel {

    Image image;

    public ImagePanel() throws MalformedURLException {
        image = Toolkit.getDefaultToolkit().createImage(
                new URL("https://cdn.dribbble.com/users/1520241/screenshots/4817391/media/dadd17150a531b206ede58106f906847.gif"));
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        // this allows us to pack() the window around the image
        return (new Dimension(image.getWidth(this), image.getHeight(this)));
    }
}
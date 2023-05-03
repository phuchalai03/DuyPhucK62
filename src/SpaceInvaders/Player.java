package SpaceInvaders;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class Player extends Sprite implements Commons{

    private final int START_Y = 340; 
    private final int START_X = 270;

    private final String player = "img/player.png";
    private int width;
    private int heigth;

    public Player() {

        ImageIcon ii = new ImageIcon(player);

        width = ii.getImage().getWidth(null); 
        heigth = ii.getImage().getHeight(null);
        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        x += dx;
        y += dy;
        if (x <= 2) {
            x = 2;}
        if (x >= BOARD_WIDTH - 2*width) {
            x = BOARD_WIDTH - 2*width;}
        
        if (y <= 2) {
            y = 2;}
        if (y >= BOARD_HEIGTH - 6*heigth) {
            y = BOARD_HEIGTH - 6*heigth;}
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 2;
        }
        if (key == KeyEvent.VK_UP)
        {
            dy = -2;
        }

        if (key == KeyEvent.VK_DOWN)
        {
            dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP)
        {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN)
        {
            dy = 0;
        }
    }
}

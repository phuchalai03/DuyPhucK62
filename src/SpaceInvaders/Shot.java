package SpaceInvaders;

import javax.swing.ImageIcon;

public class Shot extends Sprite {

    private String shot = "img/shot.png";
    private final int H_SPACE = 3; // điểm xuất phát của đạn theo dy
    private final int V_SPACE = 1;  //điểm xuất phát của đạn theo dx

    public Shot() {
    }

    public Shot(int x, int y) {
        ImageIcon ii = new ImageIcon(shot);
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}
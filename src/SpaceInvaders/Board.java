package SpaceInvaders;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Board extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList aliens;
    private Player player;
    private Shot shot;

    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;
    private int point ;
    private boolean ingame = true;
    private final String expl = "img/explosion.png";
    private final String alienpix = "img/alien.png";
    private String message = "Game Over";
    private String messagePoint = "Point: ";
    private String message2 = "Press space to restart!";
    private Thread animator;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);
        gameInit();
        setDoubleBuffered(true);//chống nhấp nháy 
        setLayout(null);       
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() { 

        aliens = new ArrayList();
        ImageIcon ii = new ImageIcon("img/alien.png");
        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Alien alien = new Alien(alienX + 18*j, alienY + 18*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }
        player = new Player();
        shot = new Shot();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible())
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    }

    public void drawBombing(Graphics g) {
        Iterator i3 = aliens.iterator();
        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }
    public void paint(Graphics g)
    {
      super.paint(g);
       int fontsize=14;
      g.setColor(Color.black);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.white);
      g.setFont(new Font("TimesRoman", Font.BOLD, fontsize));

      if (ingame) {
        message = "Game Over!";
        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        drawAliens(g);
        drawPlayer(g);
        drawShot(g);
        drawBombing(g);
        if (ingame)
        {
        	int x = player.getX();
        	int y = player.getY();
              if (!shot.isVisible())
                  shot = new Shot(x, y);
        }
      }
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
  
    public void gameOver()
    {

        Graphics g = this.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);
        
        Font medium = new Font("Helvetica", Font.ITALIC, 14);
        FontMetrics metr1 = this.getFontMetrics(medium);
        
        Font max = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr2 = this.getFontMetrics(max);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2,BOARD_WIDTH/2);
        g.setFont(max);
        g.drawString(messagePoint+point, 165, 150);
        g.setFont(medium);
        g.drawString(message2, 130, 300);
    }
    
    public void animationCycle()  {
        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";   
        }
        // player
        player.act();
        if (player.isVisible()) {
        	Iterator it = aliens.iterator();
            int playerX = player.getX();
            int playerY = player.getY();
            
            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();
                if (alien.isVisible() && player.isVisible()) {
                	if (playerX >= (alienX) && 
                            playerX <= (alienX + PLAYER_WIDTH) &&
                            playerY >= (alienY) &&
                            playerY<= (alienY+PLAYER_HEIGHT) ) {
                                ImageIcon ii = 
                                    new ImageIcon(expl);
                                player.setImage(ii.getImage());
                                player.setDying(true);   
                    }
                }
            }
        }
        // shot
        if (shot.isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = shot.getX();
            int shotY = shot.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();
                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX) && 
                        shotX <= (alienX + ALIEN_WIDTH) &&
                        shotY >= (alienY) &&
                        shotY <= (alienY+ALIEN_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(expl);
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            deaths++;
                            shot.die();
                        }
                }      
            }
            int y = shot.getY();
            y -= 4;
            if (y < 0)
                shot.die();
            else shot.setY(y);
        }
        // aliens
         Iterator it1 = aliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int x = a1.getX();
             if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                 direction = -1;
                 Iterator i1 = aliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                     a2.setY(a2.getY() + GO_DOWN);
                 }
            if (x <= BORDER_LEFT && direction != 1) {//máy bay địch sang trái trc
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }
        Iterator it = aliens.iterator();
        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {
               int y = alien.getY();
                if (y > GROUND - ALIEN_HEIGHT) {//khi địch xuống đất
                    ingame = false;
                    message = "Invasion!";
                }
                alien.act(direction);// làm máy bay địch di chuyển
            }
        }

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
                b.setX(a.getX());//lấy tọa độ quả bom theo máy bay địch
                b.setY(a.getY());//lấy tọa độ quả bom theo máy bay địch   
            }
            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            if (player.isVisible() && !b.isDestroyed()) {
                if ( bombX >= (playerX) && 
                    bombX <= (playerX+PLAYER_WIDTH) &&
                    bombY >= (playerY) && 
                    bombY <= (playerY+PLAYER_HEIGHT) ) {
                        ImageIcon ii = 
                            new ImageIcon(expl);
                        player.setImage(ii.getImage());
                        player.setDying(true);
                        b.setDestroyed(true);;
                    }
            }
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);   
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
        point=deaths*10;}
    }

    public void run() {

        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (ingame) {
            repaint();
            animationCycle();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }
    
    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
        public void keyPressed(KeyEvent e) {
          player.keyPressed(e);
          int x = player.getX();
          int y = player.getY();

          if (ingame)
          {
                if (!shot.isVisible())
                    shot = new Shot(x, y);
          }
          if (ingame==false) {
        	  if(e.getKeyChar() == KeyEvent.VK_SPACE) {
        		  gameInit(); // khởi tạo lại trò chơi
                  ingame = true; // thiết lập trạng thái ingame
                  point = 0; // thiết lập điểm về 0
                  deaths = 0;
        	  }
          }
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.component;

import game.object.Bullet;
import game.object.Chicken;
import game.object.Player;
import game.object.sound.Sound;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 *
 * @author Chu Dung
 */
public class PanelGame extends JComponent {

    private Graphics2D g2;
    private BufferedImage image;

    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Key key;
    private int shortTime; // store duration when press key shoot;

// Game FPS
    private final int FPS = 60;
    private final int Target_TIME = 1000000000 / FPS; // nano

// Game Object
    private Sound sound;
    private Player player;
    
    private List<Bullet> bullets;
    private List<Chicken> chickens;
    private int score = 0;
    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // smooth

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();

                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < Target_TIME) {
                        long sleep = (Target_TIME - time) / 1000000;
                        sleep(sleep);
                    }

                }
            }
        });
        initObjectGame();
        initKeyBoard();
        initBullets();
        thread.start();
    }
    
    private void addChicken()
    {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Chicken chicken = new Chicken();
        
        chicken.changeLocation(0, locationY); 
        chicken.changeAngle(0);
        chickens.add(chicken);
        int locationY2 = ran.nextInt(height - 50) + 25;
        Chicken chicken2 = new Chicken();
        chicken2.changeLocation(width, locationY2);
        chicken2.changeAngle(180); // moving xoay nguoc hinh lai
        chickens.add(chicken2);
    }
    private void initObjectGame() {
        sound = new Sound();
        player = new Player();
        player.changeLocation(150, 150);
        chickens = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start)
                {
                    addChicken();
                    sleep(3000);
                }
            }
        }).start();
    }
    
    private void resetGame()
    {
        score = 0;
        chickens.clear();
        bullets.clear();
        player.changeLocation(150, 150);
        player.reset();
    }
    private void initKeyBoard() {
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_A) key.setKey_left(true);
                else if(e.getKeyCode() == KeyEvent.VK_D) key.setKey_right(true);
                else if(e.getKeyCode() == KeyEvent.VK_SPACE) key.setKey_space(true);
                else if(e.getKeyCode() == KeyEvent.VK_J) key.setKey_j(true);
                else if(e.getKeyCode() == KeyEvent.VK_ENTER) key.setKey_enter(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_A) key.setKey_left(false);
                else if(e.getKeyCode() == KeyEvent.VK_D) key.setKey_right(false);
                else if(e.getKeyCode() == KeyEvent.VK_SPACE) key.setKey_space(false);
                else if(e.getKeyCode() == KeyEvent.VK_J) key.setKey_j(false);
                else if(e.getKeyCode() == KeyEvent.VK_ENTER) key.setKey_enter(false);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while(start)
                {
                    if(player.isAlive())
                    {
                        
                        float angle = player.getAngle();

                        if(key.isKey_left()) angle -= s;
                        if(key.isKey_right()) angle += s;
                        if(key.isKey_j())
                        {
                            if(shortTime == 0)
                            {
                                if(key.isKey_j())
                                {
                                    bullets.add(0,new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                                }
                              
                                sound.soundShoot();
                            }
                            shortTime++;
                            if(shortTime == 15) shortTime = 0;

                        }
                        else
                        {
                            shortTime = 0;
                        }
                        if(key.isKey_space())
                        {
                            player.speedUp();
                        }
                        else
                        {
                            player.speedDown();
                        }
                        player.update();
                        player.changeAngle(angle);
                    }
                    else
                    {
                        if(key.isKey_enter())
                        {
                            resetGame();
                        }
                    }
                    for(int i = 0; i < chickens.size(); ++i)
                    {
                        Chicken chicken = chickens.get(i);
                        if(chicken != null)
                        {
                            chicken.update();
                            if(!chicken.check(width, height))
                            {
                                chickens.remove(chicken);
//                                System.out.println("removed!");
                            }
                            else
                            {
                                if(player.isAlive())
                                {
                                    checkPlayer(chicken);
                                }
                            }
                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    private void initBullets()
    {
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start)
                {
                    for(int i = 0; i < bullets.size(); ++i)
                    {
                        Bullet bullet = bullets.get(i);
                        if(bullet != null)
                        {
                            bullet.update();
                            checkBullets(bullet);
                            if(!bullet.check(width, height)) bullets.remove(bullet);
                        }
                        else bullets.remove(bullet);
                    }
                    sleep(1);
                }
            }
        }).start();
    }
    private void checkBullets(Bullet bullet)
    {
        for(int i = 0; i <chickens.size(); ++i)
        {
            Chicken chicken = chickens.get(i);
            if(chicken != null)
            {
                Area area = new Area(bullet.getShape());
                area.intersect(chicken.getShape());
                if(!area.isEmpty())
                {
                    if(!chicken.updateHP(bullet.getSize()))
                    {
                        score++;
                        chickens.remove(chicken);
                        sound.soundCuckoo();
                    }
                    else
                    {
                        sound.soundCollide();
                    }
                    bullets.remove(bullet);
                }
            }
        }
    }
    private void checkPlayer(Chicken chicken)
    {
        if(chicken != null)
        {
            Area area = new Area(player.getShape());
            Rectangle rec = chicken.getShape().getBounds();
            area.intersect(chicken.getShape());
            if(!area.isEmpty())
            {
                double chickenHp = chicken.getHp();
                if(!chicken.updateHP(player.getHp()))
                {
                    chickens.remove(chicken);
                    sound.soundDestroy();
                }

                if( !player.updateHP(chickenHp /2))
                {
                    player.setAlive(false);
                    sound.soundDestroy();
                }
            }
        }
        
    }
    private void drawBackground() {
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(0, 0, width, height);

    }

    private void drawGame() {
        if(player.isAlive())
        {
            player.draw(g2);
        }
        for(int i = 0; i < bullets.size(); ++i)
        {
            Bullet bullet = bullets.get(i);
            if(bullet != null) bullet.draw(g2);
        }
        for(int i = 0; i < chickens.size(); ++i)
        {
            Chicken chicken = chickens.get(i);
            if(chicken != null)
            {
                chicken.draw(g2);
            }
        }
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Score : " + score, 10, 20);
        if(!player.isAlive())
        {
            String text = "GAME OVER";
            String textKey = "Press key enter to continue ...";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2);
            double textWidth = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (width - textWidth) / 2;
            double y = (height - textHeight) / 2;
            g2.drawString(text, (int) x, (int) y + fm.getAscent());
            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(textKey, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
             x = (width - textWidth) / 2;
             y = (height - textHeight) / 2;
            g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);
        }
    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }
}

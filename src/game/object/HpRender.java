package game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class HpRender {
    private final HP hp;

    public HpRender(HP hp) {
        this.hp = hp;
    }
    public void hpRender(Graphics2D g2, Shape shape, double y)
    {
            
            double hpY = shape.getBounds().getY() - y - 10;
            g2.setColor(new Color(70, 70, 70));
            g2.fill(new Rectangle2D.Double(0, hpY, Player.PLAYER_SIZE, 2));
            g2.setColor(new Color(253, 91, 91));
            double hpSize = hp.getCurrentHp() / hp.getMAX_HP() *Player.PLAYER_SIZE;
            g2.fill(new Rectangle2D.Double(0, hpY, hpSize, 2));
    }
    public boolean updateHP(double cutHp)
    {
        hp.setCurrentHp(hp.getCurrentHp() - cutHp);
        return hp.getCurrentHp() > 0;
    }
    public double getHp()
    {
        return hp.getCurrentHp();
    }
    public void resetHp()
    {
        hp.setCurrentHp(hp.getMAX_HP());
    }
}
package game.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

public class Chicken extends HpRender{
    public Chicken()
    {
        super(new HP(20, 20));
        this.image = new ImageIcon(getClass().getResource("/game/image/chicken.png")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, CHICKEN_SIZE);
        p.lineTo(CHICKEN_SIZE , CHICKEN_SIZE);
        p.lineTo(CHICKEN_SIZE, 0);
        p.lineTo(0, 0);
        chickenShap = new Area(p);
    }
    public static final double CHICKEN_SIZE = 50;
    private double x;
    private double y;
    private final float speed = 0.3f;
    private float angle = 0;
    private final Image image;
    private final Area chickenShap; // store the shape of obj
    
    public void changeLocation(double x, double y)
    {
        this.x = x;this.y = y;
    }
    public void update()
    {
        x += Math.cos(Math.toRadians(angle))*speed;
        y += Math.sin(Math.toRadians(angle))*speed;
    }

    public void changeAngle(float angle)
    {
        if(angle < 0) angle = 359;
        else if(angle  > 359) angle  = 0;
        this.angle = angle;
    }
    public void draw(Graphics2D g2)
    {
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(180), CHICKEN_SIZE / 2, CHICKEN_SIZE / 2); // Dat x de quay quay
        g2.drawImage(image, tran, null);
        
        Shape shap = getShape();
        hpRender(g2, shap, y);
        g2.setTransform(oldTransform);
        // Test shap
//        g2.setColor(new Color(12, 173, 84));
//        g2.draw(getShape());
//        g2.draw(getShape().getBounds());
        
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public float getAngle()
    {
        return angle;
    }
    public Area getShape()
    {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), CHICKEN_SIZE / 2, CHICKEN_SIZE / 2);
        return new Area(afx.createTransformedShape(chickenShap));
    }
    public boolean check(int width, int height)
    {
        Rectangle size = getShape().getBounds();
        if(x < -size.getWidth() || y < -size.getHeight() || x >width || y > height) return false;
        else return true;
    }
}
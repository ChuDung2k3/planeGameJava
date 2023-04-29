/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.main;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import game.component.PanelGame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Chu Dung
 */
public class Main extends JFrame{
    
    public Main()
    {
        init();
    }
    private void init()
    {
        setTitle("ShootingPlane!!");
        setSize(1366, 768);
        setLocationRelativeTo(null); // dat giua man hinh
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        PanelGame panelGame = new PanelGame();
        add(panelGame);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                panelGame.start();
                        
            }
            
        });
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}

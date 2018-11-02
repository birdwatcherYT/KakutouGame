package kakutou;

import java.awt.Container;
import javax.swing.*;
import java.awt.event.*;

public class Kakutou extends JFrame implements KeyListener {

    MainPanel mainpanel;

    public static void main(String[] args) {
        new Kakutou();
    }

    public Kakutou() {
        super("Ši“¬");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainpanel = new MainPanel();
        Container container = getContentPane();
        container.add(mainpanel);
        addKeyListener(this);
        pack();
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((mainpanel.gameover && e.getKeyCode() != KeyEvent.VK_ENTER)
                || (!mainpanel.gameover && mainpanel.startCount!=0)) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                mainpanel.player.left();
                break;
            case KeyEvent.VK_RIGHT:
                mainpanel.player.right();
                break;
            case KeyEvent.VK_DOWN:
                mainpanel.player.sit();
                break;
            case KeyEvent.VK_UP:
                mainpanel.player.jump();
                break;
            case KeyEvent.VK_A:
                mainpanel.player.punch();
                break;
            case KeyEvent.VK_S:
                mainpanel.player.kick();
                break;
            case KeyEvent.VK_D:
                mainpanel.player.hadou();
                break;
            case KeyEvent.VK_F:
                mainpanel.player.shouryu();
                break;
            case KeyEvent.VK_G:
                mainpanel.player.tatsumaki();
                break;
            case KeyEvent.VK_SPACE:
                mainpanel.player.ultra();
                break;
            case KeyEvent.VK_ENTER:
                mainpanel.init();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (mainpanel.gameover) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                mainpanel.player.notLeft();
                break;
            case KeyEvent.VK_RIGHT:
                mainpanel.player.notRight();
                break;
            case KeyEvent.VK_DOWN:
                mainpanel.player.notSit();
                break;
        }
    }
}

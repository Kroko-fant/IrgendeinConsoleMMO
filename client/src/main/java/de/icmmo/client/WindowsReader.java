package de.icmmo.client;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WindowsReader extends Reader {

    private class MainFrame extends JFrame implements KeyListener{


        public void keyPressed(KeyEvent e) {
            System.out.println("keyPressed");
        }

        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode()== KeyEvent.VK_RIGHT)
                System.out.println("Right");
            else if(e.getKeyCode()== KeyEvent.VK_LEFT)
                System.out.println("Left");
            else if(e.getKeyCode()== KeyEvent.VK_DOWN)
                System.out.println("down");
            else if(e.getKeyCode()== KeyEvent.VK_UP)
                System.out.println("up");

        }
        public void keyTyped(KeyEvent e) {
            System.out.println("keyTyped");
        }

        public MainFrame(){
            addKeyListener(this);
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
        }

       /*public static void main(String[] args) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mainFrame frame = new mainFrame();
                    frame.setTitle("Square Move Practice");
                    frame.setResizable(false);
                    frame.setSize(600, 600);
                    frame.setMinimumSize(new Dimension(600, 600));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(frame.draw);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
        }*/

    }

    public WindowsReader() {
    }

    @Override
    public char readNextChar() {


        return 0;
    }
}

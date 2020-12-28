package de.icmmo.client;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WindowsReader extends Reader {



    private final JFrame frame;
    ConcurrentLinkedQueue<Character> blockingQueue = new ConcurrentLinkedQueue<Character>();
    public WindowsReader() {
        this.frame = new JFrame();
        synchronized (this.frame) {
            this.frame.setUndecorated(true);
            this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            this.frame.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    synchronized (frame) {
                        blockingQueue.add(e.getKeyChar());
                    }
                }
                public void keyReleased(KeyEvent e) {
                }
                public void keyTyped(KeyEvent e) {
                }
            });
            frame.setVisible(true);
        }
    }

    @Override
    public char readNextChar() {
        while (blockingQueue.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return blockingQueue.remove();
    }

    @Override
    public void end() {
        frame.removeKeyListener(frame.getKeyListeners()[0]);
        frame.dispose();
    }
}

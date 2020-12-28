package de.icmmo.client.ui;

import java.util.Arrays;

public class MainWindow extends Window {

    protected MainWindow(int width, int height) {
        super(new Rectangle(0, 0, width, height));
    }

    @Override
    protected void initWindow() {
        //fill header
        Arrays.fill(drawnImage[0], '-');

        Arrays.fill(drawnImage[dimensions.getHeight() - 1], '-');
    }

    public void draw() {
        repaint(0, 0, dimensions.getWidth(), dimensions.getHeight(), drawnImage);
        // print window
        for (int i = 0; i < dimensions.getHeight(); ++i) {
            System.out.println(drawnImage[i]);
        }
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow(60, 15);
        window.draw();
    }

}

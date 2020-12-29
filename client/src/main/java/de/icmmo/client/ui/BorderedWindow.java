package de.icmmo.client.ui;

import java.util.Arrays;

public class BorderedWindow extends Window {

    protected BorderedWindow(Rectangle dimensions) {
        super(dimensions);
    }

    @Override
    protected void initWindow() {
        Arrays.fill(drawnImage[0], '\u2550');
        Arrays.fill(drawnImage[dimensions.getHeight() - 1], '\u2550');
        for (int i = 0; i < dimensions.getHeight(); ++i) {
            drawnImage[i][0] = '\u2551';
            drawnImage[i][dimensions.getWidth() - 1] = '\u2551';
        }
        drawnImage[0][0] = '\u2554';
        drawnImage[0][dimensions.getWidth() - 1] = '\u2557';
        drawnImage[dimensions.getHeight() - 1][0] = '\u255a';
        drawnImage[dimensions.getHeight() - 1][dimensions.getWidth() - 1] = '\u255d';
    }
}

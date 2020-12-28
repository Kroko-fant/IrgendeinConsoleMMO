package de.icmmo.client.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Window {

    protected final Rectangle dimensions;
    protected final List<Window> children;
    // The window drawn on repaint is stored in here
    protected final char[][] drawnImage;

    protected Window(Rectangle dimensions) {
        this.dimensions = dimensions;
        children = new LinkedList<>();
        drawnImage = new char[dimensions.getHeight()][dimensions.getWidth()];
        for (int i = 0; i < drawnImage.length; ++i) {
            Arrays.fill(drawnImage[i], ' ');
        }
        initWindow();
    }

    /**
     * Initial paint of window
     */
    protected abstract void initWindow();

    /**
     * @param offsetX   the total x offset of this window in the buffer
     * @param offsetY   the total y offset of this window in the buffer
     * @param width     the width of this object (x + width < buffer width)
     * @param height    the height of this object (y + height < buffer height)
     * @param buffer    the main buffer where all chars are stored in
     */
    protected void repaint(int offsetX, int offsetY, int width, int height, char[][] buffer) {
        // Paint the window into buffer
        final int posX = offsetX + dimensions.getX();
        final int posY = offsetY + dimensions.getY();
        final int drawWidth = Math.min(width - dimensions.getX(), this.dimensions.getWidth());
        final int drawHeight = Math.min(height - dimensions.getY(), this.dimensions.getHeight());

        for (int i = posY; i < drawHeight + posY; i++) {
            System.arraycopy(drawnImage[i - posY], 0, buffer[i], posX, drawWidth);
        }
        // Paint children
        children.forEach(c -> c.repaint(posX, posY, drawWidth, drawHeight, buffer));
    }
}

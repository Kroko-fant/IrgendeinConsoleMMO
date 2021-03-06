package de.icmmo.client.ui;

import de.icmmo.client.Client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Window {

    protected final Rectangle dimensions;
    protected final List<Window> children;
    // The window drawn on repaint is stored in here
    protected final char[][] drawnImage;
    protected boolean enabled = true;
    private Runnable closeCallback = null;

    protected Window(Rectangle dimensions) {
        this.dimensions = dimensions;
        children = new LinkedList<>();
        drawnImage = new char[dimensions.getHeight()][dimensions.getWidth()];
        for (char[] chars : drawnImage) {
            Arrays.fill(chars, ' ');
        }
    }

    public void close() {
        this.enabled = false;
        if (closeCallback != null) closeCallback.run();
        for (Window child : children) {
            child.close();
        }
    }

    /**
     * The parent window may register to get notified when window closes
     */
    public void setCloseCallback(Runnable closeCallback) {
        this.closeCallback = closeCallback;
    }

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
        children.forEach(c -> {
            if (c.enabled) c.repaint(posX, posY, drawWidth, drawHeight, buffer);
        });
    }

    protected void writeText(int x, int y, String text) {
        if (text == null) return;
        writeText(x, y, text.length(), text);
    }

    protected void writeText(int x, int y, int maxWidth, String text) {
        if (text == null) return;
        final int width = Math.min(Math.min(text.length(), dimensions.getWidth() - x), maxWidth);
        if (width <= 0) return;
        if (y < 0 || y >= dimensions.getHeight()) return;
        System.arraycopy(text.toCharArray(), 0, drawnImage[y], x, width);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

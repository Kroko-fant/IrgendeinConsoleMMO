package de.icmmo.client.ui;

public class TestWindow extends Window {

    private static final String TEXT = "Hello World! Lol";

    protected TestWindow(int x, int y) {
        super(new Rectangle(x, y, TEXT.length(), 1));
    }

    @Override
    protected void initWindow() {
        System.arraycopy(TEXT.toCharArray(), 0, drawnImage[0], 0, dimensions.getWidth());
    }
}

package de.icmmo.client.ui;

import de.icmmo.client.Client;

import java.util.function.Function;

public class DialogChoiceWindow extends Window {

    private final Function<Integer, Window> windowGenerator;
    private final MultiChoiceWindow multiChoiceWindow;

    /**
     * @param windowGenerator returns a newly created window. When null it closes closes this window (used for exit option)
     */
    public DialogChoiceWindow(Rectangle dimensions, Client client, String[] choices, Function<Integer, Window> windowGenerator) {
        super(dimensions);
        multiChoiceWindow = new MultiChoiceWindow(new Rectangle(0, 0, dimensions.getWidth(), dimensions.getHeight()), client, choices);
        multiChoiceWindow.setCallback(this::openWindowAction);
        children.add(multiChoiceWindow);
        this.windowGenerator = windowGenerator;
    }

    protected void openWindowAction(int choice) {
        if (children.size() > 1) return;
        multiChoiceWindow.enabled = false;
        final Window win = windowGenerator.apply(choice);
        if (win == null) {
            this.close();
            return;
        }
        children.add(win);
        win.setCloseCallback(() -> {
            children.remove(win);
            multiChoiceWindow.enabled = true;
        });
    }
}

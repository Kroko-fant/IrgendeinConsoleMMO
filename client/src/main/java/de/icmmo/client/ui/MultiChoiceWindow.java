package de.icmmo.client.ui;

import de.icmmo.client.Client;
import de.icmmo.shared.KeyPacket;
import de.icmmo.shared.Packet;

import java.util.function.IntConsumer;

public class MultiChoiceWindow extends Window {

    private final String[] choices;
    private IntConsumer callback = null;
    private int choice = 0;

    protected MultiChoiceWindow(Rectangle dimensions, Client client, String[] choices) {
        super(dimensions);
        this.choices = choices;
        client.addObserver(this::receive);
        writeValues();
        updateWindow();
    }

    private void writeValues() {
        for (int i = 0; i < choices.length; ++i) {
            writeText(3, i, choices[i]);
        }
    }

    private void updateWindow() {
        for (int i = 0; i < choices.length; ++i) {
            writeText(0, i, i == choice ? "->" : "  ");
        }
    }

    public boolean receive(Packet value) {
        try {
            return switch (value.getType()) {
                case KEY_INPUT -> handleKeyInput(((KeyPacket) value).getKey());
                default -> false;
            };
        } catch (ClassCastException ignored) {
            return false;
        }
    }

    public void setCallback(IntConsumer callback) {
        this.callback = callback;
    }

    private boolean handleKeyInput(char c) {
        if (choices.length == 0 || !enabled) return false;
        return switch (c) {
            case 's' -> {
                // Move down
                choice = (choice + 1) % choices.length;
                updateWindow();
                yield true;
            }
            case 'w'-> {
                // Move up
                choice = (choice - 1 + choices.length) % choices.length;
                updateWindow();
                yield true;
            }
            case ' ' -> {
                // Confirm
                if (callback != null) callback.accept(choice);
                yield true;
            }
            default -> false;
        };
    }

}

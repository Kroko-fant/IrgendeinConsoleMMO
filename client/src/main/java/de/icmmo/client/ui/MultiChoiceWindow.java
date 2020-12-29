package de.icmmo.client.ui;

import de.icmmo.client.Client;
import de.icmmo.shared.KeyPacket;
import de.icmmo.shared.Packet;

import java.util.function.IntConsumer;

public class MultiChoiceWindow extends Window {

    private final String[] choices;
    private final IntConsumer callback;
    private int choice = 0;

    protected MultiChoiceWindow(Rectangle dimensions, Client client, String[] choices, IntConsumer callback) {
        super(dimensions);
        this.choices = choices;
        this.callback = callback;
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

    private boolean handleKeyInput(char c) {
        if (choices.length == 0) return false;
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
                callback.accept(choice);
                yield false;
            }
            default -> false;
        };
    }

    @Override
    protected void initWindow() { }

}

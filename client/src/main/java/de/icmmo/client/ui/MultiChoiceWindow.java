package de.icmmo.client.ui;

import de.icmmo.client.Client;
import de.icmmo.client.observer.Observer;
import de.icmmo.shared.Packet;

import java.util.List;

public class MultiChoiceWindow extends Window implements Observer<Packet> {

    private final List<Choice> choices;
    private int choice = 0;

    protected MultiChoiceWindow(Rectangle dimensions, Client client, List<Choice> choices) {
        super(dimensions);
        this.choices = choices;
    }

    @Override
    protected void initWindow() {

    }

    private void updateWindow() {

    }

    @Override
    public boolean receive(Packet value) {
        return switch (value.getType()) {
            case KEY_INPUT -> {
                yield true;
            }
            default -> false;
        };
    }

    public abstract static class Choice implements Runnable {

        private final String name;

        public Choice(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

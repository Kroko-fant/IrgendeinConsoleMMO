package de.icmmo.client.observer;

public interface Observer<T> {

    void receive(T value);
}

package de.icmmo.client.observer;

public interface Observer<T> {

    boolean receive(T value);
}

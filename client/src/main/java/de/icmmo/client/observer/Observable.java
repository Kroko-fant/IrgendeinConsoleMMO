package de.icmmo.client.observer;

import java.util.LinkedList;
import java.util.List;

public class Observable<T> {

    private final List<WeakObserver<T>> observer = new LinkedList<>();

    public void addObserver(Observer<T> o) {
        observer.add(new WeakObserver<>(o));
    }

    public void handle(T value) {
        observer.forEach(o -> o.receive(value));
        observer.removeIf(o -> o.get() == null);
    }
}

package de.icmmo.client.observer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Observable<T> {

    private final ConcurrentLinkedQueue<WeakObserver<T>> observer = new ConcurrentLinkedQueue<>();

    public void addObserver(Observer<T> o) {
        observer.add(new WeakObserver<>(o));
    }

    public boolean handle(T value) {
        boolean action = false;
        for (Observer<T> o : observer) {
            if (o.receive(value)) action = true;
        }
        observer.removeIf(o -> o.get() == null);
        return action;
    }
}

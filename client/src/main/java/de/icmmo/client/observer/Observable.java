package de.icmmo.client.observer;

import java.util.LinkedList;
import java.util.List;

public class Observable<T> {

    private final List<WeakObserver<T>> observer = new LinkedList<>();

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

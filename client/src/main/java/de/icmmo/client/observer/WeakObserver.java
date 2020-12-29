package de.icmmo.client.observer;

import java.lang.ref.WeakReference;

public class WeakObserver<T> extends WeakReference<Observer<T>> implements Observer<T> {

    public WeakObserver(Observer<T> referent) {
        super(referent);
    }

    @Override
    public boolean receive(T value) {
        Observer<T> observer = super.get();
        if (observer != null) {
            return observer.receive(value);
        }
        return false;
    }
}

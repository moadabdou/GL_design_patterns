package com.glproject.behavioral;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DocumentEventBus {

    private final List<DocumentObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(DocumentObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void unsubscribe(DocumentObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(DocumentEvent event, Object data) {
        for (DocumentObserver observer : observers) {
            observer.onEvent(event, data);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}

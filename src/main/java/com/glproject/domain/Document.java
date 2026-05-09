package com.glproject.domain;

import com.glproject.behavioral.DocumentEvent;
import com.glproject.behavioral.DocumentEventBus;

import java.util.*;

public class Document implements Iterable<Element> {

    private final List<Element> elements = new ArrayList<>();
    private String title;
    private DocumentEventBus eventBus = new DocumentEventBus();

    public Document(String title) {
        this.title = title;
    }

    public Document(String title, DocumentEventBus eventBus) {
        this.title = title;
        this.eventBus = eventBus;
    }

    public void setEventBus(DocumentEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public DocumentEventBus getEventBus() {
        return eventBus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addElement(Element element) {
        elements.add(element);
        eventBus.notifyObservers(DocumentEvent.ELEMENT_ADDED, element);
    }

    public void addElement(int index, Element element) {
        elements.add(index, element);
        eventBus.notifyObservers(DocumentEvent.ELEMENT_ADDED, element);
    }

    public boolean removeElement(Element element) {
        boolean removed = elements.remove(element);
        if (removed) {
            eventBus.notifyObservers(DocumentEvent.ELEMENT_REMOVED, element);
        }
        return removed;
    }

    public Element removeElement(int index) {
        Element removed = elements.remove(index);
        eventBus.notifyObservers(DocumentEvent.ELEMENT_REMOVED, removed);
        return removed;
    }

    public Element setElement(int index, Element element) {
        Element old = elements.set(index, element);
        eventBus.notifyObservers(DocumentEvent.ELEMENT_MODIFIED, element);
        return old;
    }

    public Element getElement(int index) {
        return elements.get(index);
    }

    public int getElementCount() {
        return elements.size();
    }

    @Override
    public Iterator<Element> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }
}

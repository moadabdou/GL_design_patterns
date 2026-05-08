package com.glproject.domain;

import java.util.*;

public class Document implements Iterable<Element> {

    private final List<Element> elements = new ArrayList<>();
    private String title;

    public Document(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public boolean removeElement(Element element) {
        return elements.remove(element);
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

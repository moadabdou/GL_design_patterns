package com.glproject.behavioral;

import com.glproject.domain.Document;
import com.glproject.domain.Element;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class DocumentIterator implements Iterator<Element> {

    private final Document document;
    private final ElementTypeFilter filter;
    private final int expectedModCount;
    private int cursor;

    public DocumentIterator(Document document, ElementTypeFilter filter) {
        this.document = document;
        this.filter = filter;
        this.expectedModCount = document.getModCount();
        this.cursor = advanceToNext(0);
    }

    private int advanceToNext(int start) {
        int count = document.getElementCount();
        int i = start;
        while (i < count) {
            if (filter.matches(document.getElement(i))) {
                return i;
            }
            i++;
        }
        return count;
    }

    @Override
    public boolean hasNext() {
        return cursor < document.getElementCount();
    }

    @Override
    public Element next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (document.getModCount() != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        Element result = document.getElement(cursor);
        cursor = advanceToNext(cursor + 1);
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

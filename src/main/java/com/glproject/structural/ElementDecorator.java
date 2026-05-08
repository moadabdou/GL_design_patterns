package com.glproject.structural;

import com.glproject.domain.Element;

public abstract class ElementDecorator implements Element {

    protected final Element wrapped;

    protected ElementDecorator(Element wrapped) {
        this.wrapped = wrapped;
    }

    public Element getWrapped() {
        return wrapped;
    }

    @Override
    public String render() {
        return wrapped.render();
    }

    @Override
    public Element clone() {
        return wrapped.clone();
    }
}

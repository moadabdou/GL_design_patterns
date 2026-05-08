package com.glproject.structural;

import com.glproject.domain.*;
import com.glproject.creational.DocumentBuilder;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {

    @Test
    void boldDecorator_wrapsRender() {
        Element text = new TextElement("hello");
        Element bold = new BoldDecorator(text);
        assertEquals("<b>hello</b>", bold.render());
    }

    @Test
    void italicDecorator_wrapsRender() {
        Element text = new TextElement("hello");
        Element italic = new ItalicDecorator(text);
        assertEquals("<i>hello</i>", italic.render());
    }

    @Test
    void underlineDecorator_wrapsRender() {
        Element text = new TextElement("hello");
        Element underline = new UnderlineDecorator(text);
        assertEquals("<u>hello</u>", underline.render());
    }

    @Test
    void colorDecorator_wrapsRender() {
        Element text = new TextElement("hello");
        Element colored = new ColorDecorator(text, "red");
        assertEquals("<span style=\"color:red\">hello</span>", colored.render());
    }

    @Test
    void decorators_composeInOrder() {
        Element text = new TextElement("hello");
        Element boldItalic = new BoldDecorator(new ItalicDecorator(text));
        assertEquals("<b><i>hello</i></b>", boldItalic.render());
    }

    @Test
    void decorators_composeReversed() {
        Element text = new TextElement("hello");
        Element italicBold = new ItalicDecorator(new BoldDecorator(text));
        assertEquals("<i><b>hello</b></i>", italicBold.render());
    }

    @Test
    void multipleDecoratorsStack() {
        Element text = new TextElement("hello");
        Element decorated = new BoldDecorator(new ItalicDecorator(new UnderlineDecorator(text)));
        assertEquals("<b><i><u>hello</u></i></b>", decorated.render());
    }

    @Test
    void colorCanBeChainedWithBold() {
        Element text = new TextElement("hello");
        Element decorated = new BoldDecorator(new ColorDecorator(text, "blue"));
        assertEquals("<b><span style=\"color:blue\">hello</span></b>", decorated.render());
    }

    @Test
    void clone_throughSingleDecorator() {
        Element text = new TextElement("hello");
        Element bold = new BoldDecorator(text);
        Element cloned = bold.clone();
        assertEquals(bold.render(), cloned.render());
        assertNotSame(bold, cloned);
    }

    @Test
    void clone_throughDecoratorChain() {
        Element text = new TextElement("hello");
        Element decorated = new BoldDecorator(new ItalicDecorator(new UnderlineDecorator(text)));
        Element cloned = decorated.clone();
        assertEquals(decorated.render(), cloned.render());
        assertNotSame(decorated, cloned);
    }

    @Test
    void clone_deepCopiesWrappedElement() {
        TextElement text = new TextElement("hello");
        Element bold = new BoldDecorator(text);
        Element cloned = bold.clone();

        TextElement innerOriginal = (TextElement) ((ElementDecorator) bold).wrapped;
        TextElement innerCloned = (TextElement) ((ElementDecorator) cloned).wrapped;
        assertNotSame(innerOriginal, innerCloned);
    }

    @Test
    void decoratedElementInDocument() {
        Document doc = new DocumentBuilder()
                .setTitle("Decorated Doc")
                .addParagraph("plain")
                .build();

        Element bold = new BoldDecorator(doc.getElement(0));
        doc.addElement(bold);

        assertEquals(2, doc.getElementCount());
        assertEquals("plain", doc.getElement(0).render());
        assertEquals("<b>plain</b>", doc.getElement(1).render());
    }

    @Test
    void decoratedElementInBuilderChaining() {
        Element text = new TextElement("styled");
        Element boldItalic = new BoldDecorator(new ItalicDecorator(text));

        Document doc = new DocumentBuilder()
                .setTitle("Doc")
                .addParagraph("normal")
                .build();

        doc.addElement(boldItalic);

        assertEquals("<b><i>styled</i></b>", doc.getElement(1).render());
    }

    @Test
    void imageWithDecorator() {
        Element img = new ImageElement("photo.jpg", 100, 200);
        Element decorated = new BoldDecorator(new ColorDecorator(img, "red"));
        assertEquals("<b><span style=\"color:red\">[Image: photo.jpg (100x200)]</span></b>", decorated.render());
    }

    @Test
    void tableWithDecorator() {
        Element table = new TableElement(List.of(List.of("a", "b")));
        Element decorated = new ItalicDecorator(table);
        String expected = "<i>[Table]\n  | a | b |\n</i>";
        assertEquals(expected, decorated.render());
    }
}

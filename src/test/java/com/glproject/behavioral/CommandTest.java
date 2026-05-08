package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void addElementExecuteAddsElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        AddElementCommand cmd = new AddElementCommand(doc, text);

        cmd.execute();

        assertEquals(1, doc.getElementCount());
        assertEquals(text, doc.getElement(0));
    }

    @Test
    void addElementUndoRestoresDocument() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("existing"));
        TextElement text = new TextElement("hello");
        AddElementCommand cmd = new AddElementCommand(doc, text);

        cmd.execute();
        assertEquals(2, doc.getElementCount());

        cmd.undo();
        assertEquals(1, doc.getElementCount());
        assertEquals("existing", ((TextElement) doc.getElement(0)).getContent());
    }

    @Test
    void addElementPreservesInsertionIndex() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("first"));
        doc.addElement(new TextElement("third"));
        TextElement second = new TextElement("second");

        AddElementCommand cmd = new AddElementCommand(doc, second);
        cmd.execute();

        assertEquals(3, doc.getElementCount());
        assertEquals("second", ((TextElement) doc.getElement(2)).getContent());

        cmd.undo();
        assertEquals(2, doc.getElementCount());
        assertEquals("first", ((TextElement) doc.getElement(0)).getContent());
        assertEquals("third", ((TextElement) doc.getElement(1)).getContent());
    }

    @Test
    void removeElementExecuteRemovesElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);
        RemoveElementCommand cmd = new RemoveElementCommand(doc, 0);

        cmd.execute();

        assertEquals(0, doc.getElementCount());
    }

    @Test
    void removeElementUndoRestoresElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);
        RemoveElementCommand cmd = new RemoveElementCommand(doc, 0);

        cmd.execute();
        assertEquals(0, doc.getElementCount());

        cmd.undo();
        assertEquals(1, doc.getElementCount());
        assertEquals("hello", ((TextElement) doc.getElement(0)).getContent());
    }

    @Test
    void removeElementRestoresAtCorrectPosition() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("first"));
        doc.addElement(new TextElement("second"));
        doc.addElement(new TextElement("third"));
        RemoveElementCommand cmd = new RemoveElementCommand(doc, 1);

        cmd.execute();
        assertEquals(2, doc.getElementCount());
        assertEquals("third", ((TextElement) doc.getElement(1)).getContent());

        cmd.undo();
        assertEquals(3, doc.getElementCount());
        assertEquals("second", ((TextElement) doc.getElement(1)).getContent());
    }

    @Test
    void modifyStyleExecuteChangesElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);
        Element bold = new BoldDecorator(text);

        ModifyStyleCommand cmd = new ModifyStyleCommand(doc, 0, text, bold);
        cmd.execute();

        assertInstanceOf(BoldDecorator.class, doc.getElement(0));
    }

    @Test
    void modifyStyleUndoRevertsElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);
        Element bold = new BoldDecorator(text);

        ModifyStyleCommand cmd = new ModifyStyleCommand(doc, 0, text, bold);
        cmd.execute();
        assertInstanceOf(BoldDecorator.class, doc.getElement(0));

        cmd.undo();
        assertInstanceOf(TextElement.class, doc.getElement(0));
        assertEquals("hello", ((TextElement) doc.getElement(0)).getContent());
    }

    @Test
    void commandHistoryExecuteCommand() {
        Document doc = new Document("Test");
        CommandHistory history = new CommandHistory();
        AddElementCommand cmd = new AddElementCommand(doc, new TextElement("hello"));

        history.executeCommand(cmd);

        assertEquals(1, doc.getElementCount());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void commandHistoryUndo() {
        Document doc = new Document("Test");
        CommandHistory history = new CommandHistory();
        AddElementCommand cmd = new AddElementCommand(doc, new TextElement("hello"));

        history.executeCommand(cmd);
        assertEquals(1, doc.getElementCount());

        history.undo();
        assertEquals(0, doc.getElementCount());
        assertFalse(history.canUndo());
        assertTrue(history.canRedo());
    }

    @Test
    void commandHistoryRedo() {
        Document doc = new Document("Test");
        CommandHistory history = new CommandHistory();
        AddElementCommand cmd = new AddElementCommand(doc, new TextElement("hello"));

        history.executeCommand(cmd);
        history.undo();
        assertEquals(0, doc.getElementCount());

        history.redo();
        assertEquals(1, doc.getElementCount());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void newCommandAfterUndoClearsRedoStack() {
        Document doc = new Document("Test");
        CommandHistory history = new CommandHistory();

        history.executeCommand(new AddElementCommand(doc, new TextElement("first")));
        history.undo();
        assertTrue(history.canRedo());

        history.executeCommand(new AddElementCommand(doc, new TextElement("second")));
        assertFalse(history.canRedo());
    }

    @Test
    void multipleUndoRedoInSequence() {
        Document doc = new Document("Test");
        CommandHistory history = new CommandHistory();

        history.executeCommand(new AddElementCommand(doc, new TextElement("a")));
        history.executeCommand(new AddElementCommand(doc, new TextElement("b")));
        history.executeCommand(new AddElementCommand(doc, new TextElement("c")));
        assertEquals(3, doc.getElementCount());

        history.undo();
        assertEquals(2, doc.getElementCount());

        history.undo();
        assertEquals(1, doc.getElementCount());

        history.redo();
        assertEquals(2, doc.getElementCount());

        history.redo();
        assertEquals(3, doc.getElementCount());
    }

    @Test
    void undoOnEmptyHistoryDoesNothing() {
        CommandHistory history = new CommandHistory();

        assertFalse(history.canUndo());
        history.undo();
        assertFalse(history.canUndo());
    }

    @Test
    void redoOnEmptyHistoryDoesNothing() {
        CommandHistory history = new CommandHistory();

        assertFalse(history.canRedo());
        history.redo();
        assertFalse(history.canRedo());
    }

    @Test
    void executeUndoRestoresDocumentState() {
        Document doc = new Document("Test");
        TextElement original = new TextElement("original");
        doc.addElement(original);

        AddElementCommand addCmd = new AddElementCommand(doc, new TextElement("added"));
        addCmd.execute();
        assertEquals(2, doc.getElementCount());
        addCmd.undo();
        assertEquals(1, doc.getElementCount());
        assertEquals("original", ((TextElement) doc.getElement(0)).getContent());

        RemoveElementCommand removeCmd = new RemoveElementCommand(doc, 0);
        removeCmd.execute();
        assertEquals(0, doc.getElementCount());
        removeCmd.undo();
        assertEquals(1, doc.getElementCount());
        assertEquals("original", ((TextElement) doc.getElement(0)).getContent());
    }

    @Test
    void getDescriptionReturnsNonNull() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);

        AddElementCommand addCmd = new AddElementCommand(doc, text);
        addCmd.execute();
        assertNotNull(addCmd.getDescription());

        RemoveElementCommand removeCmd = new RemoveElementCommand(doc, 0);
        removeCmd.execute();
        assertNotNull(removeCmd.getDescription());

        ModifyStyleCommand modifyCmd = new ModifyStyleCommand(doc, 0, text, new BoldDecorator(text));
        modifyCmd.execute();
        assertNotNull(modifyCmd.getDescription());
    }

    @Test
    void addElementWithDocumentAddElementAtEnd() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("c"));

        AddElementCommand cmd = new AddElementCommand(doc, new TextElement("b"));
        cmd.execute();
        assertEquals("b", ((TextElement) doc.getElement(2)).getContent());

        cmd.undo();
        assertEquals(2, doc.getElementCount());
    }

    @Test
    void removeElementCorrectlyStoresRemovedElement() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);

        RemoveElementCommand cmd = new RemoveElementCommand(doc, 0);
        cmd.execute();

        cmd.undo();
        assertSame(text, doc.getElement(0));
    }

    @Test
    void modifyStyleWithDecoratorChain() {
        Document doc = new Document("Test");
        TextElement text = new TextElement("hello");
        doc.addElement(text);
        Element italic = new ItalicDecorator(text);
        Element boldItalic = new BoldDecorator(new ItalicDecorator(text));

        ModifyStyleCommand first = new ModifyStyleCommand(doc, 0, text, italic);
        first.execute();
        assertInstanceOf(ItalicDecorator.class, doc.getElement(0));

        ModifyStyleCommand second = new ModifyStyleCommand(doc, 0, italic, boldItalic);
        second.execute();
        assertInstanceOf(BoldDecorator.class, doc.getElement(0));

        second.undo();
        assertInstanceOf(ItalicDecorator.class, doc.getElement(0));

        first.undo();
        assertInstanceOf(TextElement.class, doc.getElement(0));
    }
}

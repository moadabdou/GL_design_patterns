package com.glproject.behavioral;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}

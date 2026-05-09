package com.glproject.behavioral;

public class ConsoleObserver implements DocumentObserver {

    @Override
    public void onEvent(DocumentEvent event, Object data) {
        System.out.println("[Observer] " + event + ": " + data);
    }
}

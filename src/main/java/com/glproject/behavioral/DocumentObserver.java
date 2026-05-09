package com.glproject.behavioral;

public interface DocumentObserver {
    void onEvent(DocumentEvent event, Object data);
}

# Adapter

## What It Is
Adapter is a structural design pattern that lets incompatible interfaces work together by wrapping one class with another interface.

It is useful when you want to reuse existing code without changing it.

## When to Use It
- You need to integrate a class with a different interface.
- You want to reuse legacy code.
- You want to make two incompatible APIs communicate.

## Java Example
```java
interface MediaPlayer {
    void play(String fileName);
}

class AdvancedMediaPlayer {
    void playMp4(String fileName) {
        System.out.println("Playing MP4: " + fileName);
    }
}

class MediaAdapter implements MediaPlayer {
    private final AdvancedMediaPlayer advancedMediaPlayer = new AdvancedMediaPlayer();

    public void play(String fileName) {
        advancedMediaPlayer.playMp4(fileName);
    }
}
```

## Typical Use Cases
- Wrapping legacy APIs
- Converting one data format to another
- Connecting third-party libraries to your codebase

## Benefits
- Reuses existing code
- Keeps client code simple
- Isolates interface differences

## Tradeoffs
- Adds an extra layer of indirection
- Can make the code harder to trace if overused

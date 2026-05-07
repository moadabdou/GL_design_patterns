# Proxy

## What It Is
Proxy is a structural design pattern that provides a placeholder or surrogate for another object to control access to it.

It is useful when you want to add control, lazy loading, or security around an object.

## When to Use It
- You want to delay expensive object creation.
- You need access control or logging.
- You want to wrap a remote, virtual, or protected resource.

## Java Example
```java
interface Image {
    void display();
}

class RealImage implements Image {
    public void display() {
        System.out.println("Showing real image");
    }
}

class ImageProxy implements Image {
    private RealImage realImage;

    public void display() {
        if (realImage == null) {
            realImage = new RealImage();
        }
        realImage.display();
    }
}
```

## Typical Use Cases
- Lazy loading
- Access control
- Remote service access
- Caching expensive operations

## Benefits
- Controls access to the real object
- Can improve performance with lazy initialization
- Adds security or logging transparently

## Tradeoffs
- Adds indirection
- Can complicate debugging and object flow

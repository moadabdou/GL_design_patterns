# Composite

## What It Is
Composite is a structural design pattern that composes objects into tree structures to represent part-whole hierarchies.

It lets clients treat individual objects and groups of objects uniformly.

## When to Use It
- You need to model tree-like structures.
- You want to treat single items and collections the same way.
- You want to build recursive object hierarchies.

## Java Example
```java
interface FileSystemComponent {
    void showDetails();
}

class FileItem implements FileSystemComponent {
    private final String name;

    FileItem(String name) {
        this.name = name;
    }

    public void showDetails() {
        System.out.println("File: " + name);
    }
}

class Folder implements FileSystemComponent {
    private final String name;
    private final java.util.List<FileSystemComponent> children = new java.util.ArrayList<>();

    Folder(String name) {
        this.name = name;
    }

    void add(FileSystemComponent component) {
        children.add(component);
    }

    public void showDetails() {
        System.out.println("Folder: " + name);
        for (FileSystemComponent child : children) {
            child.showDetails();
        }
    }
}

// Usage:
// Folder root = new Folder("root");
// root.add(new FileItem("notes.txt"));
//
// Folder images = new Folder("images");
// images.add(new FileItem("photo.png"));
// root.add(images);
//
// root.showDetails();
```

## Typical Use Cases
- File systems
- Organization charts
- GUI component trees
- Menu structures

## Benefits
- Simplifies client code
- Makes recursive structures easy to manage
- Supports uniform treatment of leaves and containers

## Tradeoffs
- Can make it harder to restrict what belongs in a composite
- Operations may be less efficient for large trees

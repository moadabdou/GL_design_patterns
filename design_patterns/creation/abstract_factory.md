# Abstract Factory

## What It Is
Abstract Factory is a creational design pattern that provides an interface for creating families of related objects without specifying their concrete classes.

Use it when you want to ensure that objects from the same family are created together and stay compatible with each other.

## When to Use It
- You need to create related objects that must work together.
- You want to hide the concrete classes from the client code.
- You want to switch entire product families without changing the client logic.

## Java Example
```java
interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}

class WindowsButton implements Button {
    public void paint() {
        System.out.println("Rendering a Windows button");
    }
}

class WindowsCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering a Windows checkbox");
    }
}

class MacButton implements Button {
    public void paint() {
        System.out.println("Rendering a Mac button");
    }
}

class MacCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering a Mac checkbox");
    }
}

interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsFactory implements GUIFactory {
    public Button createButton() {
        return new WindowsButton();
    }

    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacFactory implements GUIFactory {
    public Button createButton() {
        return new MacButton();
    }

    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}
```

## Typical Use Cases
- Cross-platform UI toolkits
- Theme-based component creation
- Database drivers for different vendors
- Game engines creating compatible assets for the same platform

## Benefits
- Keeps related objects consistent
- Makes switching product families easier
- Reduces direct dependency on concrete classes

## Tradeoffs
- Adding a new product type can require changes to every factory
- The code can become more complex than simpler factory patterns

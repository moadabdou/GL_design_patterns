# Abstract Factory

## What It Is
Abstract Factory is a creational design pattern that provides an interface for creating families of related objects without specifying their concrete classes.

It is useful when you need to create multiple objects that must be compatible with each other and belong to the same family, such as a Windows UI set or a Mac UI set.

The main idea is to move the choice of concrete classes out of client code and into a factory object.

## When to Use It
- You need to create related objects that must work together.
- You want to hide the concrete classes from client code.
- You want to switch entire product families without changing the client logic.
- You want to prevent clients from mixing incompatible products from different families.
- You expect to add new product families more often than new product types.

## Problem It Solves
Without Abstract Factory, client code often creates concrete classes directly:

```java
Button button = new WindowsButton();
Checkbox checkbox = new MacCheckbox();
```

This creates several problems:
- The client knows too much about concrete classes.
- It becomes easy to mix incompatible objects.
- Switching from one family to another requires editing client code.
- Object creation logic gets scattered across the codebase.

## Solution: Abstract Factory
Create one factory interface that defines methods for a whole family of products:

```java
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
```

Then provide one concrete factory per family:

```java
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

The client depends only on the abstraction, not the concrete product classes.

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

class Application {
    private final Button button;
    private final Checkbox checkbox;

    Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }

    void render() {
        button.paint();
        checkbox.paint();
    }
}
```

## Example Usage
```java
public class Main {
    public static void main(String[] args) {
        GUIFactory factory = new WindowsFactory();
        Application app = new Application(factory);
        app.render();
    }
}
```

If you later want a Mac version, you only change the factory:

```java
GUIFactory factory = new MacFactory();
```

## Real-World Analogy
Think of a furniture store that sells complete room sets:
- a modern set includes a modern chair, modern table, and modern lamp
- a classic set includes a classic chair, classic table, and classic lamp

You do not want the customer to mix a modern table with a classic lamp if they are meant to match. Abstract Factory ensures the whole set comes from one family.

## Key Components
1. **Abstract Factory** - Declares creation methods for related products.
2. **Concrete Factory** - Implements the creation methods for one product family.
3. **Abstract Products** - Interfaces for each product type.
4. **Concrete Products** - Family-specific implementations of those products.
5. **Client** - Uses only the abstract factory and abstract products.

## Typical Use Cases
- Cross-platform UI toolkits
- Theme-based component creation
- Database drivers for different vendors
- Game engines creating compatible assets for the same platform
- Document generation systems creating matching export components
- Cloud platform abstraction layers

## Benefits
- Keeps related objects consistent.
- Makes switching product families easier.
- Reduces direct dependency on concrete classes.
- Improves maintainability when the family changes as a unit.
- Keeps client code cleaner and more reusable.

## Tradeoffs
- Adding a new product type can require changes to every factory.
- The code can become more complex than simpler factory patterns.
- It may be overkill if you only need one or two object types.

## Comparison with Other Factory Patterns
| Pattern | Focus | When to Use |
|---------|-------|------------|
| **Factory Method** | Create one product through inheritance | One product type, subclasses decide the concrete class |
| **Abstract Factory** | Create families of related products | Multiple related products must stay compatible |
| **Simple Factory** | Centralize object creation in one class | Quick and simple creation logic, no strict pattern structure |

## Common Pitfalls
1. **Too many families** - If you only have one family, Abstract Factory may be unnecessary.
2. **Too many product types** - Adding a new product type means touching every factory.
3. **Leaking concrete classes** - If the client starts using `new WindowsButton()` directly, you lose most of the benefit.

## Implementation Guidelines
- Keep product interfaces small and focused.
- Use one factory per family, not one factory per product.
- Keep client code dependent on abstractions only.
- Use the pattern when you know products must remain compatible.
- Prefer it when you expect to swap whole families at runtime or during configuration.

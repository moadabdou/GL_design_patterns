# Document Builder - Design Patterns Demo

## Project Goal
This project is a minimal, focused demonstration of applying 10 classic Design Patterns (Creational, Structural, and Behavioral) in a cohesive software application. Built in Java, it serves as a practical showcase for how design patterns seamlessly solve common software engineering pain points.

## Overview
The application is a simple Document/Page Builder that allows users to:
- Construct complex documents step-by-step.
- Instantiate different elements (Text, Images, Tables).
- Apply dynamic formatting to elements (Bold, Italics).
- Exute and undo/redo modifications on the document.
- Export the resulting document to various formats (HTML, PDF, Markdown).
- Observe application events (like export completion or document modification).

## Implemented Design Patterns
1. **Builder** (Creational) - Constructs complex Document objects step by step.
2. **Factory Method** (Creational) - Instantiates different types of Elements.
3. **Singleton** (Creational) - Manages global Configuration and Logging.
4. **Adapter** (Structural) - Adapts a hypothetical 3rd-party library to our Exporter interface.
5. **Decorator** (Structural) - Dynamically adds formatting styles to text elements.
6. **Facade** (Structural) - Provides a simple interface to the complex exporting subsystem.
7. **Strategy** (Behavioral) - Defines interchangeable export algorithms (PDF, HTML, etc.).
8. **Observer** (Behavioral) - Notifies listeners of document changes or system events.
9. **Command** (Behavioral) - Encapsulates operations to support Undo/Redo functionality.
10. **Iterator** (Behavioral) - Provides a standard way to traverse document elements securely.

## Getting Started
*(Instructions for compiling and running the Java application will go here once the codebase is generated.)*

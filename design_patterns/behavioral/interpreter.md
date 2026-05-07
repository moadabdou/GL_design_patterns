# Interpreter

## What It Is
Interpreter is a behavioral pattern that defines a grammar and evaluates sentences in that grammar.

It is useful for simple languages or expression evaluation.

## When to Use It
- You need to interpret a simple language.
- The grammar is stable and not too large.
- You want to represent expressions as objects.

## Java Example
```java
interface Expression {
    int interpret();
}

class NumberExpression implements Expression {
    private final int value;

    NumberExpression(int value) {
        this.value = value;
    }

    public int interpret() {
        return value;
    }
}
```

## Typical Use Cases
- Rule engines
- Mathematical expressions
- Simple query languages
- Configuration parsers

## Benefits
- Models grammar explicitly
- Easy to extend for small languages
- Makes expression trees readable

## Tradeoffs
- Can become complex for large grammars
- Many classes may be needed

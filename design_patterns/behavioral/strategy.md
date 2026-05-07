# Strategy

## What It Is
Strategy is a behavioral pattern that defines a family of algorithms, encapsulates each one, and makes them interchangeable.

It lets the algorithm vary independently from the client that uses it.

## When to Use It
- You need different ways to perform the same task.
- You want to switch algorithms at runtime.
- You want to avoid large conditional blocks.

## Java Example
```java
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid by card: " + amount);
    }
}
```

## Typical Use Cases
- Payment methods
- Sorting algorithms
- Compression algorithms
- Pricing or discount rules

## Benefits
- Easy to swap algorithms
- Keeps code open for extension
- Reduces conditional logic

## Tradeoffs
- More classes to manage
- Clients must know which strategy to choose

# Singleton

## What It Is
Singleton is a creational design pattern that ensures a class has only one instance and provides a global point of access to that instance.

It is often used for shared resources that should not be duplicated.

## When to Use It
- You need exactly one shared instance across the application.
- You want controlled access to a resource.
- You want to avoid creating multiple costly objects.

## Java Example
```java
class ConfigurationManager {
    private static ConfigurationManager instance;

    private ConfigurationManager() {
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public String getSetting(String key) {
        return "value-for-" + key;
    }
}

// Usage:
// ConfigurationManager config = ConfigurationManager.getInstance();
// System.out.println(config.getSetting("app.name"));
```

## Typical Use Cases
- Application configuration
- Logging services
- Cache managers
- Thread pools or connection managers when a single shared entry point is needed

## Benefits
- Guarantees a single instance
- Provides a global access point
- Can reduce repeated initialization costs

## Tradeoffs
- Can make testing harder because of global state
- May hide dependencies
- Can become a design shortcut when a normal object would be better

# Synchronized and Volatile

Both `synchronized` and `volatile` are used in multithreaded programming to ensure proper visibility and consistency of shared data among multiple threads.

### `synchronized`
`synchronized` is used to provide mutual exclusion and ensure thread safety in a multithreaded environment. When a method or block is synchronized, it can only be executed by one thread at a time. This ensures that only one thread is accessing the shared resource at any given time, preventing race conditions and other synchronization issues. For example:

```java
public synchronized void incrementCounter() {
    counter++;
}
```

In the above example, the `incrementCounter()` method is synchronized, which ensures that only one thread can execute this method at any given time, preventing multiple threads from accessing and updating the `counter` variable simultaneously.

### `volatile`
`volatile` is used to ensure visibility of shared variables across multiple threads. When a variable is declared as `volatile`, any write to that variable will immediately become visible to all other threads that access that variable. **This prevents thread-local caching of variables and ensures that all threads see the most up-to-date value of the variable.** For example:

```java
public class SharedData {
    private volatile int counter = 0;

    public void incrementCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
```

In the above example, the `counter` variable is declared as `volatile`, which ensures that any updates to `counter` made by one thread are immediately visible to all other threads that access `counter`. This ensures that all threads are working with the most up-to-date value of `counter`.
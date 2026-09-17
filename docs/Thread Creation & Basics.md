# Thread Creation & Fundamentals

In Java, a thread is an independent path of execution within a program. Multithreading allows concurrent execution of two or more parts of a program for maximum CPU utilization.

---

## 1. Mechanisms for Thread Creation

There are two primary ways to create a thread in Java:

### Approach A: Implementing the `Runnable` Interface (Recommended)

Implementing `Runnable` is the standard and preferred approach because:
- Java supports single inheritance of classes; implementing `Runnable` leaves your class free to extend other classes.
- It decouples the task (`Runnable`) from the thread execution mechanism (`Thread`).

```java
public class RunnableExample {

    public static void main(String[] args) {
        // 1. Implementing Runnable via class
        Runnable task = new CustomTask();
        Thread thread1 = new Thread(task, "Worker-1");
        thread1.start();

        // 2. Implementing Runnable via Lambda expression
        Thread thread2 = new Thread(() -> {
            System.out.println("Lambda task executing in: " + Thread.currentThread().getName());
        }, "Worker-2");
        thread2.start();
    }
}

class CustomTask implements Runnable {
    @Override
    public void run() {
        System.out.println("CustomTask executing in: " + Thread.currentThread().getName());
    }
}
```

### Approach B: Extending the `Thread` Class

Directly subclass `Thread` and override its `run()` method:

```java
public class ExtendedThreadExample {

    public static void main(String[] args) {
        CustomThread thread = new CustomThread();
        thread.setName("CustomThread-Instance");
        thread.start();
    }
}

class CustomThread extends Thread {
    @Override
    public void run() {
        System.out.println("CustomThread running in: " + Thread.currentThread().getName());
    }
}
```

---

## 2. Critical Pitfall: `start()` vs. `run()`

Calling `run()` directly vs calling `start()` is the single most common multithreading bug:

```java
Thread thread = new Thread(() -> {
    System.out.println("Executed by: " + Thread.currentThread().getName());
}, "Worker-Thread");

// ❌ WRONG: Executes synchronously on the CALLING thread (e.g. main thread)
// NO new OS thread is spawned!
thread.run(); 

// ✅ CORRECT: Asks the JVM/OS to allocate a new call stack and start a new thread
thread.start();
```

### Key Differences

| Feature | `thread.start()` | `thread.run()` |
|---|---|---|
| **New Thread Created?** | Yes, allocates new call stack | No, runs on current thread |
| **Execution** | Asynchronous / Concurrent | Synchronous / Blocking |
| **Can call multiple times?** | No (`IllegalThreadStateException`) | Yes (just a normal method call) |

> **Rule**: Never invoke `run()` manually to start background work. Always call `start()`.

---

## 3. Concurrent Execution & Thread Scheduling

When multiple threads are started, their execution order is determined by the Operating System's thread scheduler and is non-deterministic:

```java
public class ConcurrentExecutionDemo {

    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + " -> count: " + i);
            }
        };

        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        Thread t3 = new Thread(task, "Thread-C");

        // All threads run concurrently; output lines interleave unpredictably
        t1.start();
        t2.start();
        t3.start();
    }
}
```

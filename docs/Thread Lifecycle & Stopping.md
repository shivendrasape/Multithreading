# Thread Lifecycle, Sleep & Cooperative Stopping

Understanding how threads transition through states and how to control or terminate them safely is vital for robust multithreaded systems.

---

## 1. Thread States Overview

In Java, a thread always exists in one of the states defined in `java.lang.Thread.State`:
1. **`NEW`**: Thread instance created, but `start()` has not been called yet.
2. **`RUNNABLE`**: Thread is actively executing or ready to execute in the JVM (waiting for CPU allocation).
3. **`BLOCKED`**: Thread is waiting to acquire a monitor lock to enter a `synchronized` block/method.
4. **`WAITING`**: Thread is waiting indefinitely for another thread to perform a specific action (e.g., `wait()`, `join()`).
5. **`TIMED_WAITING`**: Thread is waiting for another thread up to a specified timeout (e.g., `Thread.sleep(ms)`, `join(ms)`).
6. **`TERMINATED`**: Thread has completed its execution or threw an unhandled exception.

---

## 2. Pausing Execution: `Thread.sleep()`

`Thread.sleep(millis)` suspends the current thread for the specified duration without relinquishing any locks it currently holds:

```java
public class SleepExample {

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            System.out.println("Worker starting...");
            try {
                // Suspends execution of this worker thread for 1 second
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                // Restore interrupted status if not propagating
                Thread.currentThread().interrupt();
                System.err.println("Worker was interrupted during sleep");
            }
            System.out.println("Worker completed after delay.");
        });

        worker.start();
    }
}
```

### Key Considerations
- `sleep()` is accurate only up to the precision and limits of underlying OS system timers. It should never be used for real-time synchronization.
- Always handle or propagate `InterruptedException`.

---

## 3. Graceful Thread Termination

### Why `Thread.stop()` is Deprecated & Dangerous

Java initially provided `Thread.stop()`, but it was deprecated and strongly discouraged because:
- It terminates the thread abruptly mid-execution.
- It immediately unlocks all monitors/locks held by the thread.
- If an object was in an inconsistent or partially-modified state when locked, other threads will now observe corrupted data without warning.

### The Correct Pattern: Cooperative Cancellation with `volatile`

Threads should stop cooperatively. The standard pattern uses a `volatile boolean` flag checked within the thread's work loop:

```java
public class GracefulShutdownExample {

    public static void main(String[] args) throws InterruptedException {
        StoppableWorker worker = new StoppableWorker();
        Thread thread = new Thread(worker, "Background-Worker");

        thread.start();

        // Let the worker run for 500ms
        Thread.sleep(500L);

        System.out.println("Main thread requesting worker shutdown...");
        worker.stop();

        thread.join();
        System.out.println("Worker successfully terminated.");
    }
}

class StoppableWorker implements Runnable {

    // volatile ensures write by main thread is immediately visible to worker thread
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Worker is doing work...");
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Worker clean-up finished.");
    }

    public void stop() {
        this.running = false;
    }
}
```

### Why `volatile` is Mandatory
Without the `volatile` modifier:
- The compiler or JIT optimizer may optimize the `while (running)` loop into `if (running) while (true)` because it detects no writes to `running` within the thread itself.
- CPU core caches may retain the stale cached value (`true`), preventing the worker thread from ever seeing the update written by the main thread.
- `volatile` inserts memory barriers ensuring all reads and writes go directly through main memory.

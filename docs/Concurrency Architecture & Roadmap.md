# Java Concurrency Architecture & Future Roadmap

Beyond fundamental `Thread` and `synchronized` primitives, Java provides high-level concurrency utilities in `java.util.concurrent` designed for scalability, thread pooling, and high-throughput systems.

---

## 1. Executor Framework & Thread Pools

Creating raw OS threads (`new Thread()`) is expensive because each thread requires ~1MB of stack memory and OS context-switching overhead. The **Executor Framework** decouples task submission from thread lifecycle management.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {

    public static void main(String[] args) throws InterruptedException {
        // Pool of 4 reusable worker threads
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Processing task " + taskId + " on " + Thread.currentThread().getName());
            });
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

---

## 2. Producer-Consumer Pattern (`BlockingQueue`)

The **Producer-Consumer** pattern decouples components that generate work from components that process work, preventing fast producers from overwhelming slow consumers.

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo {

    public static void main(String[] args) {
        // Thread-safe bounded queue
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.put(i); // Blocks automatically if buffer is full
                    System.out.println("Produced item: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take(); // Blocks automatically if buffer is empty
                    System.out.println("Consumed item: " + item);
                    if (item == 10) break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
```

---

## 3. Explicit Locks: `ReentrantLock` & `ReadWriteLock`

Java's `java.util.concurrent.locks` package provides explicit locking with capabilities unavailable in `synchronized`:
- **Timed Lock Acquisition**: `tryLock(timeout, unit)` to prevent deadlock.
- **Fair Ordering**: Optional fairness policies where the longest-waiting thread acquires the lock first.
- **Read/Write Separation**: Multiple concurrent readers allowed while writer maintains exclusive access.

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteResource {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private int data = 0;

    public int read() {
        rwLock.readLock().lock(); // Multiple threads can read concurrently
        try {
            return data;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void write(int value) {
        rwLock.writeLock().lock(); // Exclusive lock
        try {
            this.data = value;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
```

---

## 4. Modern Java Concurrency: Virtual Threads (Project Loom)

Available starting from Java 21 LTS, **Virtual Threads** are lightweight threads managed directly by the Java Virtual Machine rather than the underlying Operating System.

### Why Virtual Threads?
- Standard platform threads map 1:1 to OS threads (costly, limited to thousands).
- Virtual threads are multiplexed onto carrier platform threads. Millions of virtual threads can run concurrently without exhausting OS resources or memory.

```java
public class VirtualThreadExample {

    public static void main(String[] args) throws InterruptedException {
        // Spawning 10,000 virtual threads effortlessly
        for (int i = 0; i < 10_000; i++) {
            final int id = i;
            Thread.ofVirtual().start(() -> {
                // Blocking I/O unmounts virtual thread from carrier thread automatically!
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            });
        }
        System.out.println("Spawned 10,000 virtual threads without memory exhaustion.");
    }
}
```

---

## 5. Microbenchmarking Concurrency with JMH

Microbenchmarking multithreaded code using standard `System.currentTimeMillis()` is error-prone due to:
- JIT warmup and loop unrolling
- Dead-code elimination
- CPU cache line bouncing and false sharing

Always use the **Java Microbenchmark Harness (JMH)** for statistically sound concurrency measurement.

# Java Concurrency & Multithreading — Learning Roadmap

> **How to read this roadmap**
> - Each phase builds on the previous one; complete phases in order.
> - Topics within a phase can generally be studied in parallel unless a dependency arrow (→) is shown.

---

## Phase 1 — Foundations: What is a Thread?

*No prior concurrency knowledge required.*

| # | Topic | Notes |
|---|-------|-------|
| 1.1 | Thread creation — `Runnable` vs extending `Thread` | [Thread Creation & Basics.md](Thread%20Creation%20%26%20Basics.md) |
| 1.2 | `start()` vs `run()` pitfall | Covered in 1.1 |
| 1.3 | Thread naming, daemon threads, thread priorities | `setName()`, `setDaemon()`, `setPriority()` |
| 1.4 | Thread lifecycle & states (`NEW → RUNNABLE → BLOCKED → WAITING → TERMINATED`) | [Thread Lifecycle & Stopping.md](Thread%20Lifecycle%20%26%20Stopping.md) |
| 1.5 | `Thread.sleep()` & `TIMED_WAITING` | Covered in 1.4 |
| 1.6 | Graceful thread stopping — cooperative cancellation with `volatile` flag | Covered in 1.4 |

---

## Phase 2 — Thread Coordination Primitives

*Requires: Phase 1*

| # | Topic | Notes |
|---|-------|-------|
| 2.1 | `Thread.join()` — waiting for completion | [Thread Join Demo.md](Thread%20Join%20Demo.md) |
| 2.2 | Fan-out / Fan-in pattern with raw threads | [Parallel Task Coordination.md](Parallel%20Task%20Coordination.md) |
| 2.3 | `Thread.interrupt()` — interruption protocol & `InterruptedException` | Complement to the `volatile` flag pattern |
| 2.4 | `Object.wait()` / `notify()` / `notifyAll()` — low-level monitor signalling | Classic hand-rolled producer-consumer before `BlockingQueue` |

---

## Phase 3 — Memory Model & Visibility

*Requires: Phase 1*
*(Understanding this phase makes Phase 4 problems obvious rather than mysterious.)*

| # | Topic | Notes |
|---|-------|-------|
| 3.1 | CPU caches, cache coherence & the visibility problem | Root cause analysis in [Race Condition.md](Race%20Condition.md) |
| 3.2 | `volatile` — guarantees & limitations | [Synchronized vs Volatile.md](Synchronized%20vs%20Volatile.md) |
| 3.3 | Java Memory Model (JMM) & happens-before relationship | Explains *why* `synchronized` and `volatile` work |
| 3.4 | Instruction reordering & memory barriers | Compiler/CPU reordering; JMM fence semantics |

---

## Phase 4 — Mutual Exclusion & Synchronization

*Requires: Phase 3*

| # | Topic | Notes |
|---|-------|-------|
| 4.1 | Race conditions — atomicity & the read-modify-write problem | [Race Condition.md](Race%20Condition.md) |
| 4.2 | `synchronized` — intrinsic locks, critical sections | Covered in 4.1 |
| 4.3 | `synchronized` vs `volatile` — when to use which | [Synchronized vs Volatile.md](Synchronized%20vs%20Volatile.md) |
| 4.4 | Lock-free atomics — `AtomicInteger`, `AtomicReference`, CAS | Covered in 4.1 |
| 4.5 | Deadlock — four Coffman conditions | [Deadlock Prevention.md](Deadlock%20Prevention.md) |
| 4.6 | Deadlock prevention — strict lock ordering | Covered in 4.5 |
| 4.7 | Livelock & starvation | Closely related to deadlock; completes the safety picture |
| 4.8 | Explicit locks — `ReentrantLock`, `tryLock()`, fairness | Overview in [Race Condition.md](Race%20Condition.md); advanced lock acquisition & fairness |
| 4.9 | `ReadWriteLock` / `ReentrantReadWriteLock` | Separate read/write locks for read-heavy workloads |
| 4.10 | `StampedLock` (optimistic reads) | Java 8+; optimistic read validation, more advanced than `ReadWriteLock` |

---

## Phase 5 — High-Level Concurrency Utilities (`java.util.concurrent`)

*Requires: Phase 4*

| # | Topic | Notes |
|---|-------|-------|
| 5.1 | Executor Framework — `ExecutorService`, `Executors`, thread pools | [Executors Framework & Thread Pools.md](Executors%20Framework%20&%20Thread%20Pools.md) |
| 5.2 | `Callable` vs `Runnable` — returning results from threads | Prerequisite for `Future` (5.3) |
| 5.3 | `Future` & `ExecutorService.submit()` — async result retrieval | Depends on 5.2 |
| 5.4 | `CompletableFuture` — composable async pipelines | Depends on 5.3; modern replacement for `Future` |
| 5.5 | `BlockingQueue` — `ArrayBlockingQueue`, `LinkedBlockingQueue` | Thread-safe bounded/unbounded buffer with blocking `put()`/`take()` |
| 5.6 | Producer-Consumer pattern with `BlockingQueue` | Coordinating producers and consumers without low-level `wait()`/`notify()` |
| 5.7 | `CountDownLatch` — one-time gate / barrier | Common in parallel task coordination |
| 5.8 | `CyclicBarrier` — reusable rendezvous point | Depends on 5.7 for contrast |
| 5.9 | `Semaphore` — resource pool / rate limiting | Useful alongside 5.7 & 5.8 |
| 5.10 | `Phaser` — flexible multi-phase barrier (Java 7+) | Advanced; generalises `CountDownLatch` + `CyclicBarrier` |

---

## Phase 6 — Thread-Safe Collections

*Requires: Phase 4*

| # | Topic | Notes |
|---|-------|-------|
| 6.1 | `ConcurrentHashMap` — segment locking / CAS internals | Most widely-used concurrent map |
| 6.2 | `CopyOnWriteArrayList` / `CopyOnWriteArraySet` | Read-heavy, write-rare use cases |
| 6.3 | `ConcurrentLinkedQueue` / `ConcurrentLinkedDeque` | Lock-free FIFO; contrast with `BlockingQueue` |
| 6.4 | `Collections.synchronizedXxx()` wrappers — pitfalls | Compound operations are still not atomic |

---

## Phase 7 — Fork/Join & Parallel Streams

*Requires: Phase 5*

| # | Topic | Notes |
|---|-------|-------|
| 7.1 | `ForkJoinPool` & `RecursiveTask` / `RecursiveAction` | Work-stealing scheduler; basis for parallel streams |
| 7.2 | Parallel Streams (`stream().parallel()`) | Depends on 7.1 understanding |
| 7.3 | When parallel is slower — overhead, false sharing, spliterator | Critical for real-world use |

---

## Phase 8 — Modern Java: Virtual Threads & Structured Concurrency

*Requires: Phase 5. Java 21+ LTS.*

| # | Topic | Notes |
|---|-------|-------|
| 8.1 | Platform threads vs Virtual Threads — carrier thread model | Lightweight threads managed by JVM; carrier thread scheduling |
| 8.2 | `Thread.ofVirtual()` & virtual thread executor | Spawning virtual threads and `Executors.newVirtualThreadPerTaskExecutor()` |
| 8.3 | Pinning — when virtual threads block the carrier | Key limitation of Project Loom (`synchronized` blocks / native calls) |
| 8.4 | Structured Concurrency (`StructuredTaskScope`) | Java 21 preview → 24 stable; composable child-task scoping |
| 8.5 | Scoped Values (replacing `ThreadLocal` in virtual threads) | Java 21+; immutable, inheritable data across virtual threads |

---

## Phase 9 — Observability, Testing & Benchmarking

*Requires: Phase 5. Can be revisited after each phase.*

| # | Topic | Notes |
|---|-------|-------|
| 9.1 | Thread dumps & deadlock detection (`jstack`, VisualVM) | Essential debugging skill |
| 9.2 | Testing concurrent code — `CountDownLatch` harness, thread weaving | Hard to test; known patterns help |
| 9.3 | Microbenchmarking with JMH | Standard framework for accurate concurrency measurement avoiding JIT/cache pitfalls |
| 9.4 | False sharing & `@Contended` | Cache-line padding for high-performance code |

---

## Suggested Next Topics

1. **`Thread.interrupt()` & interruption protocol** (Phase 2.3) — completes the thread-stopping picture already started.
2. **Java Memory Model & happens-before** (Phase 3.3) — gives a solid mental model for *why* `volatile`/`synchronized` work.
3. **`Callable` + `Future`** (Phase 5.2–5.3) — natural next step after `ExecutorService`.
4. **`CompletableFuture`** (Phase 5.4) — the modern async composition tool used everywhere in production Java.
5. **`CountDownLatch` / `CyclicBarrier` / `Semaphore`** (Phase 5.7–5.9) — common concurrency interview topics.

---

## Implementation Reference

The sections below provide reference implementations and code examples for study.

---

## Executor Framework & Thread Pools

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

## Producer-Consumer Pattern (`BlockingQueue`)

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

## Explicit Locks: `ReentrantLock` & `ReadWriteLock`

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

## Modern Java Concurrency: Virtual Threads (Project Loom)

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

## Microbenchmarking Concurrency with JMH

Microbenchmarking multithreaded code using standard `System.currentTimeMillis()` is error-prone due to:
- JIT warmup and loop unrolling
- Dead-code elimination
- CPU cache line bouncing and false sharing

Always use the **Java Microbenchmark Harness (JMH)** for statistically sound concurrency measurement.

# Race Condition & Thread Synchronization

A **race condition** occurs when multiple threads access and modify shared data **concurrently**, and the **final outcome depends on the non-deterministic timing, scheduling, and interleaving** of execution.

---

## The Problem Demonstrated

Consider a shared `TicketCounter` where multiple threads decrement an `availableTickets` counter concurrently without synchronization:

```java
public class TicketCounter {
    static int availableTickets = 1000;

    public static void main(String[] args) throws InterruptedException {
        // Simulate 2 booking agents selling tickets simultaneously
        Thread agent1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                availableTickets--; // Agent 1 sells 500 tickets
            }
        }, "Agent-1");

        Thread agent2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                availableTickets--; // Agent 2 sells 500 tickets
            }
        }, "Agent-2");

        agent1.start();
        agent2.start();

        agent1.join();
        agent2.join();

        System.out.println("Remaining tickets: " + availableTickets);
    }
}
```

### Observed Behavior
Expected remaining tickets: $1000 - 500 - 500 = 0$.

However, running this code produces inconsistent, incorrect results on every run:
```text
Remaining tickets: 23  
Remaining tickets: 187  
Remaining tickets: 0  
Remaining tickets: 74
```
The program is non-deterministic: sometimes correct, usually wrong, **always unpredictable**.

---

## Root Cause Analysis

![Race Condition](Race%20Condition%20image.png)

A race condition is caused by two fundamental issues:

### 1. Lack of Atomicity (Read-Modify-Write Interleaving)
The decrement operation `availableTickets--` (or increment `count++`) is **not atomic**. At the bytecode and CPU hardware level, it consists of 3 distinct instructions:
1. **Read:** Load current value of `availableTickets` from memory into CPU register.
2. **Modify:** Decrement value in CPU register by 1.
3. **Write:** Store decremented value back into shared memory.

When two threads execute concurrently, their instructions interleave:
- **Thread 1** reads `100`.
- **Thread 2** context-switches in and reads `100`.
- **Thread 1** decrements to `99` and writes back `99`.
- **Thread 2** decrements its stale value to `99` and writes back `99`.
- **Result:** Two tickets were sold, but the counter only decreased by 1 (lost update).

### 2. Lack of Visibility
Each CPU core has its own hardware cache (L1/L2). Without memory barriers, Thread 2 may read a stale cached value long after Thread 1 has written the updated value to main memory.

---

## Solutions to Fix Race Conditions

### Solution 1: Mutual Exclusion via `synchronized`

The `synchronized` keyword enforces that **only one thread can execute the critical section at any given time**. It also establishes a *happens-before* relationship, ensuring memory visibility.

```java
public class TicketCounter {
    // Shared variable — both threads will touch this
    static int availableTickets = 10000;

    // Synchronized method locks on TicketCounter.class
    public synchronized static void sellTicket() {
        if (availableTickets > 0) {
            availableTickets--;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 5000; i++) {
                sellTicket();
            }
        };

        // Simulate 2 booking agents selling tickets simultaneously
        Thread agent1 = new Thread(task, "Agent-1");
        Thread agent2 = new Thread(task, "Agent-2");

        agent1.start();
        agent2.start();

        agent1.join();
        agent2.join();

        // Guaranteed: 10000 - 5000 - 5000 = 0
        System.out.println("Remaining tickets: " + availableTickets);
    }
}
```

> **Key takeaway:** Only ONE thread can execute this block at a time. Everyone else waits. That's exactly what `synchronized` does.

---

### Solution 2: Lock-Free Atomic Variables (`AtomicInteger`)

For scalar counters, locks introduce blocking and context-switch overhead. `java.util.concurrent.atomic.AtomicInteger` provides lock-free, atomic operations backed by low-level CPU **Compare-And-Swap (CAS)** instructions.

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTicketCounter {
    // Shared variable — both threads will touch this
    static AtomicInteger availableTickets = new AtomicInteger(10000);

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 5000; i++) {
                availableTickets.getAndDecrement(); // Atomic decrement operation
            }
        };

        // Simulate 2 booking agents selling tickets simultaneously
        Thread agent1 = new Thread(task, "Agent-1");
        Thread agent2 = new Thread(task, "Agent-2");

        agent1.start();
        agent2.start();

        agent1.join();
        agent2.join();

        // Guaranteed: 10000 - 5000 - 5000 = 0
        System.out.println("Remaining tickets: " + availableTickets.get());
    }
}
```

> **Benefit:** Non-blocking and significantly faster than locks under low-to-medium contention.

---

### Solution 3: Explicit Locks (`ReentrantLock`)

`java.util.concurrent.locks.ReentrantLock` offers explicit lock management with advanced capabilities: timed lock attempts (`tryLock`), interruptible locks, and fairness guarantees.

```java
import java.util.concurrent.locks.ReentrantLock;

public class LockTicketCounter {
    static int availableTickets = 10000;
    static final ReentrantLock lock = new ReentrantLock();

    public static void sellTicket() {
        lock.lock();
        try {
            if (availableTickets > 0) {
                availableTickets--;
            }
        } finally {
            lock.unlock(); // Always release in finally block!
        }
    }
}
```

---

## Comparison: When to Use What

| Mechanism | Blocking? | Performance | Best Used For |
| :--- | :--- | :--- | :--- |
| **`AtomicInteger` / CAS** | Lock-free | Highest | Simple counters, single-variable state updates |
| **`synchronized`** | Blocking | High (JVM-optimized) | Multi-statement critical sections, simple idioms |
| **`ReentrantLock`** | Blocking | High | Advanced locking (`tryLock()`, timeouts, fair queuing) |
# Parallel Task Coordination

In real-world enterprise applications, services often need to execute multiple independent tasks (e.g., querying external APIs, saving database records, sending notification emails) to fulfill a single request. 

Executing these tasks sequentially adds cumulative latency. Multithreading enables **Fan-Out / Fan-In** coordination to execute tasks concurrently, drastically reducing total response time.

---

## 1. Sequential vs. Concurrent Latency

Imagine an order fulfillment workflow with four independent tasks:
1. Save order to database (~500 ms)
2. Send customer confirmation email (~300 ms)
3. Notify delivery partner service (~400 ms)
4. Update analytics dashboard (~200 ms)

- **Sequential Execution Time:** $500 + 300 + 400 + 200 = 1400\text{ ms}$
- **Parallel Execution Time:** $\max(500, 300, 400, 200) \approx 500\text{ ms}$ (nearly **3x faster**)

---

## 2. Implementation: Coordinating Concurrent Tasks via `join()`

The `join()` method instructs the calling thread (e.g., main thread or controller thread) to pause until the target thread finishes execution.

```java
public class OrderProcessingCoordinator {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Order received. Firing background tasks in parallel...\n");

        // 1. Fan-out: Define tasks as independent threads
        Thread dbTask = new Thread(() -> {
            simulateWork(500);
            System.out.println("[DATABASE] Order record persisted (500ms)");
        }, "DB-Thread");

        Thread emailTask = new Thread(() -> {
            simulateWork(300);
            System.out.println("[EMAIL] Confirmation dispatched (300ms)");
        }, "Email-Thread");

        Thread deliveryTask = new Thread(() -> {
            simulateWork(400);
            System.out.println("[DELIVERY] Partner notified (400ms)");
        }, "Delivery-Thread");

        Thread dashboardTask = new Thread(() -> {
            simulateWork(200);
            System.out.println("[DASHBOARD] Metrics updated (200ms)");
        }, "Dashboard-Thread");

        // 2. Start all threads simultaneously
        dbTask.start();
        emailTask.start();
        deliveryTask.start();
        dashboardTask.start();

        // 3. Fan-in: Wait for ALL threads to complete before returning response
        dbTask.join();
        emailTask.join();
        deliveryTask.join();
        dashboardTask.join();

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("\nAll tasks completed successfully! Total elapsed time: " + totalTime + " ms");
    }

    private static void simulateWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## 3. Best Practices for Task Coordination

1. **Avoid unbound thread creation**: In high-throughput production systems, creating bare `new Thread()` instances for every request introduces substantial OS thread allocation overhead. Use an `ExecutorService` thread pool instead.
2. **Handle partial failures**: When coordinating parallel tasks, always establish timeouts and fallbacks if one task stalls or fails.
3. **Always preserve interrupted status**: If a thread catches `InterruptedException`, it should either rethrow it or invoke `Thread.currentThread().interrupt()` to restore the flag.

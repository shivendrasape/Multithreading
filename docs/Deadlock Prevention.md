# Deadlock Analysis & Prevention

A **deadlock** is a state where two or more threads are blocked forever, each waiting for a lock or resource held by another thread in the group.

---

## 1. The Four Necessary Conditions for Deadlock

Deadlock can occur only if all four **Coffman conditions** hold simultaneously:

1. **Mutual Exclusion**: At least one resource must be held in a non-shareable mode (only one thread can hold it at a time).
2. **Hold and Wait**: A thread holds at least one resource and is waiting to acquire additional resources held by other threads.
3. **No Preemption**: Resources cannot be forcibly taken from a thread; they can only be released voluntarily by the thread holding them.
4. **Circular Wait**: A closed chain of threads exists such that each thread holds a resource needed by the next thread in the chain (Thread 1 waits for Thread 2, which waits for Thread 1).

---

## 2. Deadlock Demonstration: Account Transfer Scenario

Consider a banking transfer where each transaction must lock both the source and target accounts before performing debit and credit operations:

```java
public class DeadlockDemo {

    static final Object accountA = new Object();
    static final Object accountB = new Object();

    // Transaction 1: Transfer from A -> B
    static void transferAToB() {
        synchronized (accountA) { // Grabs Lock A
            System.out.println("T1: Locked Account A. Waiting for Account B...");
            sleep(50);

            synchronized (accountB) { // Tries to grab Lock B -> BLOCKS!
                System.out.println("T1: Transfer A -> B completed.");
            }
        }
    }

    // Transaction 2: Transfer from B -> A
    static void transferBToA() {
        synchronized (accountB) { // Grabs Lock B
            System.out.println("T2: Locked Account B. Waiting for Account A...");
            sleep(50);

            synchronized (accountA) { // Tries to grab Lock A -> BLOCKS!
                System.out.println("T2: Transfer B -> A completed.");
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(DeadlockDemo::transferAToB, "Transfer-T1");
        Thread t2 = new Thread(DeadlockDemo::transferBToA, "Transfer-T2");

        t1.start();
        t2.start();
        // Result: Program hangs indefinitely. Both threads deadlock.
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
```

### The Failure Sequence

```
Thread 1: Locks Account A ────► Wants Account B (held by Thread 2) ──► ⛔
                                         ▲
                                         │ Circular Wait
                                         ▼
Thread 2: Locks Account B ────► Wants Account A (held by Thread 1) ──► ⛔
```

---

## 3. Deadlock Prevention Strategy: Strict Lock Ordering

The most effective and widely adopted prevention technique is to **break the Circular Wait condition** by enforcing a **strict, global lock acquisition order**.

If every thread acquires locks in the exact same consistent order, circular wait becomes mathematically impossible.

### Refactored Solution with Global Ordering

```java
public class DeadlockFreeTransfer {

    static class Account {
        final int id;
        int balance;

        Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    public static void transfer(Account from, Account to, int amount) {
        // Determine lock order deterministically using unique account IDs
        Account firstLock = from.id < to.id ? from : to;
        Account secondLock = from.id < to.id ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                from.balance -= amount;
                to.balance += amount;
                System.out.println("Transferred " + amount + " from Acc " + from.id + " to Acc " + to.id);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account(101, 1000);
        Account acc2 = new Account(102, 1000);

        // Thread 1 transfers 101 -> 102
        Thread t1 = new Thread(() -> transfer(acc1, acc2, 100));
        // Thread 2 transfers 102 -> 101
        Thread t2 = new Thread(() -> transfer(acc2, acc1, 200));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("All transfers completed without deadlock!");
    }
}
```
Regardless of the transfer direction, both threads will always acquire `acc1` (id 101) before `acc2` (id 102). One thread proceeds, finishes, and the second thread cleanly takes its turn.

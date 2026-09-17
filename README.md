# Java Multithreading & Concurrency

A structured, production-aligned repository for learning and practicing Java Multithreading and Concurrency.

---



## 🛠️ Build & Run

### Prerequisites
- Java Development Kit (JDK 17 or higher)

### Compile All Sources
Compile all source files from the project root:
```bash
javac -d out $(find src -name "*.java")
```

### Run Examples
Execute any demonstration class using its fully qualified name:

```bash
# 1. Thread Creation & Fundamentals
java -cp out com.practice.multithreading.creation.ThreadCreationDemo

# 2. Thread Control & Cooperative Stopping
java -cp out com.practice.multithreading.control.StoppingThreadDemo

# 3. Race Condition Resolution (AtomicInteger)
java -cp out com.practice.multithreading.synchronization.BrokenTicketCounterAtomicDemo

# 4. Real-World Parallel Task Coordination
java -cp out com.practice.multithreading.realworld.FoodOrderProcessorDemo
```

---

## 📖 Concept Documentation

All in-depth concept explanations and standalone code snippets are available in [`docs/`](docs/):

1. [Thread Creation & Fundamentals](docs/Thread%20Creation%20&%20Basics.md)
2. [Thread Lifecycle, Sleep & Cooperative Stopping](docs/Thread%20Lifecycle%20&%20Stopping.md)
3. [Thread Join Coordination](docs/Thread%20Join%20Demo.md)
4. [Synchronized vs Volatile](docs/Synchronized%20vs%20Volatile.md)
5. [Race Condition & Thread Synchronization](docs/Race%20Condition.md)
6. [Deadlock Analysis & Prevention](docs/Deadlock%20Prevention.md)
7. [Parallel Task Coordination](docs/Parallel%20Task%20Coordination.md)
8. [Concurrency Architecture & Roadmap](docs/Concurrency%20Architecture%20&%20Roadmap.md)

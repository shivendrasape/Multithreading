# Thread join demo class

### Execution Order Pattern

```java
thread1.start();
thread1.join(); // Waits for thread1 to finish before continuing

thread2.start();
thread3.start();
```

### Complete Demo

```java
public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(() -> {
        System.out.println("Doing some work..");
    }, "thread 1");

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("other runnable");
        }
    };

    Thread thread2 = new Thread(runnable);

    thread1.start();
    thread1.join();

    thread2.start();
    thread2.join();

    System.out.println("printing ..");
}
```
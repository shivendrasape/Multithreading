package com.practice.multithreading.creation;

public class ThreadCreationDemo {

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            System.out.println("Doing some work..");
        }, "thread 1");

        /*
           Common pitfall
           It is NOT executed by the new thread you just created.
           Instead, the run() method is executed by the thread that created the thread.
         */
        thread1.run();

        MyThread myThread = new MyThread();
        myThread.start();

        // we can not start thread twice, it will throw IllegalThreadStateException
        // myThread.start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("anonymous runnable");
            }
        };

        Thread thread2 = new Thread(runnable);
        thread2.start();
    }
}

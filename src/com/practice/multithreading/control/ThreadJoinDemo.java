package com.practice.multithreading.control;

public class ThreadJoinDemo {

    /*
    The join method allows one thread to wait for the completion of another.
    If t is a Thread object whose thread is currently executing, t.join();
    causes the current thread to pause execution until t's thread terminates.
    Overloads of join allow the programmer to specify a waiting period.
    However, as with sleep, join is dependent on the OS for timing, so you should not assume that join will
    wait exactly as long as you specify.

    https://www.educative.io/answers/what-is-the-threadjoin-method-in-java
     */
    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                System.out.println("Starting " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Completed " + Thread.currentThread().getName());
            }
        };

        Thread thread1 = new Thread(runnable, "Thread 1");
        Thread thread2 = new Thread(runnable, "Thread 2");
        Thread thread3 = new Thread(runnable, "Thread 3");

        thread1.start();

        System.out.println("Pausing the " + Thread.currentThread().getName() + " thread before executing " + thread2.getName() + " and " + thread3.getName());
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
        thread3.start();
    }
}

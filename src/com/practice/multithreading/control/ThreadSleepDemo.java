package com.practice.multithreading.control;

public class ThreadSleepDemo {

    public static void main(String[] args) {

        System.out.println("Hello " + Thread.currentThread().getName());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Hello " + Thread.currentThread().getName());
            }
        };

        Thread thread1 = new Thread(runnable, "thread 1");
        thread1.start();

        // runnable as lambda
        Thread thread2 = new Thread(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        }, "Thread 2");

        thread2.start();
    }
}

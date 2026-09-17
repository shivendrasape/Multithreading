package com.practice.multithreading.synchronization;

public class SynchronizedKeywordDemo {
    /*
    Synchronization is a process of handling resource accessibility by multiple thread requests.
    The main purpose of synchronization is to avoid thread interference. At times when more than one
    thread try to access a shared resource, we need to ensure that resource will be used by only one thread at a time.
    Synchronization thus prevents the race condition
     */
    public static void main(String[] args) {

        // shared resource
        Counter counter = new Counter();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                for (int i = 1; i <= 10000; i++) {
                    counter.increment();
                }
                System.out.println(counter.getValue());
            }
        };

        Thread thread1 = new Thread(runnable, "thread 1");
        Thread thread2 = new Thread(runnable, "thread 2");

        // without synchronization the result will be inconsistent
        // since both of the threads has entered the method when one currently running
        // count will be greater than 10000 in both cases

        // we can either wait for one thread to complete using join or use synchronization
        thread1.start();
        thread2.start();
    }
}

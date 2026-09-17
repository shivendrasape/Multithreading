package com.practice.multithreading.creation;

public class MultipleThreadsDemo {

    public static void main(String[] args) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    System.out.println("count " + i + " Executed by " + Thread.currentThread().getName());
                }
            }
        };

        Thread thread1 = new Thread(task, "Thread 1");
        Thread thread2 = new Thread(task, "Thread 2");
        Thread thread3 = new Thread(task, "Thread 3");

        thread1.start();
        thread2.start();
        thread3.start();

        /*
          In this example each of thread will execute the runnable task i.e count from 1 to 500, without waiting
          for other thread.

          Thread.currentThread() will give the name of the thread which is executing the task
         */
    }
}

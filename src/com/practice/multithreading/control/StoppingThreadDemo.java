package com.practice.multithreading.control;

public class StoppingThreadDemo {

    public static void main(String[] args) {

        Server server = new Server();

        Thread thread1 = new Thread(server, " thread 1");

        // Infinitely running server thread
        thread1.start();

        // waiting for some time to keep thread 1 running, i.e main thread will wait for 1 sec before call stop
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " is stopping " + thread1.getName());
        server.stop();
    }
}

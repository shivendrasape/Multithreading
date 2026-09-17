package com.practice.multithreading.control;

public class Server implements Runnable {

    /*
       We need to make sure that to stop the thread flag is a volatile, otherwise, in the worst case,
       your thread may not stop and run infinitely, Why? because, in the absence of any synchronization
       instruction like volatile modifier here, the compiler is free to cache the value of boolean variable exit,
       which means even if the main thread makes it true, the Server will always see it false, thus running infinitely.

       The volatile keyword does not cache the value of the variable and always read the variable from the main memory.
       The volatile keyword cannot be used with classes or methods.
     */
    volatile boolean exit = false;

    @Override
    public void run() {

        // an infinite loop till flag is false
        while (!exit) {
            System.out.println("Server is running ..");
        }

        System.out.println("Server is stopped");
    }

    public void stop() {
        exit = true;
    }
}

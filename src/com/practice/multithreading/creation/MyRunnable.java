package com.practice.multithreading.creation;

public class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("My Runnable, executed by " + Thread.currentThread().getName());
    }
}

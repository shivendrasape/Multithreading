package com.practice.multithreading.synchronization;

public class Counter {

    int count;

    void increment() {
        count++;
    }

    synchronized void increment1() {
        count++;
    }

    //    Sometimes it is preferable to synchronize only part of a method.
    //    Java synchronized blocks inside methods makes this possible.
    void increment2() {
        // here this is monitor object
        synchronized (this) {
            count++;
        }
    }

    int getValue() {
        return count;
    }
}

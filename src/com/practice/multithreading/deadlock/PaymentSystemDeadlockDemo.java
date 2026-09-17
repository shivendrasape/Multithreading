package com.practice.multithreading.deadlock;

/*
You're building a payment system. Every transaction needs to:

Lock the sender's account (debit)
Lock the receiver's account (credit)

Two transactions fire simultaneously:

Transaction-1: Raj → Priya (locks Raj's account first, then Priya's)
Transaction-2: Priya → Raj (locks Priya's account first, then Raj's)

Problem -

Transaction-1:  holds Raj's lock    → waiting for Priya's lock...
Transaction-2:  holds Priya's lock  → waiting for Raj's lock...

Both wait forever. Neither releases.
Payment system frozen.

 */
public class PaymentSystemDeadlockDemo {

    static final Object rajAccount   = new Object(); // Lock A
    static final Object priyaAccount = new Object(); // Lock B

    // Transaction 1: Raj → Priya
    static void transferRajToPriya() {
        synchronized (rajAccount) {                    // Grabs Lock A
            System.out.println("T1: Locked Raj's account");

            sleep(100); // Simulates some processing
                        // Gives T2 time to grab Lock B
                        // Making deadlock near-certain

            System.out.println("T1: Waiting for Priya's account...");
            synchronized (priyaAccount) {              // Tries to grab Lock B
                                                       // ← HANGS HERE FOREVER
                System.out.println("T1: ✅ Transfer Raj → Priya done!");
            }
        }
    }

    // Transaction 2: Priya → Raj
    static void transferPriyaToRaj() {
        synchronized (priyaAccount) {                  // Grabs Lock B
            System.out.println("T2: Locked Priya's account");

            sleep(100);

            System.out.println("T2: Waiting for Raj's account...");
            synchronized (rajAccount) {                // Tries to grab Lock A
                                                       // ← HANGS HERE FOREVER
                System.out.println("T2: ✅ Transfer Priya → Raj done!");
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(PaymentSystemDeadlockDemo::transferRajToPriya, "T1-Raj→Priya");
        Thread t2 = new Thread(PaymentSystemDeadlockDemo::transferPriyaToRaj, "T2-Priya→Raj");

        t1.start();
        t2.start();
        // Program never terminates. Both threads hang forever.
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}

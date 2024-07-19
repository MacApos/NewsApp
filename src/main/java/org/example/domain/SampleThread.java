package org.example.domain;

import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SampleThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread name : " + Thread.currentThread().getName());
        int div = 10 / 0;
        System.out.println("Div=:" + div);
    }
}

class SampleRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("Thread name : " + Thread.currentThread().getName());
        int div = 10 / 0;
//        System.out.println("Div=:" + div);
    }
}

class TestClass {
    private static Runnable errorHandlingWrapper(Runnable action) {
        return () -> {
            try {
                action.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
    }

    public static void main(String args[]) {


        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(errorHandlingWrapper(new SampleRunnable()), 0, 2000, TimeUnit.MILLISECONDS);

    }
}

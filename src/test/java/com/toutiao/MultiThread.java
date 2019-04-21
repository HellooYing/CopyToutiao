package com.toutiao;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThread {
    public static void main(String[] argv) {
        //testThread();
        testSynchronized();
        //testBlockingQueue();
        //testAtomic();
        //testThreadLocal();
        //testExecutor();
        //testFutrue();
    }

    static void testFutrue(){
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future=service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
            }
        });
        service.shutdown();
        try{
            System.out.println(future.get(100, TimeUnit.MILLISECONDS));
            //System.out.println(future.get());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    static void testExecutor(){
        ExecutorService service= Executors.newFixedThreadPool(1);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute1 " + i);
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute2 " + i);
                }
            }
        });
        service.shutdown();
        while (!service.isTerminated()) {
            sleep(1000);
            System.out.println("Wait for termination.");
        }
    }

    static private ThreadLocal<Integer> tid=new ThreadLocal<>();
    static private int id;
    static void testThreadLocal(){
        for(int i=0;i<10;i++){
            final int f=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tid.set(f);
                    sleep(1000);
                    System.out.println("ThreadLocal: " + tid.get());
                }
            }).start();
        }
        for(int i=0;i<10;i++){
            final int f=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    id=f;
                    sleep(1000);
                    System.out.println("NoThreadLocal: " + id);
                }
            }).start();
        }
    }

    private static int c1=0;
    private static AtomicInteger c2=new AtomicInteger(0);

    static void testAtomic(){
        testWithAtomic();
        //testWithoutAtomic();
    }

    static void testWithAtomic(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    System.out.println(c2.incrementAndGet());
                }
            }).start();
        }
    }

    static void testWithoutAtomic(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    System.out.println(c1++);
                }
            }).start();
        }
    }
    public static void sleep(int mills) {
        try {
            //Thread.sleep(new Random().nextInt(mills));
            Thread.sleep(mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testBlockingQueue(){
        BlockingQueue<Integer> q=new ArrayBlockingQueue<Integer>(10);
        new Thread(new Produce(q)).start();
        new Thread(new Consumer(q),"c1").start();
        new Thread(new Consumer(q),"c2").start();
    }

    static class Produce implements Runnable{
        private BlockingQueue<Integer> q;
        public Produce(BlockingQueue<Integer> q){
            this.q=q;
        }
        @Override
        public void run(){
            try{
                for(int i=0;i<10;i++){
                    Thread.sleep(1000);
                    q.put(i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable{
        private BlockingQueue<Integer> q;
        public Consumer(BlockingQueue<Integer> q){
            this.q=q;
        }
        @Override
        public void run(){
            try{
                while(true){
                    System.out.println(Thread.currentThread().getName()+":"+q.take());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    private static Object obj=new Object();
    private static Object obj2=new Object();


    private static void testSynchronized1(){
        synchronized (obj){
            try {
                for(int i=0;i<3;i++){
                    Thread.sleep(400);
                    System.out.println("1 "+i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static void testSynchronized2(){
        synchronized (obj){
            try {
                for(int i=0;i<3;i++){
                    Thread.sleep(400);
                    System.out.println("2 "+i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized(){
        for(int i=0;i<3;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testThread() {
//        for (int i = 0; i < 10; ++i) {
//            new MyThread(i).start();
//        }
        for(int i=0;i<5;i++){
            final int tid=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for(int i=0;i<5;i++){
                            Thread.sleep(10);
                            System.out.println(tid+" "+i);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    class MyThread extends Thread{
        private int tid;
        public MyThread(int tid){
            this.tid=tid;
        }
        @Override
        public void run(){
            try{
                for(int i=0;i<5;i++){
                    Thread.sleep(1000);
                    System.out.println(tid+" "+i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}


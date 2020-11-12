package ru.otus.gc.bench;

public interface BenchmarkMBean extends Runnable {
//    public void run() throws InterruptedException;

    public void setSize(int size);
}
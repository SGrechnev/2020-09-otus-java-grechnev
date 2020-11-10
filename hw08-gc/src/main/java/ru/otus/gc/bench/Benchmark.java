package ru.otus.gc.bench;

public class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 5_000_000;
    private final static String SOME_BIG_STRING = "SOME_".repeat(13107); // length == 65535

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    @Override
    public void run() {
//        Runtime runtime = Runtime.getRuntime();
        Object[] outerArray = new Object[loopCounter];
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new String(new char[0]);
            }
            outerArray[idx] = new String(SOME_BIG_STRING.substring(idx % SOME_BIG_STRING.length()));
//            outerArray[idx] = new String(new char[0]);
            try {
//              outerArray[idx] = new String(SOME_BIG_STRING);
              Thread.sleep(40); //Label_1
            } catch (InterruptedException e) {
              System.out.println(e);
              return;
          }
//            long memory = runtime.freeMemory();
//            System.out.println("Free memory is bytes: " + memory);
        }
    }

    @Override
    public void setSize(int newSize){
        if ( newSize < 1 ){
            System.out.println("ne ok, newSize must be positive ;)");
            return;
        }
        System.out.println("ok, new size: " + newSize);
        this.size = newSize;
    }
}
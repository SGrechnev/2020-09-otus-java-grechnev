package ru.otus.gc.bench;

import javax.management.*;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public class GcMonitor {

    private BenchmarkMBean benchmark;

    private int timeInterval = 60; // in seconds

    public GcMonitor(BenchmarkMBean benchmark) {
        this.benchmark = benchmark;
    }

    public void setMonitoringInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void start() throws MalformedObjectNameException, NotCompliantMBeanException,
            InstanceAlreadyExistsException, MBeanRegistrationException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=" + this.benchmark.getClass().getSimpleName());

        mbs.registerMBean(this.benchmark, name);
        Thread benchmarkThread = new Thread(this.benchmark);
        benchmarkThread.start();

        startMonitoring();
    }

    private void startMonitoring() {
        try {
            List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
            long[] prevCounts = new long[gcbeans.size()];
            long[] prevTimes = new long[gcbeans.size()];
            Arrays.fill(prevCounts, 0);
            Arrays.fill(prevTimes, 0);
            while (!Thread.currentThread().isInterrupted()) {
                // G1 Young Generation, G1 Old Generation
                int idx = 0;
                for (GarbageCollectorMXBean gcbean : gcbeans) {
                    long currentCount = gcbean.getCollectionCount();
                    long currentTime = gcbean.getCollectionTime();

                    System.out.printf("GC name: %s,\tcalled %d times, time: %dms%n",
                            gcbean.getName(),
                            currentCount - prevCounts[idx],
                            currentTime - prevTimes[idx]);

                    prevTimes[idx] = currentTime;
                    prevCounts[idx++] = currentCount;
                }
                Thread.sleep(this.timeInterval * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("----- Finish -----");
        }
    }

}
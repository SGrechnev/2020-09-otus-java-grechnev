package ru.otus.gc;

import ru.otus.gc.bench.Benchmark;
import ru.otus.gc.bench.BenchmarkMBean;
import ru.otus.gc.bench.GcMonitor;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.IOException;

public class GcMain {

    public static void main(String[] args) {
        int loopCounter = 40_000_000, size = 1_000_000, monitoringInterval = 60;
        BenchmarkMBean benchmark = new Benchmark(loopCounter);
        benchmark.setSize(size);

        GcMonitor gcMonitor = new GcMonitor(benchmark);
        gcMonitor.setMonitoringInterval(monitoringInterval);
        try {
            gcMonitor.start();
        } catch (MalformedObjectNameException | NotCompliantMBeanException | InstanceAlreadyExistsException | MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

}
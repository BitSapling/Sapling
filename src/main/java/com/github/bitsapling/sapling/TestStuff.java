package com.github.bitsapling.sapling;

import com.github.bitsapling.sapling.util.PerfMonitorPlus;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TestStuff {
    public TestStuff() throws InterruptedException {
        testPerfMonitorBasic();
        testPerfMonitorPlus();
    }

    private void testPerfMonitorPlus() throws InterruptedException {
        Random rand = new Random();
        PerfMonitorPlus monitor = new PerfMonitorPlus("Test Monitor");
        try (PerfMonitorPlus.Child prepareTask = monitor.child("Read database from configuration")) {
            Thread.sleep(1800); // Prepare
            for (int i = 0; i < 8; i++) {
                try (PerfMonitorPlus.Child decodeTask = monitor.child("Decoding: " + i + ".db")) {
                    Thread.sleep(300); // Decoding
                    if (rand.nextBoolean()) {
                        try (PerfMonitorPlus.Child upgradeTask = monitor.child("Upgrading: " + i + ".db")) {
                            Thread.sleep(200); // Decoding
                            if (rand.nextBoolean()) {
                                try (PerfMonitorPlus.Child convertTask = monitor.child("Converting:" + i + ".db")) {
                                    Thread.sleep(500); // Converting
                                }
                            }
                        }
                    }
                }
            }
        }
        try (PerfMonitorPlus.Child finishTask = monitor.child("Finishing the database")) {
            Thread.sleep(1000);
        }
        monitor.close();
        LoggerFactory.getLogger("Test").info(monitor.generateReport());
    }

    private void testPerfMonitorBasic() {

    }
}

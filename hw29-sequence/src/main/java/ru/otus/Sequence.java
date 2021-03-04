package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sequence {
    private static final Logger logger = LoggerFactory.getLogger(Sequence.class);
    private Integer current = 1;
    private Integer diff = 1;

    private synchronized void action(Integer initState) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                //поэтому не if
                while ((current - initState) % 2 == 0) {
                    this.wait();
                }

                if ((current == 1) && (diff == -1) || (current == 10) && (diff == 1)) {
                    diff = -diff;
                }
                logger.info("current: {}", current);
                current = current + diff;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new NotInterestingException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Sequence sequence = new Sequence();
        new Thread(() -> sequence.action(2)).start();
        new Thread(() -> sequence.action(1)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }
}
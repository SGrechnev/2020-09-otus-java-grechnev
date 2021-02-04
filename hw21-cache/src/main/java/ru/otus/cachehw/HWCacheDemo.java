package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        try{
            new HWCacheDemo().demo();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    private void demo() throws InterruptedException {
        HwCache<Integer, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<Integer, Integer> listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        // Duplicate listener
        cache.addListener(listener);
        cache.addListener(listener);
        cache.put(1, 1);

        logger.info("getValue:{}", cache.get(1));
        cache.remove(1);
        cache.removeListener(listener);

        // Check that listener removed
        cache.put(2, 2);

        {
            // Check WeakReference in listeners
            HwListener<Integer, Integer> listener2 = new HwListener<Integer, Integer>() {
                @Override
                public void notify(Integer key, Integer value, String action) {
                    logger.info("# Key:{}, Value:{}, Action: {}", key, value, action);
                }
            };
            cache.addListener(listener2);
            cache.put(3,3);
        }
        logger.info("local variable 'listener2' is forgotten (x_x)");
        logger.info("So strange...");
        Thread.sleep(1000);
        System.gc();
        cache.put(4,4);
        Thread.sleep(1000);
        System.gc();
        cache.put(5,5);
        for (int i = 6; i < 10; i++) {
            System.gc();
            Thread.sleep(100);
            cache.put(i, i);
        }
    }
}

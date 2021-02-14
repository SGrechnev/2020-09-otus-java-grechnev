package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.WeakHashMap;

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
        HwCache<Integer, Integer> cache = new MyCache<>(new WeakHashMap<>());

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

        /** Check WeakReference in listeners
         *  listener2 is local variable for next block
         */
        {
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
        for (int i = 4; i < 5; i++) {
            System.gc();
            cache.put(i, i);
        }
    }
}

package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    //Надо реализовать эти методы
    private final Map<K, V> innerMap;
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    public MyCache(Map<K, V> map){
        this.innerMap = map;
    }

    @Override
    public void put(K key, V value) {
        innerMap.put(key, value);
        notifyAndShrink(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = innerMap.remove(key);
        if (value == null){
            // Элемент по ключу не найден
            return;
        }
        notifyAndShrink(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = innerMap.get(key);
        notifyAndShrink(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (listenersHas(listener)){
            logger.info("listener is already in list");
        } else {
            listeners.add(new WeakReference<>(listener));
            logger.info("listener was added");
        }
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        var weakListenersToRemove = listeners.stream()
                .filter(l -> listener.equals(l.get()))
                .collect(Collectors.toList());
        listeners.removeAll(weakListenersToRemove);
        logger.info("listener was removed");
    }

    private boolean listenersHas(HwListener<K, V> listener) {
        for (var listener_ : listeners) {
            if (listener.equals(listener_.get())) {
                return true;
            }
        }
        return false;
    }

    private void notifyAndShrink(K key, V value, String action) {
        for (var iterator = listeners.iterator(); iterator.hasNext(); ) {
            var listener = iterator.next().get();
            if (listener != null) {
                try {
                    listener.notify(key, value, action);
                } catch (Throwable e) {
                    logger.error("Error occurred during notification: {}", e.getMessage());
                }
            } else {
                logger.info("Forget removed listener");
                iterator.remove();
            }
        }
    }
}

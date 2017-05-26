package com.sailing.facetec.queue;

import com.sailing.facetec.entity.RlEntity;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yunan on 2017/5/26.
 */
public class DataQueue {
    private static LinkedBlockingQueue<String> faceRepositoryQueue = new LinkedBlockingQueue<>(10000);

    public static String takeFromQueue() throws InterruptedException {
        return faceRepositoryQueue.take();
    }

    public static void putToQueue(String dataStr) throws InterruptedException {
        faceRepositoryQueue.put(dataStr);
    }

    public static boolean isEmpty(){
        return faceRepositoryQueue.isEmpty();
    }

}

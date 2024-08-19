package com.intuit.example.zwigato.utility;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadConfigManager {
    public static final ExecutorService DEFAULT_POOL = Executors.newCachedThreadPool();

    public static void runAsync(String orderId, Runnable runnable) {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        Runnable overRiddenRunnable = getOverriddenRunnable(orderId, runnable);
        CompletableFuture.runAsync(overRiddenRunnable, executor);
    }

    private static Runnable getOverriddenRunnable(String orderId, Runnable runnable) {
        MDC.put("ORDER_ID", orderId);
        Map<String, String> mdc = MDC.getCopyOfContextMap();
        return () -> {
            try {
                mdc.forEach(MDC::put);
                 MDC.put("ORDER_ID", orderId);
                runnable.run();
            }
            catch (Exception e) {
                log.error("Exception occurred", e);
            }
            finally {
                MDC.clear();
            }
        };
    }

}

package com.ortopunkt.telegram.ui.photo;

import com.ortopunkt.crm.entity.Application;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoCollector {

    private static final Map<Long, List<Application>> buffer = new ConcurrentHashMap<>();
    private static final Map<Long, Instant> lastReceivedTime = new ConcurrentHashMap<>();
    private static final long TIMEOUT_SECONDS = 4;

    public static void add(Long chatId, Application app) {
        buffer.computeIfAbsent(chatId, k -> new ArrayList<>()).add(app);
        lastReceivedTime.put(chatId, Instant.now());
    }

    public static boolean isReady(Long chatId) {
        Instant lastTime = lastReceivedTime.get(chatId);
        return lastTime != null && Instant.now().isAfter(lastTime.plusSeconds(TIMEOUT_SECONDS));
    }

    public static List<Application> collect(Long chatId) {
        List<Application> apps = buffer.remove(chatId);
        lastReceivedTime.remove(chatId);
        return apps != null ? apps : List.of();
    }

    public static Set<Long> getChatIds() {
        return buffer.keySet();
    }

    public static void clear(Long chatId) {
        buffer.remove(chatId);
        lastReceivedTime.remove(chatId);
    }
}

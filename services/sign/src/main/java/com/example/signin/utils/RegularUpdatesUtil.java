package com.example.signin.utils;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 定期更新工具类，用于生成和更新随机字符串。
 */
@Component
@Data
public class RegularUpdatesUtil {
    // 存储与特定任务关联的调度执行服务
    private static Map<String, ScheduledExecutorService> executorServices = new HashMap<>();
    // 存储随机字符串及其关联的键
    private static Map<String, String> randomStrings = new ConcurrentHashMap<>();

    /**
     * 启动生成特定键关联的随机字符串任务。
     *
     * @param key 任务的唯一标识符。
     */
    public static void startGenerating(String key) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // 以固定延迟执行更新随机字符串任务
        executorService.scheduleAtFixedRate(() -> updateRandomString(key), 0, 20, TimeUnit.SECONDS);
        executorServices.put(key, executorService);
    }

    /**
     * 停止生成特定键关联的随机字符串任务。
     *
     * @param key 要停止的任务的唯一标识符。
     */
    public static void stopGenerating(String key) {
        ScheduledExecutorService executorService = executorServices.get(key);
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        executorServices.remove(key);
    }

    /**
     * 立即启动任务，并在指定时间后关闭任务。
     *
     * @param key             任务的唯一标识符。
     * @param durationSeconds 任务持续时间，单位为秒。
     */
    public static void startTaskAndStopAfterDuration(String key, long durationSeconds) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // 立即启动任务
        executorService.execute(() -> startGenerating(key));

        // 安排在指定时间后关闭任务
        executorService.schedule(() -> {
            stopGenerating(key);
            executorService.shutdown(); // 停止ExecutorService，防止内存泄漏
            System.out.println("任务已关闭：" + key);
        }, durationSeconds, TimeUnit.SECONDS);

        // 存储executorService以便之后可以管理
        executorServices.put(key, executorService);

        System.out.println("任务已启动：" + key);
    }

    /**
     * 在指定的开始时间启动任务，并在经历指定时长后关闭该任务。
     *
     * @param key       要关闭的任务的唯一标识符。
     * @param time      时间，单位为秒。
     * @param startTime 开始时间。
     */
    public static void scheduleStartAndShutdown(String key, long time, LocalDateTime startTime) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        long delaySeconds = Duration.between(LocalDateTime.now(ZoneId.of("Asia/Shanghai")), startTime).toSeconds();
        executorService.schedule(() -> startGenerating(key), delaySeconds, TimeUnit.SECONDS);

        executorService.schedule(() -> {
            stopGenerating(key);
            executorService.shutdown(); // 停止ExecutorService，防止内存泄漏
            System.out.println("任务已关闭：" + key);
        }, delaySeconds + time, TimeUnit.SECONDS);

        // 存储executorService以便之后可以管理
        executorServices.put(key, executorService);

        System.out.println("任务已启动：" + key);
    }

    /**
     * 更新指定键关联的随机字符串。
     *
     * @param key 需要更新随机字符串的键。
     */
    private static void updateRandomString(String key) {
        String randomString = generateRandomString();
        randomStrings.put(key, randomString);
        System.out.println("已更新随机字符串：" + key + " -> " + randomString);
    }

    /**
     * 生成一个指定长度的随机字符串。
     *
     * @return 生成的随机字符串。
     */
    private static String generateRandomString() {
        int length = 24;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 根据给定键获取对应的随机字符串。
     *
     * @param key 随机字符串的唯一标识符。
     * @return 对应于给定键的随机字符串，若未找到则返回 null 或抛出异常（根据实际需求调整）。
     */
    public static String getRandomStringByKey(String key) {
        return randomStrings.get(key);
    }

    /**
     * 如果线程已经启动或被预定在未来启动，则取消此线程。
     *
     * @param key 任务的唯一标识符。
     */
    public static boolean cancelScheduledTaskIfExists(String key) {
        ScheduledExecutorService executorService = executorServices.get(key);
        if (executorService != null) {
            // 尝试优雅地关闭正在运行的ExecutorService
            if (!executorService.isShutdown()) {
                executorService.shutdownNow(); // 尝试停止所有正在执行的任务并关闭ExecutorService
            }
            executorServices.remove(key); // 从存储中移除，表示任务已被取消
            return true; // 成功取消
        }
        // 如果没有找到对应的任务
        return false; // 取消失败
    }

    /**
     * 每十秒更新一次所有随机字符串。
     */
    @Scheduled(fixedRate = 10000)
    public void updateAllRandomStrings() {
        for (Map.Entry<String, ScheduledExecutorService> entry : executorServices.entrySet()) {
            String key = entry.getKey();
            ScheduledExecutorService executorService = entry.getValue();
            String randomString = generateRandomString();
            randomStrings.put(key, randomString);
            System.out.println("已更新随机字符串：" + key + " -> " + randomString);
        }
    }
}

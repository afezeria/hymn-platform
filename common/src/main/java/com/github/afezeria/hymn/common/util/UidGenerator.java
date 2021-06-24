package com.github.afezeria.hymn.common.util;

import java.time.*;

/**
 * id生成器
 *
 * @author afezeria
 * date 2021/6/10 上午10:17
 */
public class UidGenerator {
    /**
     * 机器id所占的位数
     */
    private final static long WORKER_ID_BITS = 5L;
    /**
     * 数据标识id所占的位数
     */
    private final static long DATACENTER_ID_BITS = 5L;
    /**
     * 时间戳所占位数
     */
    private final static long TIME_BITS = 41L;
    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * 支持的最大数据标识id，结果是31
     */
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    /**
     * 支持的最大时间戳
     */
    private final static long MAX_TIMESTAMP = ~(-1L << TIME_BITS);
    /**
     * 序列在id中占的位数
     */
    private final static long SEQUENCE_BITS = 12L;
    /**
     * 机器ID向左移12位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据标识id向左移17位(12+5)
     */
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间截向左移22位(5+5+12)
     */
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private final long datacenterId;
    /**
     * 开始时间截
     */
    private final long epoch;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public UidGenerator(int workerId, int datacenterId, String utcTimeStr) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new UidGenerateException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new UidGenerateException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.epoch = ZonedDateTime.of(LocalDateTime.parse(utcTimeStr), ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public static void main(String[] args) {

        System.out.println(LocalDateTime.parse("2020-01-01T00:00:00").toInstant(ZoneOffset.UTC).toEpochMilli());
        long l = System.currentTimeMillis();
        long l1 = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        System.out.println(l);
        System.out.println(l1);
        System.out.println("l" + LocalDateTime.ofInstant(Instant.ofEpochMilli(l1), ZoneId.systemDefault()));
        System.out.println("s" + LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault()));
        System.out.println("u" + LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
        System.out.println(ZoneId.systemDefault());
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return id
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new UidGenerateException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - epoch) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - epoch > MAX_TIMESTAMP) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentTimeMillis);
        }
        return System.currentTimeMillis();
    }
}

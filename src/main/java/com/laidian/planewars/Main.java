package com.laidian.planewars;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hfb
 * @date 2018/11/21
 */
public class Main {

    /**
     * 窗口初始化宽
     */
    private static final int WIDTH = 400;
    /**
     * 窗口初始化高
     */
    private static final int HEIGHT = 654;


    private static int poolSize = Runtime.getRuntime().availableProcessors();
    private static ThreadFactory threadFactory = new BasicThreadFactory.Builder()
            .namingPattern("myThread").daemon(true).build();
    private static ExecutorService pool = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), threadFactory);

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("飞机大战");
        frame.setSize(WIDTH,HEIGHT);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        GamePanel game = new GamePanel(WIDTH,HEIGHT);
        pool.execute(game.getGameThread());
        pool.execute(game);

        frame.add(game);
        frame.setVisible(true);

    }
}

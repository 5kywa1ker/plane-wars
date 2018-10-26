package com.laidian.planewars;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author hfb
 * @date 2018/10/26
 */
public class GamePanel extends JPanel {
    /**
     * 窗口宽
     */
    public static final int WIDTH=400;
    /**
     * 窗口高
     */
    public static final int HEIGHT=654;

    /**
     * 游戏状态-启动
     */
    public static final int START=0;
    /**
     * 游戏状态-运行
     */
    public static final int RUNNING=1;
    /**
     * 游戏状态-暂停
     */
    public static final int PAUSE=2;
    /**
     * 游戏状态-结束
     */
    public static final int GAME_OVER=3;


    /**
     * 当前状态
     */
    private int state= START;
}

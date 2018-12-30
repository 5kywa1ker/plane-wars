package com.laidian.planewars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 敌机
 *
 * @author hfb
 * @date 2018/10/25
 */
public class Airplane extends FlyingObject implements Enemy {

    private static BufferedImage IMAGE;
    static {
        try {
            IMAGE = ImageIO.read(FlyingObject.class.getResourceAsStream("/img/airplane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 飞机的移动速度
     */
    private int speed;

    private GamePanel gamePanel;

    /**
     * 构造方法初始化
     */
    public Airplane(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.speed = 2;
        // 所有飞机对象使用同一张图片
        this.image = IMAGE;
        // 飞机的宽高就是图片的宽高
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        // 初始化坐标位置，x在窗口宽度范围内随机
        this.x = random.nextInt(gamePanel.getWidth() - this.width);
        this.y = -this.height;
    }

    @Override
    public int getScore() {
        return 5;
    }

    @Override
    public void step() {
        this.y += this.speed;
    }

    @Override
    public boolean outOfBounds() {
        return this.y > this.gamePanel.getHeight();
    }
}

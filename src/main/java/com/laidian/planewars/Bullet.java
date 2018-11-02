package com.laidian.planewars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 子弹
 * @author hfb
 * @date 2018/10/25
 */
public class Bullet extends FlyingObject {
    private int speed;

    public static BufferedImage IMAGE;

    static {
        try {
            IMAGE = ImageIO.read(FlyingObject.class.getResourceAsStream("/img/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造方法
     * @param x 子弹的x坐标
     * @param y 子弹的y坐标
     */
    public Bullet(int x, int y) {
        this.speed = 3;
        this.image = IMAGE;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.x = x;
        this.y = y;
    }

    @Override
    public void step() {
        this.y -= this.speed;
    }

    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;
    }
}

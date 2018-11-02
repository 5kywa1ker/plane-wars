package com.laidian.planewars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 蜜蜂🐝
 *
 * @author hfb
 * @date 2018/11/2
 */
public class Bee extends FlyingObject implements Award {
    private static BufferedImage IMAGE;
    static {
        try {
            IMAGE = ImageIO.read(FlyingObject.class.getResourceAsStream("/img/bee.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * x方向的速度
     */
    private int xSpeed;
    /**
     * y方向的速度
     */
    private int ySpeed;
    /**
     * 奖励类型 随机产生
     */
    private int awardType;

    public Bee() {
        this.xSpeed = 1;
        this.ySpeed = 2;
        this.image = IMAGE;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.x = random.nextInt(GamePanel.WIDTH - this.width);
        this.y = -this.height;
        this.awardType = random.nextInt(2);
    }

    @Override
    public int getType() {
        return this.awardType;
    }

    @Override
    public void step() {
        this.x += this.xSpeed;
        this.y += this.ySpeed;
        if (this.x >= GamePanel.WIDTH - this.width) {
            this.xSpeed = -1;
        }
        if (this.x <= 0) {
            this.xSpeed = 1;
        }
    }

    @Override
    public boolean outOfBounds() {
        return this.y > GamePanel.HEIGHT;
    }
}

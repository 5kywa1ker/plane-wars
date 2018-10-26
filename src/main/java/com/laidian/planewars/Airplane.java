package com.laidian.planewars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author hfb
 * @date 2018/10/25
 */
public class Airplane extends FlyingObject implements Enemy {

    private static BufferedImage IMAGE;
    static {
        try {
            IMAGE = ImageIO.read(FlyingObject.class.getResourceAsStream("img/airplane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 飞机的移动速度
     */
    private int speed = 2;

    /**
     * 构造方法初始化
     */
    public Airplane() {
        image = IMAGE;
        width = image.getWidth();
        height = image.getHeight();
        Random rand=new Random();
        x=rand.nextInt(GamePanel.WIDTH-this.width);
        y=-this.height;
    }

    public int getScore() {
        return 0;
    }

    @Override
    public void step() {

    }

    @Override
    public boolean outOfBounds() {
        return false;
    }
}

package com.laidian.planewars;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author hfb
 * @date 2018/10/25
 */
public abstract class FlyingObject {
    static Random random=new Random();

    /**
     * 飞行物体的图片
     */
    protected BufferedImage image;
    /**
     * 飞行物宽
     */
    protected int width;
    /**
     * 飞行物高
     */
    protected int height;
    /**
     * 飞行物x坐标
     */
    protected int x;
    /**
     * 飞行物y坐标
     */
    protected int y;

    /**
     * 移动一步
     */
    public abstract void step();

    /**
     * 是否出了边界
     *
     * @return boolean
     */
    public abstract boolean outOfBounds();

    /**
     * 判断这个飞行物体是否被子弹击中了
     * @param bullet 子弹
     * @return boolean
     */
    public boolean shootBy(Bullet bullet) {
        int x1 = this.x;
        int x2 = this.x + this.width;
        int y1 = this.y;
        int y2 = this.y + this.height;

        int x = bullet.x;
        int y = bullet.y;

        return x > x1 && x < x2 && y > y1 && y < y2;
    }


}

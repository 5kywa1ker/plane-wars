package com.laidian.planewars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 英雄机✈
 *
 * @author hfb
 * @date 2018/11/2
 */
public class Hero extends FlyingObject {
    private static BufferedImage[] IMAGES;

    static {
        try {
            IMAGES = new BufferedImage[2];
            IMAGES[0] = ImageIO.read(FlyingObject.class.getResourceAsStream("/img/hero0.png"));
            IMAGES[1] = ImageIO.read(FlyingObject.class.getResourceAsStream("/img/hero1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生命数
     */
    private int life;
    /**
     * 火力值 >1 1表示1倍火力，一次射出一颗子弹
     */
    private int fire;
    /**
     * 图片
     */
    private BufferedImage[] images;
    /**
     * 用于切换图片
     */
    private long index;

    private GamePanel gamePanel;


    public Hero(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.image = IMAGES[0];
        this.images = IMAGES;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        // 初始化位置 居中
        this.x = (gamePanel.getWidth() - this.width) / 2;
        this.y = (gamePanel.getHeight() - this.height) / 2 + 50;
        // 初始化3条命
        this.life = 3;
        this.fire = 1;
        this.index = 0;
    }

    @Override
    public void step() {
        this.index++;
        this.image = this.images[(int) (index / 10 % 2)];
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    /**
     * 射击
     *
     * @return 返回一组子弹
     */
    public Bullet[] shoot() {
        // 生成一组子弹，要初始化每颗子弹的坐标位置
        Bullet[] bullets;
        int bulletYOffset = this.y - 10;
        if (fire > 1) {
            bullets = new Bullet[fire];
            // 子弹的位置并排排列，相对于飞机位置是居中的
            // 每子弹间隔 10
            int bulletSpace = 10;
            // 子弹总宽度
            int totalWidth = (fire * Bullet.IMAGE.getWidth()) + ((fire - 1) * bulletSpace);
            // 第一颗子弹的x坐标
            int firstBulletXOffset = this.x + Math.abs((this.width - totalWidth) / 2);
            for (int i = 0; i < fire; i++) {
                int bulletXOffset = firstBulletXOffset + (i * (Bullet.IMAGE.getWidth() + bulletSpace));
                bullets[i] = new Bullet(bulletXOffset, bulletYOffset);
            }
        } else {
            bullets = new Bullet[1];
            int bulletXOffset = this.x + (this.width / 2);
            bullets[0] = new Bullet(bulletXOffset, bulletYOffset);
        }
        return bullets;
    }

    /**
     * 英雄机移动到某个坐标上
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }

    /**
     * 英雄机加一条命
     */
    public void addLife() {
        this.life++;
    }

    /**
     * 英雄机减一条命
     */
    public void subtractLife() {
        this.life--;
    }

    public int getLife() {
        return life;
    }

    /**
     * 重置火力
     */
    public void resetFire() {
        this.fire = 1;
    }

    /**
     * 设置火力
     * @param fire
     */
    public void setFire(int fire) {
        if (this.fire < fire){
            this.fire = fire;
        }
    }

    /**
     * 英雄机是否被其他飞行物碰到
     *
     * @param obj 其他的飞行物体
     * @return boolean
     */
    public boolean hit(FlyingObject obj) {
        // 判断两个矩形区域是否相交
        int x1 = obj.x - this.width / 2;
        int x2 = obj.x + obj.width + this.width / 2;
        int y1 = obj.y - this.height / 2;
        int y2 = obj.y + obj.height + this.width / 2;

        int x = this.x + this.width / 2;
        int y = this.y + this.height / 2;

        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }
}

package com.laidian.planewars;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hfb
 * @date 2018/11/21
 */
public class GameThread implements Runnable {

    /**
     * 游戏状态-启动
     */
    public static final int START = 0;
    /**
     * 游戏状态-运行
     */
    public static final int RUNNING = 1;
    /**
     * 游戏状态-暂停
     */
    public static final int PAUSE = 2;
    /**
     * 游戏状态-结束
     */
    public static final int GAME_OVER = 3;

    /**
     * 分数
     */
    private int score;

    /**
     * 当前状态
     */
    private int state = START;

    /**
     * 英雄机
     */
    private Hero hero;
    /**
     * 飞行物
     */
    private List<FlyingObject> flyings;
    /**
     * 子弹
     */
    private List<Bullet> bullets;

    private GamePanel gamePanel;

    public GameThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.hero = new Hero(gamePanel);
        this.flyings = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public List<FlyingObject> getFlyings() {
        return flyings;
    }

    public void setFlyings(List<FlyingObject> flyings) {
        this.flyings = flyings;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    /**
     * 生成一个飞行物体
     *
     * @return 返回敌机或者蜜蜂
     */
    private FlyingObject nextOne() {
        int type = FlyingObject.random.nextInt(20);
        if (type == 0) {
            return new Bee(gamePanel);
        } else {
            return new Airplane(gamePanel);
        }
    }

    private long flyEnteredIndex = 0;

    /**
     * 生成敌机动作
     */
    private void enterAction() {
        flyEnteredIndex++;
        int rate = 40;
        if (flyEnteredIndex % rate == 0) {
            FlyingObject one = nextOne();
            flyings.add(one);
        }

    }

    /**
     * 飞行物和子弹移动
     */
    private void stepAction() {
        hero.step();
        for (FlyingObject flying : flyings) {
            flying.step();
        }
        for (Bullet bullet : bullets) {
            bullet.step();
        }
    }

    private long shootIndex = 0;

    /**
     * 射击动作
     */
    private void shootAction() {
        shootIndex++;
        int rate = 20;
        if (shootIndex % rate == 0) {
            Bullet[] bs = hero.shoot();
            bullets.addAll(Arrays.asList(bs));
        }
    }

    /**
     * 越界检测
     */
    private void outOfBoundsAction() {
        for (FlyingObject f : flyings) {
            if (f.outOfBounds()) {
                flyings.remove(f);
            }
        }
        for (Bullet b : bullets) {
            if (b.outOfBounds()) {
                bullets.remove(b);
            }
        }
    }

    /**
     * 子弹撞击检测
     */
    private void hitAction() {
        for (Bullet b : bullets) {
            hitBy(b);
        }
    }

    /**
     * 判断子弹是否击中敌机
     *
     * @param b 子弹
     */
    private void hitBy(Bullet b) {
        FlyingObject shooted = null;
        for (FlyingObject flying : flyings) {
            if (flying.shootBy(b)){
                shooted = flying;
                flyings.remove(shooted);
                bullets.remove(b);
                break;
            }
        }
        if (shooted != null) {
            if (shooted instanceof Enemy) {
                Enemy e = (Enemy) shooted;
                score += e.getScore();
            }
            if (shooted instanceof Award) {
                Award a = (Award) shooted;
                int type = a.getType();
                switch (type) {
                    case Award.AWARD_TYPE_DOUBLE_FIRE:
                        hero.setFire(2);
                        break;
                    case Award.AWARD_TYPE_TREBLE_FIRE:
                        hero.setFire(3);
                        break;
                    case Award.AWARD_TYPE_FOURFOLD_FIRE:
                        hero.setFire(4);
                        break;
                    case Award.AWARD_TYPE_LIFE:
                        hero.addLife();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 检测是否游戏结束
     */
    private void checkGameOverAction() {
        if (isGameOver()) {
            state = GAME_OVER;
        }
    }

    /**
     * 判断是否游戏结束
     *
     * @return boolean
     */
    private boolean isGameOver() {
        for (FlyingObject flying : flyings) {
            //相撞了
            if (hero.hit(flying)) {
                //减命
                hero.subtractLife();
                // 重置火力值
                hero.resetFire();
                // 飞行物消失
                flyings.remove(flying);
            }
        }
        // 如果英雄的生命值为0就game over
        return hero.getLife() <= 0;
    }


    @Override
    public void run() {
        while (true){
            if (state == RUNNING) {
                //敌人（敌机+小蜜蜂）入场
                enterAction();
                //飞行物走一步
                stepAction();
                //子弹入场（英雄级发射子弹）
                shootAction();
                //删除越界的敌人（敌人）
                outOfBoundsAction();
                //子弹与敌人的碰撞
                hitAction();
                //英雄机与敌人相撞
                checkGameOverAction();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

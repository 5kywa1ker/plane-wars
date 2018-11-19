package com.laidian.planewars;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author hfb
 * @date 2018/10/26
 */
public class GamePanel extends JPanel {
    private static BufferedImage background;
    public static BufferedImage gameover;
    public static BufferedImage pause;
    public static BufferedImage start;

    static {
        try {
            background = ImageIO.read(GamePanel.class.getResourceAsStream("/img/background.png"));
            gameover = ImageIO.read(GamePanel.class.getResourceAsStream("/img/gameover.png"));
            pause = ImageIO.read(GamePanel.class.getResourceAsStream("/img/pause.png"));
            start = ImageIO.read(GamePanel.class.getResourceAsStream("/img/start.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 窗口宽
     */
    public static final int WIDTH = 400;
    /**
     * 窗口高
     */
    public static final int HEIGHT = 654;

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
    private Hero hero = new Hero();
    /**
     * 飞行物
     */
    private FlyingObject[] flyings = {};
    /**
     * 子弹
     */
    private Bullet[] bullets = {};

    /**
     * 画对象
     *
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.drawImage(background, 0, 0, null);
        paintHero(g2d);
        paintFlyingObjects(g2d);
        paintBullets(g2d);
        paintScoreAndLife(g2d);
        paintState(g2d);
    }

    /**
     * 画英雄机
     *
     * @param g
     */
    private void paintHero(Graphics g) {
        g.drawImage(hero.image, hero.x, hero.y, null);
    }

    /**
     * 画其他飞行物体
     *
     * @param g
     */
    private void paintFlyingObjects(Graphics g) {
        for (FlyingObject f : flyings) {
            g.drawImage(f.image, f.x, f.y, null);
        }
    }

    /**
     * 画子弹
     *
     * @param g
     */
    private void paintBullets(Graphics g) {
        for (Bullet b : bullets) {
            g.drawImage(b.image, b.x, b.y, null);
        }
    }

    /**
     * 画分数和生命值
     *
     * @param g
     */
    private void paintScoreAndLife(Graphics g) {
        g.setColor(new Color(0x696969));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        g.drawString("SCORE: " + score, 10, 25);
        g.drawString("LIFE: " + hero.getLife(), 10, 45);
    }

    /**
     * 画游戏状态
     *
     * @param g
     */
    private void paintState(Graphics g) {
        switch (state) {
            case START:
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pause, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(gameover, 0, 0, null);
                break;
            default:
                break;
        }
    }

    /**
     * 生成一个飞行物体
     *
     * @return 返回敌机或者蜜蜂
     */
    public FlyingObject nextOne() {
        int type = FlyingObject.random.nextInt(20);
        if (type == 0) {
            return new Bee();
        } else {
            return new Airplane();
        }
    }

    int flyEnteredIndex = 0;

    /**
     * 生成敌机动作
     */
    public void enterAction() {
        flyEnteredIndex++;
        int rate = 40;
        if (flyEnteredIndex % rate == 0) {
            FlyingObject one = nextOne();
            flyings = Arrays.copyOf(flyings, flyings.length + 1);
            flyings[flyings.length - 1] = one;
        }

    }

    /**
     * 飞行物和子弹移动
     */
    public void stepAction() {
        hero.step();
        for (FlyingObject flying : flyings) {
            flying.step();
        }
        for (Bullet bullet : bullets) {
            bullet.step();
        }
    }

    int shootIndex = 0;

    /**
     * 射击动作
     */
    public void shootAction() {
        shootIndex++;
        int rate = 30;
        if (shootIndex % rate == 0) {
            Bullet[] bs = hero.shoot();
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
        }
    }

    /**
     * 越界检测
     */
    public void outOfBoundsAction() {
        int index = 0;
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];

        for (FlyingObject f : flyings) {
            if (!f.outOfBounds()) {
                flyingLives[index] = f;
                index++;
            }
        }
        flyings = Arrays.copyOf(flyingLives, index);

        index = 0;
        Bullet[] bulletLives = new Bullet[bullets.length];
        for (Bullet b : bullets) {
            if (!b.outOfBounds()) {
                bulletLives[index] = b;
                index++;
            }
        }
        bullets = Arrays.copyOf(bulletLives, index);
    }

    /**
     * 子弹撞击检测
     */
    public void bangAction() {
        for (Bullet b : bullets) {
            bang(b);
        }
    }

    /**
     * 判断子弹是否击中敌机
     *
     * @param b 子弹
     */
    public void bang(Bullet b) {
        //存储被撞敌人的下标
        int index = -1;
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (f.shootBy(b)) {
                //记录敌人的下标
                index = i;
                break;
            }
        }
        if (index != -1) {
            FlyingObject one = flyings[index];
            if (one instanceof Enemy) {
                Enemy e = (Enemy) one;
                score += e.getScore();
            }
            if (one instanceof Award) {
                Award a = (Award) one;
                int type = a.getType();
                switch (type) {
                    case Award.AWARD_TYPE_DOUBLE_FIRE:
                        hero.addFire(2);
                        break;
                    case Award.AWARD_TYPE_LIFE:
                        hero.addLife();
                        break;
                    default:
                        break;
                }
            }
            /*将被撞敌人与数组最后一个元素交换*/
            FlyingObject t = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            flyings[flyings.length - 1] = t;
            /*缩容*/
            flyings = Arrays.copyOf(flyings, flyings.length - 1);
        }
    }

    /**
     * 检测是否游戏结束
     */
    public void checkGameOverAction() {
        if (isGameOver()) {
            state = GAME_OVER;
        }
    }

    /**
     * 判断是否游戏结束
     *
     * @return boolean
     */
    public boolean isGameOver() {
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            //相撞了
            if (hero.hit(f)) {
                //减命
                hero.subtractLife();
                // 重置火力值
                hero.resetFire();
                // 飞行物消失
                FlyingObject t = flyings[i];
                flyings[i] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;

                flyings = Arrays.copyOf(flyings, flyings.length - 1);
            }
        }
        // 如果英雄的生命值为0就game over
        return hero.getLife() <= 0;
    }

    public void action() {
        MouseAdapter l = new MouseAdapter() {
            /*鼠标移动事件*/
            @Override
            public void mouseMoved(MouseEvent e) {
                if (state == RUNNING) {
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (state) {
                    case START:
                        state = RUNNING;
                        break;
                    case GAME_OVER:
                        score = 0;
                        hero = new Hero();
                        flyings = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state = START;
                        break;
                    default:
                        break;
                }
            }

            /*重写鼠标移出*/
            @Override
            public void mouseExited(MouseEvent e) {
                if (state == RUNNING) {
                    //运行状态时
                    //改为暂停状态
                    state = PAUSE;
                }
            }

            /*重写鼠标移入事件*/
            @Override
            public void mouseEntered(MouseEvent e) {
                if (state == PAUSE) {
                    state = RUNNING;
                }
            }
        };
        this.addMouseListener(l);
        this.addMouseMotionListener(l);
        Timer timer = new Timer();
        int interval = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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
                    bangAction();
                    //英雄机与敌人相撞
                    checkGameOverAction();
                }
                //重画--调用paint()方法
                repaint();
            }
        }, interval, interval);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("飞机大战");
        GamePanel game = new GamePanel();
        frame.add(game);
        frame.setSize(WIDTH,HEIGHT);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.action();

    }


}

package com.laidian.planewars;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static com.laidian.planewars.GameThread.*;

/**
 * 游戏界面
 * @author hfb
 * @date 2018/10/26
 */
public class GamePanel extends JPanel implements Runnable {
    private static final long serialVersionUID = -8818837110172218333L;
    private static BufferedImage background;
    private static BufferedImage gameover;
    private static BufferedImage pause;
    private static BufferedImage start;
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
     * 游戏核心线程
     */
    private GameThread gameThread;


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

    public GamePanel(int width, int height) {
        this.addListener();
        this.setSize(width, height);
        this.gameThread = new GameThread(this);
        this.hero = gameThread.getHero();
        this.flyings = gameThread.getFlyings();
        this.bullets = gameThread.getBullets();
    }

    public GameThread getGameThread() {
        return gameThread;
    }

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

        paintBackground(g2d);
        paintHero(g2d);
        paintFlyingObjects(g2d);
        paintBullets(g2d);
        paintScoreAndLife(g2d);
        paintState(g2d);
    }

    /**
     * 平铺某张图片
     * @param g 画笔
     * @param image 图片
     */
    private void repeatImage(Graphics g, BufferedImage image) {
        int width = this.getWidth();
        int height = this.getHeight();
        int m = width / image.getWidth() + 1;
        int n = height / image.getHeight() + 1;
        int y = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                g.drawImage(image, j*image.getWidth(), y, null);
            }
            y += image.getHeight();
        }
    }

    /**
     * 居中绘制某张图片
     * @param g 画笔
     * @param image 图片
     */
    private void centerImage(Graphics g, BufferedImage image) {
        int x = Math.abs(image.getWidth() - this.getWidth()) / 2;
        int y = Math.abs(image.getHeight() - this.getHeight()) / 2;
        g.drawImage(image, x, y, null);
    }

    /**
     * 画背景
     * @param g
     */
    private void paintBackground(Graphics g) {
        repeatImage(g, background);
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
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        g.drawString("SCORE: " + gameThread.getScore(), 10, 25);
        g.drawString("LIFE: " + hero.getLife(), 10, 45);
    }
    /**
     * 画游戏状态
     *
     * @param g
     */
    private void paintState(Graphics g) {
        switch (gameThread.getState()) {
            case START:
                centerImage(g, start);
                break;
            case PAUSE:
                repeatImage(g, pause);
                break;
            case GAME_OVER:
                repeatImage(g, gameover);
                break;
            default:
                break;
        }
    }
    private void addListener() {
        GamePanel gamePanel = this;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            //鼠标移动事件
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameThread.getState() == RUNNING) {
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 右击
                if (e.isMetaDown()){
                    if (gameThread.getState() == RUNNING){
                        gameThread.setState(PAUSE);
                        return;
                    }
                }else {
                    // 单击事件 如果是暂停或者开始，则改为运行状态，如果是游戏结束，则改为开始状态
                    switch (gameThread.getState()) {
                        case PAUSE:
                        case START:
                            gameThread.setState(RUNNING);
                            break;
                        case GAME_OVER:
                            gameThread.setScore(0);
                            gameThread.setHero(new Hero(gamePanel));
                            flyings.clear();
                            bullets.clear();
                            gameThread.setState(START);
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

    }

    @Override
    public void run() {
        while (true){
            repaint();
            try {
                Thread.sleep(1000/70);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}

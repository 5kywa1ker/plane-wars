package com.laidian.planewars;

/**
 * 奖励
 *
 * @author hfb
 * @date 2018/10/25
 */
public interface Award {
    /**
     * 奖励类型-双倍火力
     */
    int AWARD_TYPE_DOUBLE_FIRE = 0;
    /**
     * 奖励类型-续命
     */
    int AWARD_TYPE_LIFE = 1;

    /**
     * 奖励类型-三倍火力
     */
    int AWARD_TYPE_TREBLE_FIRE = 2;

    /**
     * 奖励类型-四倍火力
     */
    int AWARD_TYPE_FOURFOLD_FIRE = 3;

    /**
     * 获取奖励类型
     *
     * @return Award.AWARD_TYPE_DOUBLE_FIRE or Award.AWARD_TYPE_LIFE
     */
    int getType();
}

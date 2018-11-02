package kakutou;

import java.util.Random;
import static kakutou.Chara.*;

public class AI {

    static final int MOVE_TIME = 10;
    Random rand;
    MainPanel m;
    int moveCount;

    public AI(MainPanel mainPanel) {
        rand = new Random();
        this.m = mainPanel;
        moveCount = 0;
    }

    void combo() {
        int cpu = m.cpu.state, player = m.player.state;

        //ウルトラコンボ時や、近い昇竜拳時は、ガードに専念
        if (m.player.ultra.exist || (player == SHOURYU && Math.abs(m.cpu.x - m.player.x) <= MainPanel.IMG_SIZE * 2 / 3)) {
            if (m.cpu.direction == LEFT) {
                m.cpu.right();
                moveCount = 0;
            } else {
                m.cpu.left();
                moveCount = 0;
            }
            return;
        }
        //波動拳ガード
        if ((m.player.hadou.x + 10 <= m.cpu.x + MainPanel.IMG_SIZE / 3 && m.cpu.x + MainPanel.IMG_SIZE / 3 <= m.player.hadou.x + Hadou.LENGTH - 10)
                || (m.player.hadou.x + 10 <= m.cpu.x + MainPanel.IMG_SIZE * 2 / 3 && m.cpu.x + MainPanel.IMG_SIZE * 2 / 3 <= m.player.hadou.x + Hadou.LENGTH - 10)) {
            if (m.cpu.onGround && m.player.hadou.exist) {
                if (m.cpu.direction == LEFT) {
                    m.cpu.right();
                    moveCount = 0;
                } else {
                    m.cpu.left();
                    moveCount = 0;
                }
                return;
            }
        }
        if (player == TATSUMAKI && Math.abs(m.cpu.x - m.player.x) < MainPanel.IMG_SIZE * 2 / 3 + 30
                && Math.abs(m.cpu.x - m.player.x) > MainPanel.IMG_SIZE * 2 / 3 -20 && m.cpu.onGround) {
            if (m.cpu.direction == LEFT) {
                m.cpu.right();
                moveCount = 0;
            } else {
                m.cpu.left();
                moveCount = 0;
            }
            return;
        }
        //移動の時間
        if (moveCount == MOVE_TIME) {
            m.cpu.notLeft();
            m.cpu.notRight();
            moveCount = 0;
        }
        if (m.cpu.right || m.cpu.left) {
            moveCount++;
        }
        //距離が近くて相手が波動拳なら、竜巻で迎え撃つ
        if (player == HADOU && Math.abs(m.cpu.x - m.player.x) < MainPanel.W * 8 / 27) {
            m.cpu.tatsumaki();
            return;
        }
        //必殺技で吹っ飛ばせたら、ウルコンへ。ライフが少なすぎてもウルコンへ。
        if (((cpu == TATSUMAKI || cpu == SHOURYU) && player == FUTTOBI)
                || m.cpu.life < LIFE_MAX / 7) {
            m.cpu.ultra();
        }
        //パンチ、キックからのコンボ
        if ((player == HIT || player == FUTTOBI) && (cpu == PUNCH || cpu == KICK || cpu == SIT_PUNCH)) {
            double num = rand.nextDouble();
            if (num < 0.6) {
                m.cpu.shouryu();
                return;
            } else if (num < 0.7) {
                m.cpu.notSit();
                m.cpu.kick();
                return;
            } else if (num < 0.8) {
                m.cpu.notSit();
                m.cpu.punch();
                return;
            } else if (num < 0.9) {
                m.cpu.sit();
                m.cpu.punch();
                return;
            } else {
                m.cpu.tatsumaki();
                return;
            }
        } else if (player == FUTTOBI) {//吹っ飛んでいるとき
            if (Math.abs(m.cpu.x - m.player.x) <= MainPanel.IMG_SIZE * 2 / 3) {//吹っ飛んでいて、近い時
                double num = rand.nextDouble();
                if (num < 0.6) {
                    m.cpu.shouryu();
                    return;
                } else if (num < 0.7) {
                    m.cpu.notSit();
                    m.cpu.kick();
                    return;
                } else if (num < 0.8) {
                    m.cpu.notSit();
                    m.cpu.punch();
                    return;
                } else {
                    m.cpu.tatsumaki();
                }
            } else {//吹っ飛んでいて、遠い時
                m.cpu.hadou();
                return;
            }
        }
        //最初の行動を生み出す
        if (Math.abs(m.cpu.x - m.player.x) < MainPanel.W / 2) {//近い時
            if (Math.abs(m.cpu.x - m.player.x) <= MainPanel.IMG_SIZE * 2 / 3 && player != FALL) {//中でもかなり近い時
                double num = rand.nextDouble();
                if (player == TATSUMAKI && m.cpu.onGround) {//相手が竜巻なら、昇竜で迎え撃つ
                    num = 0;
                }
                if (num < 0.2 && m.player.guard == NO_GUARD) {
                    m.cpu.shouryu();
                } else if (num < 0.4 && m.player.guard != GUARD) {
                    m.cpu.notSit();
                    m.cpu.punch();
                } else if (num < 0.6 && m.player.guard == NO_GUARD) {
                    m.cpu.notSit();
                    m.cpu.kick();
                } else if (num < 0.8 && m.player.guard != SIT_GUARD) {
                    m.cpu.sit();
                    m.cpu.punch();
                } else if (m.player.guard != SIT_GUARD) {
                    m.cpu.sit();
                    m.cpu.kick();
                }
            } else {//中でもまあ近いとき
                double num = rand.nextDouble();
                if (num < 0.02 && player != TATSUMAKI) {//相手が竜巻でないなら、波動拳
                    m.cpu.hadou();
                } else if (num < 0.2 && player != SIT && moveCount == 0) {
                    m.cpu.tatsumaki();
                } else if (num < 0.3) {
                    //近づく処理
                    if (m.cpu.direction == LEFT) {
                        m.cpu.left();
                        moveCount = 0;
                    } else {
                        m.cpu.right();
                        moveCount = 0;
                    }
                }
            }
        } else {//遠い時
            double num = rand.nextDouble();
            if (num < 0.4 && player != TATSUMAKI) {//相手が竜巻でないなら、波動拳
                m.cpu.hadou();
            } else if (num < 0.7) {
                //近づく処理
                if (m.cpu.direction == LEFT) {
                    m.cpu.left();
                    moveCount = 0;
                } else {
                    m.cpu.right();
                    moveCount = 0;
                }
            } else if (num < 0.85 && moveCount == 0) {
                m.cpu.tatsumaki();
            } else {
                //ジャンプしながら近づく
                m.cpu.jump();
                if (m.cpu.direction == LEFT) {
                    m.cpu.left();
                    moveCount = 0;
                } else {
                    m.cpu.right();
                    moveCount = 0;
                }
            }
        }
    }
}

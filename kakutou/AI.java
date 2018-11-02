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

        //�E���g���R���{����A�߂����������́A�K�[�h�ɐ�O
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
        //�g�����K�[�h
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
        //�ړ��̎���
        if (moveCount == MOVE_TIME) {
            m.cpu.notLeft();
            m.cpu.notRight();
            moveCount = 0;
        }
        if (m.cpu.right || m.cpu.left) {
            moveCount++;
        }
        //�������߂��đ��肪�g�����Ȃ�A�����Ō}������
        if (player == HADOU && Math.abs(m.cpu.x - m.player.x) < MainPanel.W * 8 / 27) {
            m.cpu.tatsumaki();
            return;
        }
        //�K�E�Z�Ő�����΂�����A�E���R���ցB���C�t�����Ȃ����Ă��E���R���ցB
        if (((cpu == TATSUMAKI || cpu == SHOURYU) && player == FUTTOBI)
                || m.cpu.life < LIFE_MAX / 7) {
            m.cpu.ultra();
        }
        //�p���`�A�L�b�N����̃R���{
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
        } else if (player == FUTTOBI) {//�������ł���Ƃ�
            if (Math.abs(m.cpu.x - m.player.x) <= MainPanel.IMG_SIZE * 2 / 3) {//�������ł��āA�߂���
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
            } else {//�������ł��āA������
                m.cpu.hadou();
                return;
            }
        }
        //�ŏ��̍s���𐶂ݏo��
        if (Math.abs(m.cpu.x - m.player.x) < MainPanel.W / 2) {//�߂���
            if (Math.abs(m.cpu.x - m.player.x) <= MainPanel.IMG_SIZE * 2 / 3 && player != FALL) {//���ł����Ȃ�߂���
                double num = rand.nextDouble();
                if (player == TATSUMAKI && m.cpu.onGround) {//���肪�����Ȃ�A�����Ō}������
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
            } else {//���ł��܂��߂��Ƃ�
                double num = rand.nextDouble();
                if (num < 0.02 && player != TATSUMAKI) {//���肪�����łȂ��Ȃ�A�g����
                    m.cpu.hadou();
                } else if (num < 0.2 && player != SIT && moveCount == 0) {
                    m.cpu.tatsumaki();
                } else if (num < 0.3) {
                    //�߂Â�����
                    if (m.cpu.direction == LEFT) {
                        m.cpu.left();
                        moveCount = 0;
                    } else {
                        m.cpu.right();
                        moveCount = 0;
                    }
                }
            }
        } else {//������
            double num = rand.nextDouble();
            if (num < 0.4 && player != TATSUMAKI) {//���肪�����łȂ��Ȃ�A�g����
                m.cpu.hadou();
            } else if (num < 0.7) {
                //�߂Â�����
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
                //�W�����v���Ȃ���߂Â�
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

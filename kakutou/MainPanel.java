package kakutou;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel implements Runnable {

    static final int W = 1000, H = 700,
            GROUND_H = H - 100,
            IMG_SIZE = 256,//キャラクターの画像サイズ
            GAME_SPEED = 20,//1ループで何秒sleepするか
            ATTACK_IMG_LENGTH = 240,//攻撃の画像サイズ
            YOU_WIN = 1, YOU_LOSE = -1, DRAW = 0,
            START_TIME = 80, END_TIME = 200;//開始までの時間と、終了後の時間
    static final Color TRANS_COLOR = new Color(0, 255, 0);
    Chara player, cpu;
    Image sit[], stand[], punch[][], jump[][], foward[], back[], kick[][], hit[][],
            sitPunch[][], sitKick[][], attack[][], fire[], hadou[][], fall[][],
            shouryu[][], jumpKick[][], jumpPunch[][], land[][], tatsumaki[], ora,
            guard[][], sitGuard[][], ultra[][], ultraFire, ultraBack, stage, ultraHit, win[][], lose[][];
    boolean gameover;
    public static AudioClip dageki, ultraSE, guardSE, bgm, start, explosion;
    int winOrLose;
    int startCount, endCount;
    AI ai;

    public MainPanel() {
        setPreferredSize(new Dimension(W, H));
        gameover = true;
        imageLoad();
        dageki = Applet.newAudioClip(getClass().getResource("se/dageki.wav"));
        ultraSE = Applet.newAudioClip(getClass().getResource("se/ultra.wav"));
        guardSE = Applet.newAudioClip(getClass().getResource("se/guard.wav"));
        bgm = Applet.newAudioClip(getClass().getResource("se/bgm.wav"));
        start = Applet.newAudioClip(getClass().getResource("se/start.wav"));
        explosion = Applet.newAudioClip(getClass().getResource("se/explosion.wav"));
        ai = new AI(this);
        new Thread(this).start();
    }

    private void imageLoad() {
        attack = new Image[2][3];
        land = new Image[2][3];
        sit = new Image[2];
        stand = new Image[2];
        fire = new Image[2];
        foward = new Image[2];
        back = new Image[2];
        punch = new Image[2][11];
        kick = new Image[2][11];
        hadou = new Image[2][11];
        jump = new Image[2][10];
        hit = new Image[2][5];
        win = new Image[2][5];
        fall = new Image[2][26];
        sitKick = new Image[2][10];
        sitPunch = new Image[2][8];
        shouryu = new Image[2][13];
        lose = new Image[2][19];
        ultra = new Image[2][17];
        jumpKick = new Image[2][10];
        jumpPunch = new Image[2][10];
        tatsumaki = new Image[48];
        guard = new Image[2][4];
        sitGuard = new Image[2][4];
        ora = new ImageIcon(getClass().getResource("picture/ora.gif")).getImage();
        ultraHit = new ImageIcon(getClass().getResource("picture/ultraHit.gif")).getImage();
        stage = new ImageIcon(getClass().getResource("picture/stage.png")).getImage();
        ultraBack = new ImageIcon(getClass().getResource("picture/ultraBack.gif")).getImage();
        ultraFire = new ImageIcon(getClass().getResource("picture/ultraFire.gif")).getImage();
        for (int j = 0; j < 48; j++) {
            tatsumaki[j] = new ImageIcon(getClass().getResource("picture/tatsumaki_" + j + ".gif")).getImage();
        }
        for (int i = 0; i < 2; i++) {
            sit[i] = new ImageIcon(getClass().getResource("picture/sit" + i + ".gif")).getImage();
            fire[i] = new ImageIcon(getClass().getResource("picture/fire" + i + ".gif")).getImage();
            back[i] = new ImageIcon(getClass().getResource("picture/back" + i + ".gif")).getImage();
            foward[i] = new ImageIcon(getClass().getResource("picture/foward" + i + ".gif")).getImage();
            stand[i] = new ImageIcon(getClass().getResource("picture/stand" + i + ".gif")).getImage();
            for (int j = 0; j < 4; j++) {
                guard[i][j] = new ImageIcon(getClass().getResource("picture/guard" + i + "_" + j + ".gif")).getImage();
                sitGuard[i][j] = new ImageIcon(getClass().getResource("picture/sitGuard" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 10; j++) {
                jump[i][j] = new ImageIcon(getClass().getResource("picture/jump" + i + "_" + j + ".gif")).getImage();
                sitKick[i][j] = new ImageIcon(getClass().getResource("picture/sitKick" + i + "_" + j + ".gif")).getImage();
                jumpKick[i][j] = new ImageIcon(getClass().getResource("picture/jumpKick" + i + "_" + j + ".gif")).getImage();
                jumpPunch[i][j] = new ImageIcon(getClass().getResource("picture/jumpPunch" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 11; j++) {
                punch[i][j] = new ImageIcon(getClass().getResource("picture/punch" + i + "_" + j + ".gif")).getImage();
                kick[i][j] = new ImageIcon(getClass().getResource("picture/kick" + i + "_" + j + ".gif")).getImage();
                hadou[i][j] = new ImageIcon(getClass().getResource("picture/hadou" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 8; j++) {
                sitPunch[i][j] = new ImageIcon(getClass().getResource("picture/sitPunch" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 3; j++) {
                attack[i][j] = new ImageIcon(getClass().getResource("picture/attack" + i + "_" + j + ".gif")).getImage();
                land[i][j] = new ImageIcon(getClass().getResource("picture/land" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 26; j++) {
                fall[i][j] = new ImageIcon(getClass().getResource("picture/fall" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 5; j++) {
                hit[i][j] = new ImageIcon(getClass().getResource("picture/hit" + i + "_" + j + ".gif")).getImage();
                win[i][j] = new ImageIcon(getClass().getResource("picture/win" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 13; j++) {
                shouryu[i][j] = new ImageIcon(getClass().getResource("picture/shouryu" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 19; j++) {
                lose[i][j] = new ImageIcon(getClass().getResource("picture/lose" + i + "_" + j + ".gif")).getImage();
            }
            for (int j = 0; j < 17; j++) {
                ultra[i][j] = new ImageIcon(getClass().getResource("picture/ultra" + i + "_" + j + ".gif")).getImage();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (!gameover) {
            drawStage(g);
            drawCountDown(g);
            drawWinOrLose(g);
            drawChara(g, cpu);
            drawChara(g, player);
            drawLife(g);
            drawFire(g);
            drawAttack(g, player, cpu);
            drawAttack(g, cpu, player);
            drawOra(g);
        } else {
            drawStart(g);
        }
    }

    /**
     * 初期化する関数
     */
    void init() {
        if (gameover) {
            player = new Chara(100, GROUND_H - IMG_SIZE, Chara.RIGHT);
            cpu = new Chara(W - 100 - IMG_SIZE, GROUND_H - IMG_SIZE, Chara.LEFT);
            startCount = START_TIME;
            endCount = END_TIME;
            bgm.loop();
            gameover = false;
        }
    }

    void drawCountDown(Graphics g) {
        if (startCount != 0) {
            g.setFont(new Font(null, Font.BOLD, 100));
            g.drawString("" + startCount / 20, W / 2 - 25, H / 2);
        }
    }

    void drawWinOrLose(Graphics g) {
        if (cpu.winCount > 0 || player.winCount > 0 || player.loseCount > 0 || cpu.loseCount > 0) {
            g.setFont(new Font(null, Font.BOLD, 50));
            String str = "";
            switch (winOrLose) {
                case YOU_WIN:
                    str = "YOU WIN";
                    break;
                case YOU_LOSE:
                    str = "YOU LOSE";
                    break;
                case DRAW:
                    str = "DRAW";
                    break;
            }
            g.drawString(str, W / 2 - 50 * 2, H / 2);
        }
    }

    void drawStart(Graphics g) {
        g.clearRect(0, 0, W, H);
        g.setColor(Color.black);
        g.setFont(new Font(null, Font.BOLD, 50));
        g.drawString("ENTER : スタート", W / 2 - 200, H / 2 - 200);
        g.setColor(Color.blue);
        g.drawString("A : パンチ", W / 2 - 100, H / 2 + 50 - 100);
        g.drawString("S : キック", W / 2 - 100, H / 2 + 50 * 2 - 100);
        g.drawString("D : 波動", W / 2 - 100, H / 2 + 50 * 3 - 100);
        g.drawString("F : 昇竜", W / 2 - 100, H / 2 + 50 * 4 - 100);
        g.drawString("G : 竜巻", W / 2 - 100, H / 2 + 50 * 5 - 100);
        g.setColor(Color.red);
        g.drawString("SPACE : ウルトラコンボ", W / 2 - 280, H / 2 + 50 * 6 - 100);
        g.setFont(new Font(null, Font.BOLD, 30));
        g.drawString("(ライフが赤の時 1回のみ使用可能)", W / 2 - 230, H / 2 + 50 * 6 + 30 - 100);

    }

    void drawStage(Graphics g) {
        if ((0 < cpu.ultraCount && cpu.ultraCount < 11 * 2 && cpu.ultra.exist)
                || (0 < player.ultraCount && player.ultraCount < 11 * 2 && player.ultra.exist)) {
            g.setColor(Color.black);
            g.fillRect(0, 0, W, H);
            g.drawImage(ultraBack, 0, 0, this);
        } else {
            g.drawImage(stage, 0, -250, this);
        }
    }

    /**
     * 波動とウルコンの波動の描画
     */
    void drawFire(Graphics g) {
        if (player.hadou.exist) {
            g.drawImage(fire[(player.hadou.left) ? 0 : 1], player.hadou.x, player.hadou.y, this);
        }
        if (cpu.hadou.exist) {
            g.drawImage(fire[(cpu.hadou.left) ? 0 : 1], cpu.hadou.x, cpu.hadou.y, this);
        }
        if (player.ultra.exist) {
            g.drawImage(ultraFire, player.ultra.x, player.ultra.y, this);
        }
        if (cpu.ultra.exist) {
            g.drawImage(ultraFire, cpu.ultra.x, cpu.ultra.y, this);
        }
    }

    void drawLife(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(null, Font.BOLD, 30));
        g.drawString("YOU", 10, 70);
        g.drawString("CPU", W - 75, 70);
        if (player.life < Chara.LIFE_MAX / 3) {
            g.setColor(Color.red);
        } else if (player.life == Chara.LIFE_MAX) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillRect(10, 10, player.life, 30);
        if (cpu.life < Chara.LIFE_MAX / 3) {
            g.setColor(Color.red);
        } else if (cpu.life == Chara.LIFE_MAX) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillRect(W - cpu.life - 10, 10, cpu.life, 30);
    }

    /**
     * 敵と区別するために描くオーラ
     */
    void drawOra(Graphics g) {
        g.drawImage(ora, cpu.x + 8, cpu.y + 25, this);
    }

    /**
     * 攻撃をくらったときの火花の描画
     *
     * @param c1 攻撃したキャラ
     * @param c2 攻撃されたキャラ
     */
    void drawAttack(Graphics g, Chara c1, Chara c2) {
        //ウルコンの時のみ特別
        if (c1.ultra.hitCount > 0 && c1.ultra.exist) {
            g.drawImage(ultraHit, c2.x + 8, c2.y + 8, this);
        }
        //その他
        if (c2.state == Chara.HIT || c2.attackCount >= 0) {
            switch (c1.state) {
                case Chara.PUNCH:
                    if (c1.direction == Chara.RIGHT) {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE - ATTACK_IMG_LENGTH / 2 - 25, c1.y, this);
                    } else {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x - ATTACK_IMG_LENGTH / 2 + 25, c1.y, this);
                    }
                    break;
                case Chara.SIT_PUNCH:
                    if (c1.direction == Chara.RIGHT) {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE - ATTACK_IMG_LENGTH / 2 - 25, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    } else {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x - ATTACK_IMG_LENGTH / 2 + 25, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    }
                    break;
                case Chara.KICK:
                case Chara.TATSUMAKI:
                    if (c1.direction == Chara.RIGHT) {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE - ATTACK_IMG_LENGTH / 2, c1.y, this);
                    } else {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x - ATTACK_IMG_LENGTH / 2, c1.y, this);
                    }
                    break;
                case Chara.SIT_KICK:
                case Chara.JUMP_PUNCH:
                    if (c1.direction == Chara.RIGHT) {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE - ATTACK_IMG_LENGTH / 2 - 20, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    } else {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x - ATTACK_IMG_LENGTH / 2 + 20, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    }
                    break;
                case Chara.SHOURYU:
                case Chara.JUMP_KICK:
                    if (c1.direction == Chara.RIGHT) {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE * 2 / 3 - ATTACK_IMG_LENGTH / 2, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    } else {
                        g.drawImage(attack[c1.direction][c2.attackCount / 2], c1.x + IMG_SIZE / 3 - ATTACK_IMG_LENGTH / 2, c1.y + ATTACK_IMG_LENGTH / 3, this);
                    }
                    break;
            }
        }
    }

    void drawChara(Graphics g, Chara c) {
        switch (c.state) {
            case Chara.STAND:
                if (c.left) {
                    if (c.direction == 0) {
                        g.drawImage(foward[c.direction], c.x, c.y, this);
                    } else {
                        g.drawImage(back[c.direction], c.x, c.y, this);
                    }
                } else if (c.right) {
                    if (c.direction == 0) {
                        g.drawImage(back[c.direction], c.x, c.y, this);
                    } else {
                        g.drawImage(foward[c.direction], c.x, c.y, this);
                    }
                } else {
                    g.drawImage(stand[c.direction], c.x, c.y, this);
                }
                break;
            case Chara.SIT:
                g.drawImage(sit[c.direction], c.x, c.y, this);
                break;
            case Chara.PUNCH:
                g.drawImage(punch[c.direction][c.punchCount], c.x, c.y, this);
                break;
            case Chara.KICK:
                g.drawImage(kick[c.direction][c.kickCount], c.x, c.y, this);
                break;
            case Chara.JUMP:
                g.drawImage(jump[c.direction][c.jumpCount / 3], c.x, c.y, this);
                break;
            case Chara.SIT_KICK:
                g.drawImage(sitKick[c.direction][c.kickCount], c.x, c.y, this);
                break;
            case Chara.SIT_PUNCH:
                g.drawImage(sitPunch[c.direction][c.punchCount], c.x, c.y, this);
                break;
            case Chara.HADOU:
                g.drawImage(hadou[c.direction][c.superWaza / 2], c.x, c.y, this);
                break;
            case Chara.FALL:
            case Chara.FUTTOBI:
                g.drawImage(fall[c.direction][c.fallCount / 2], c.x, c.y, this);
                break;
            case Chara.HIT:
                g.drawImage(hit[c.direction][c.hitCount / 2], c.x, c.y, this);
                break;
            case Chara.SHOURYU:
                g.drawImage(shouryu[c.direction][c.superWaza / 2], c.x, c.y, this);
                break;
            case Chara.JUMP_PUNCH:
                g.drawImage(jumpPunch[c.direction][c.punchCount], c.x, c.y, this);
                break;
            case Chara.JUMP_KICK:
                g.drawImage(jumpKick[c.direction][c.kickCount], c.x, c.y, this);
                break;
            case Chara.LAND:
                g.drawImage(land[c.direction][c.superWaza / 2], c.x, c.y, this);
                break;
            case Chara.TATSUMAKI:
                g.drawImage(tatsumaki[c.superWaza], c.x, c.y, this);
                break;
            case Chara.GUARD:
                g.drawImage(guard[c.direction][c.guardCount], c.x, c.y, this);
                break;
            case Chara.SIT_GUARD:
                g.drawImage(sitGuard[c.direction][c.guardCount], c.x, c.y, this);
                break;
            case Chara.ULTRA:
                g.drawImage(ultra[c.direction][c.ultraCount / 2], c.x, c.y, this);
                break;
            case Chara.WIN:
                g.drawImage(win[c.direction][c.winCount / 3], c.x, c.y, this);
                break;
            case Chara.LOSE:
                g.drawImage(lose[c.direction][c.loseCount / 2], c.x, c.y, this);
                break;
        }
    }

    void winLose() {
        //ウルコンの波動が存在しなくなったら--->ウルコンが全てヒットしたら
        if (!cpu.ultra.exist && !player.ultra.exist) {
            if (cpu.life == 0 && player.life > 0) {
                player.setWin();
                cpu.setLose();
                winOrLose = YOU_WIN;
                endCount--;
            } else if (player.life == 0 && cpu.life > 0) {
                cpu.setWin();
                player.setLose();
                winOrLose = YOU_LOSE;
                endCount--;
            } else if (player.life == 0 && cpu.life == 0) {
                player.setLose();
                cpu.setLose();
                winOrLose = DRAW;
                endCount--;
            }
            //スタート画面に戻る
            if (endCount == 0) {
                gameover = true;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!gameover) {
                //開始までのカウントダウン
                if (startCount != 0) {
                    startCount--;
                }
                //ゴングを鳴らす
                if (startCount == 1) {
                    start.play();
                }
                //開始後
                if (startCount == 0) {
                    //ウルコン発動時、相手の動きいったん停止
                    if (!(0 < cpu.ultraCount && cpu.ultraCount <= 7 * 2 && cpu.ultra.exist)) {
                        player.update();
                    }
                    //ウルコン発動時、相手の動きいったん停止
                    if (!(0 < player.ultraCount && player.ultraCount <= 7 * 2 && player.ultra.exist)) {
                        cpu.update();
                    }
                    Chara.direction(player, cpu);
                    Hadou.utikeshi(cpu.hadou, player.hadou);
                    Ultra.utikeshi(player.ultra, cpu.ultra);
                    winLose();

                    if (cpu.state != Chara.WIN && player.state != Chara.WIN) {
                        ai.combo();
                    }
                    Attack.attack(player, cpu);
                }
            }
            repaint();
            try {
                Thread.sleep(GAME_SPEED);
            } catch (InterruptedException ex) {
            }
        }
    }
}

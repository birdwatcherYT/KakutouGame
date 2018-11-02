package kakutou;

public class Chara {

    static final int LIFE_MAX = 485,
            CHARA_HEIGHT = MainPanel.IMG_SIZE - 60, CHARA_WIDTH = MainPanel.IMG_SIZE / 3, CHARA_SIT_HEIGHT = MainPanel.IMG_SIZE - 115,
            STAND = 0, SIT = 1, PUNCH = 2, JUMP = 3, KICK = 4,
            SIT_KICK = 5, SIT_PUNCH = 6, HADOU = 7, FALL = 8, HIT = 9,
            SHOURYU = 10, JUMP_KICK = 11, JUMP_PUNCH = 12, FUTTOBI = 13,
            LAND = 14, TATSUMAKI = 15, ULTRA = 16, WIN = 17, LOSE = 18,//状態

            LEFT = 0, RIGHT = 1,//キャラの向き
            MOVE_SPEED = 10, JUMP_SPEED = 40, GRAVITY = 3,
            NO_GUARD = -1, GUARD = -2, SIT_GUARD = -3;
    int x, y, life, state, direction, vx, vy, punchCount, jumpCount, kickCount, tempState, superWaza, guardCount, ultraCount,
            attackCount,
            fallCount, hitCount,
            guard, winCount, loseCount;
    boolean onGround,
            left, right;//それぞれの矢印キーを押しているかどうか
    Hadou hadou;
    Ultra ultra;

    public Chara(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        vx = vy = 0;
        hadou = new Hadou();
        ultra = new Ultra();
        life = LIFE_MAX;
        onGround = true;
        left = right = false;
        state = STAND;
        guard = NO_GUARD;
        attackCount = -1;
        loseCount = winCount =  ultraCount = guardCount = hitCount
                = fallCount = superWaza = kickCount = jumpCount = punchCount = 0;
        this.direction = direction;
    }

    void ultra() {
        if (life >= LIFE_MAX / 3 || !ultra.ok) {
            return;
        }
        if (state != HIT && state != FALL && state != FUTTOBI && state != LAND && state != WIN && state != LOSE) {
            MainPanel.ultraSE.play();
            vx = 0;
            vy = 0;
            ultraCount = 0;
            tempState = STAND;
            if (direction == RIGHT) {
                ultra.setXY(x, y - Ultra.LENGTH / 2);
            } else {
                ultra.setXY(x, y - Ultra.LENGTH / 2);
            }
            ultra.setDirection(this);
            state = ULTRA;
        }
    }

    void land() {
        superWaza = 0;
        if (state != FALL && state != FUTTOBI) {
            tempState = STAND;
            state = LAND;
        }
    }

    void futtobi() {
        tempState = FUTTOBI;
        state = FUTTOBI;
        fallCount = 7 * 2;
        if (direction == LEFT) {
            vx = 10;
        } else {
            vx = -10;
        }
        vy = -30;
    }

    void hit() {
        attackCount = 0;
        hitCount = 0;
        if (state == SIT || state == SIT_KICK || state == SIT_PUNCH) {
            tempState = SIT;
        } else {
            tempState = STAND;
        }
        state = HIT;
        if (!onGround) {
            futtobi();
        }
        if (ultra.exist && ultraCount <= 11 * 2) {
            ultra.exist = false;
        }
    }

    void fall() {
        fallCount = 0;
        state = FALL;
    }

    void shouryu() {
        if (onGround && state != HADOU && state != HIT && state != FALL && state != SHOURYU && state != WIN && state != LOSE
                && state != LAND && state != TATSUMAKI && state != FUTTOBI && state != ULTRA) {
            superWaza = 0;
            vy = -44;
            if (direction == RIGHT) {
                vx = 14;
            } else {
                vx = -14;
            }
            onGround = false;
            tempState = STAND;
            state = SHOURYU;
        }
    }

    void tatsumaki() {
        if (state != HADOU && state != HIT && state != FALL && state != SHOURYU && state != LAND && state != WIN && state != LOSE
                && state != TATSUMAKI && state != FUTTOBI && superWaza != 48 && state != ULTRA) {
            superWaza = 0;
            y -= 50;
            vy = 0;
            if (direction == RIGHT) {
                vx = 15;
            } else {
                vx = -15;
            }
            onGround = false;
            tempState = STAND;
            state = TATSUMAKI;
        }
    }

    void hadou() {
        if (!hadou.exist && onGround && state != HADOU && state != HIT && state != FALL && state != WIN && state != LOSE
                && state != SHOURYU && state != LAND && state != TATSUMAKI && state != FUTTOBI && state != ULTRA) {
            superWaza = 0;
            tempState = STAND;
            if (direction == RIGHT) {
                hadou.setXY(x - 10, y);
            } else {
                hadou.setXY(x + 10, y);
            }
            hadou.setDirection(this);
            state = HADOU;
        }
    }

    void punch() {
        if (state == STAND && onGround) {
            punchCount = 0;
            tempState = state;
            state = PUNCH;
        } else if (state == SIT) {
            punchCount = 0;
            tempState = state;
            state = SIT_PUNCH;
        } else if (state == JUMP) {
            punchCount = 0;
            tempState = STAND;
            state = JUMP_PUNCH;
        }
    }

    void kick() {
        if (state == STAND && onGround) {
            kickCount = 0;
            tempState = state;
            state = KICK;
        } else if (state == SIT) {
            kickCount = 0;
            tempState = state;
            state = SIT_KICK;
        } else if (state == JUMP) {
            kickCount = 0;
            tempState = STAND;
            state = JUMP_KICK;
        }
    }

    void update() {
        //画面内に収まる処理
        if (x + MainPanel.IMG_SIZE / 3 < 0) {
            x = -MainPanel.IMG_SIZE / 3;
            if (state != TATSUMAKI) {
                vx = 0;
            }
        }
        if (x + MainPanel.IMG_SIZE * 2 / 3 > MainPanel.W) {
            x = MainPanel.W - MainPanel.IMG_SIZE * 2 / 3;
            if (state != TATSUMAKI) {
                vx = 0;
            }
        }
        //移動処理
        if (left && (state == JUMP || state == STAND)) {
            x -= MOVE_SPEED;
        }
        if (right && (state == JUMP || state == STAND)) {
            x += MOVE_SPEED;
        }
        x += vx;
        y += vy;
        vy += GRAVITY;
        //着地処理
        if (y + MainPanel.IMG_SIZE >= MainPanel.GROUND_H) {
            y = MainPanel.GROUND_H - MainPanel.IMG_SIZE;
            vy = 0;
            if (!onGround) {
                state = tempState;
                jumpCount = 0;
                vx = 0;
            }
            onGround = true;
        }
        //パンチ処理
        if (state == PUNCH || state == SIT_PUNCH || state == JUMP_PUNCH) {
            punchCount++;
        }
        if (punchCount == 11 && state == PUNCH) {
            state = tempState;
            punchCount = 0;
        }
        if (punchCount == 8 && state == SIT_PUNCH) {
            state = tempState;
            punchCount = 0;
        }
        if (punchCount == 10 && state == JUMP_PUNCH) {
            state = tempState;
            punchCount = 0;
        }
        //キック処理
        if (state == KICK || state == SIT_KICK || state == JUMP_KICK) {
            kickCount++;
        }
        if (kickCount == 11 && state == KICK) {
            state = tempState;
            kickCount = 0;
        }
        if (kickCount == 10 && state == SIT_KICK) {
            state = tempState;
            kickCount = 0;
        }
        if (kickCount == 10 && state == JUMP_KICK) {
            state = tempState;
            kickCount = 0;
        }
        //ジャンプ処理
        if (state == JUMP) {
            jumpCount++;
        }
        if (jumpCount == 10 * 3) {
            jumpCount = 0;
        }
        //アタック処理
        if (attackCount >= 0) {
            attackCount++;
        }
        if (attackCount == 3 * 2) {
            attackCount = -1;
        }
        //波動 & 昇竜 & 竜巻
        hadou.update(this);
        if (state == HADOU || state == SHOURYU || state == LAND || state == TATSUMAKI) {
            superWaza++;
        }
        if (state == TATSUMAKI) {
            vy = 0;
        }
        if (state == SHOURYU && superWaza == 7) {
            vx = 0;
        }
        if (superWaza == 11 * 2 && state == HADOU) {
            state = tempState;
            superWaza = 0;
        }
        if (state == TATSUMAKI && superWaza == 48) {//竜巻旋風脚後は着地まで何もできない(superWaza==48のとき)
            state = tempState;
            vx = 0;
        }
        if (superWaza == 13 * 2 && state == SHOURYU) {
            state = tempState;
        }
        if (superWaza == 13 * 2 && onGround) {//昇竜拳後の着地
            land();
        }
        if (superWaza == 48 && onGround) {//竜巻旋風脚後の着地
            superWaza = 0;
        }
        if (superWaza == 3 * 2 && state == LAND) {
            superWaza = 0;
            state = tempState;
        }
        //倒れ & 起き上がり & 吹っ飛び
        if (state == FALL || (state == FUTTOBI && onGround)) {
            fallCount++;
            state = FALL;
        }
        if (fallCount == 26 * 2) {
            state = STAND;
            fallCount = 0;
        }
        //ヒット時
        if (state == HIT) {
            hitCount++;
        }
        if (hitCount == 5 * 2) {
            state = tempState;
            hitCount = 0;
        }
        //ガード状態のセット
        guardSet();
        //ガード
        if (state == GUARD || state == SIT_GUARD) {
            guardCount++;
        }
        if ((state == GUARD || state == SIT_GUARD) && guardCount == 4) {
            guardCount = 0;
            state = tempState;
        }
        //ウルトラコンボ
        ultra.update(this);
        if (state == ULTRA) {
            vy = 0;
            ultraCount++;
        }
        if (ultraCount == 17 * 2 && state == ULTRA) {
            state = tempState;
        }
        
        //WIN
        if (state == WIN && onGround) {
            winCount++;
        }
        if (winCount == 5 * 3) {
            winCount = 5 * 3 - 1;
        }
        //LOSE
        if (state == LOSE) {
            loseCount++;
        }
        if (loseCount >= 19 * 2) {
            loseCount = 19 * 2 - 1;
        }
    }

    static void direction(Chara c1, Chara c2) {
        if (c1.x - c2.x > 0) {
            c1.direction = LEFT;
            c2.direction = RIGHT;
        } else {
            c1.direction = RIGHT;
            c2.direction = LEFT;
        }
        if (Math.abs(c1.y - c2.y) <= CHARA_HEIGHT && Math.abs(c1.x - c2.x) <= CHARA_WIDTH
                && (c1.state == STAND || c1.state == SIT || c1.state == JUMP)
                && (c2.state == STAND || c2.state == SIT || c2.state == JUMP)) {
            if (c1.direction == RIGHT) {
                c1.x -= 3;
                c2.x += 3;
            } else {
                c1.x += 3;
                c2.x -= 3;
            }
        }
    }

    void guardSet() {
        guard = NO_GUARD;
        if (direction == LEFT) {
            if (right) {
                if (state == STAND || state == GUARD) {
                    guard = GUARD;
                } else if (state == SIT || state == SIT_GUARD) {
                    guard = SIT_GUARD;
                }
            }
        } else {
            if (left) {
                if (state == STAND || state == GUARD) {
                    guard = GUARD;
                } else if (state == SIT || state == SIT_GUARD) {
                    guard = SIT_GUARD;
                }
            }
        }
    }

    void guard() {
        guardCount = 0;
        if (state == STAND && onGround) {
            tempState = STAND;
            state = GUARD;
        } else if (state == SIT && onGround) {
            tempState = SIT;
            state = SIT_GUARD;
        }
    }

    void setWin() {
        state = WIN;
    }

    void setLose() {
        state = LOSE;
    }

    void sit() {
        if (onGround && state == STAND) {
            state = SIT;
        }
    }

    void notSit() {
        if (state == SIT || state == SIT_KICK || state == SIT_PUNCH) {
            state = STAND;
        }
    }

    void left() {
        left = true;
        right = false;
    }

    void notLeft() {
        left = false;
    }

    void right() {
        left = false;
        right = true;
    }

    void notRight() {
        right = false;
    }

    void jump() {
        if (onGround && (state == STAND || state == SIT)) {
            tempState = state;
            onGround = false;
            state = JUMP;
            vy = -JUMP_SPEED;
        }
    }
}

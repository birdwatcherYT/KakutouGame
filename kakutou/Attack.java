package kakutou;

import java.util.Random;

public class Attack {

    static final int PUNCH_DAMAGE = 10, KICK_DAMAGE = 15,
            SIT_KICK_DAMAGE = 15, SIT_PUNCH_DAMAGE = 5,
            HADOU_DAMAGE = 10, SHOURYU_DAMAGE = 7, TATSUMAKI_DAMAGE = 4,
            ULTRA_DAMAGE = 5, ULTRA_HIT_MAX = 30,
            BACK = 5,//攻撃をあてて、どれだけ下がるか
            GUARD_BACK = 20;

    /**
     * @param c1 パンチするキャラ
     * @param c2 攻撃を受けるキャラ
     * @return 攻撃がヒットしたかどうか
     */
    static boolean punch(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL) {
            return false;
        }
        switch (c1.state) {
            case Chara.PUNCH:
                if (c1.punchCount == 7
                        && Math.abs(c1.y + MainPanel.IMG_SIZE / 2 - c2.y) < MainPanel.IMG_SIZE) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE - 20 > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.GUARD) {
                                c2.life -= PUNCH_DAMAGE;
                                c1.x -= BACK;
                                MainPanel.dageki.play();
                                c2.hit();
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x -= GUARD_BACK;
                                c2.guard();
                            }
                        }
                    } else {
                        if (c1.x + 20 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.GUARD) {
                                c2.life -= PUNCH_DAMAGE;
                                c2.hit();
                                MainPanel.dageki.play();
                                c1.x += BACK;
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x += GUARD_BACK;
                                c2.guard();
                            }
                        }
                    }
                }
                break;
            case Chara.SIT_PUNCH:
                if (c1.punchCount == 3
                        && Math.abs(c1.y + 160 - c2.y) < MainPanel.IMG_SIZE) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE - 25 > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.SIT_GUARD) {
                                c2.life -= SIT_PUNCH_DAMAGE;
                                c1.x -= BACK;
                                MainPanel.dageki.play();
                                c2.hit();
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x -= GUARD_BACK;
                                c2.guard();
                            }
                        }
                    } else {
                        if (c1.x + 25 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.SIT_GUARD) {
                                c2.life -= SIT_PUNCH_DAMAGE;
                                c1.x += BACK;
                                MainPanel.dageki.play();
                                c2.hit();
                                damage = true;
                            } else {
                                c1.x += GUARD_BACK;
                                MainPanel.guardSE.play();
                                c2.guard();
                            }
                        }
                    }
                }
                break;
            case Chara.JUMP_PUNCH:
                if (c1.punchCount == 4
                        && c2.y - c1.y < MainPanel.IMG_SIZE) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE - 10 > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.GUARD) {
                                MainPanel.dageki.play();
                                c2.life -= PUNCH_DAMAGE;
                                c2.hit();
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x -= GUARD_BACK;
                                c2.guard();
                            }
                        }
                    } else {
                        if (c1.x + 10 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.GUARD) {
                                c2.life -= PUNCH_DAMAGE;
                                MainPanel.dageki.play();
                                c2.hit();
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x += GUARD_BACK;
                                c2.guard();
                            }
                        }
                    }
                }
                break;
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    /**
     * @param c1 キックするキャラ
     * @param c2 キックされる側
     */
    static boolean kick(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL) {
            return false;
        }
        switch (c1.state) {
            case Chara.KICK:
                if (c1.kickCount == 5
                        && Math.abs(c1.y + 80 - c2.y) < MainPanel.IMG_SIZE) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.GUARD && c2.guard != Chara.SIT_GUARD) {
                                c2.life -= KICK_DAMAGE;
                                c1.x -= BACK;
                                MainPanel.dageki.play();
                                c2.hit();
                                damage = true;
                            } else {
                                c1.x -= GUARD_BACK;
                                MainPanel.guardSE.play();
                                c2.guard();
                            }
                        }
                    } else {
                        if (c1.x < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.GUARD && c2.guard != Chara.SIT_GUARD) {
                                c2.life -= KICK_DAMAGE;
                                c2.hit();
                                c1.x += BACK;
                                MainPanel.dageki.play();
                                damage = true;
                            } else {
                                MainPanel.guardSE.play();
                                c1.x += GUARD_BACK;
                                c2.guard();
                            }
                        }
                    }
                }
                break;
            case Chara.SIT_KICK:
                if (c1.kickCount == 5 && c2.onGround) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE - 20 > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.SIT_GUARD) {
                                c2.life -= SIT_KICK_DAMAGE;
                                MainPanel.dageki.play();
                                damage = true;
                                c2.hit();
                                c2.fall();
                            } else {
                                c1.x -= GUARD_BACK;
                                MainPanel.guardSE.play();
                                c2.guard();
                            }
                        }
                    } else {
                        if (c1.x + 20 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.SIT_GUARD) {
                                c2.life -= SIT_KICK_DAMAGE;
                                c2.hit();
                                c2.fall();
                                MainPanel.dageki.play();
                                damage = true;
                            } else {
                                c1.x += GUARD_BACK;
                                MainPanel.guardSE.play();
                                c2.guard();
                            }
                        }
                    }
                }
                break;
            case Chara.JUMP_KICK:
                if (c1.kickCount == 5
                        && c2.y - c1.y < 200) {
                    if (c1.direction == Chara.RIGHT) {
                        if (c1.x + MainPanel.IMG_SIZE - 30 > c2.x + MainPanel.IMG_SIZE / 3) {
                            if (c2.guard != Chara.GUARD) {
                                c2.life -= KICK_DAMAGE;
                                MainPanel.dageki.play();
                                damage = true;
                                c2.hit();
                            } else {
                                c1.x -= GUARD_BACK;
                                c2.guard();
                                MainPanel.guardSE.play();
                            }
                        }
                    } else {
                        if (c1.x + 30 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                            if (c2.guard != Chara.GUARD) {
                                MainPanel.dageki.play();
                                c2.life -= KICK_DAMAGE;
                                c2.hit();
                                damage = true;
                            } else {
                                c1.x += GUARD_BACK;
                                c2.guard();
                                MainPanel.guardSE.play();
                            }
                        }
                    }
                }
                break;
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    /**
     * @param c1 波動拳するキャラ
     * @param c2 攻撃を受けるキャラ
     * @return 攻撃がヒットしたかどうか
     */
    static boolean hadou(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL || c2.state == Chara.TATSUMAKI) {
            return false;
        }
        if (c1.hadou.exist && c2.y + MainPanel.IMG_SIZE > c1.hadou.y + 100) {
            if ((c1.hadou.x + 10 < c2.x + MainPanel.IMG_SIZE / 3 && c2.x + MainPanel.IMG_SIZE / 3 < c1.hadou.x + Hadou.LENGTH - 10)
                    || (c1.hadou.x + 10 < c2.x + MainPanel.IMG_SIZE * 2 / 3 && c2.x + MainPanel.IMG_SIZE * 2 / 3 < c1.hadou.x + Hadou.LENGTH - 10)) {
                if (!(c2.guard == Chara.GUARD || c2.guard == Chara.SIT_GUARD)) {
                    damage = true;
                    MainPanel.dageki.play();
                    c2.hit();
                    c2.life -= HADOU_DAMAGE;
                } else {
                    c2.life -= 1;
                    c2.guard();
                    MainPanel.guardSE.play();
                }
                c1.hadou.exist = false;
            }
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    /**
     * @param c1 昇竜拳するキャラ
     * @param c2 攻撃を受けるキャラ
     * @return 攻撃がヒットしたかどうか
     */
    static boolean shouryu(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL) {
            return false;
        }
        if (c1.state == Chara.SHOURYU && c1.superWaza < 9
                && c1.y + 15 - c2.y < MainPanel.IMG_SIZE) {
            if (c1.direction == Chara.RIGHT) {
                if (c1.x + MainPanel.IMG_SIZE * 2 / 3 > c2.x + MainPanel.IMG_SIZE / 3) {
                    if (c2.guard != Chara.GUARD && c2.guard != Chara.SIT_GUARD) {
                        c2.life -= SHOURYU_DAMAGE;
                        c2.onGround = false;
                        MainPanel.dageki.play();
                        c2.hit();
                        c2.futtobi();
                        damage = true;
                    } else {
                        c2.life -= 1;
                        c1.x -= GUARD_BACK;
                        MainPanel.guardSE.play();
                        c2.guard();
                    }
                }
            } else {
                if (c1.x + MainPanel.IMG_SIZE / 3 < c2.x + MainPanel.IMG_SIZE * 2 / 3) {
                    if (c2.guard != Chara.GUARD && c2.guard != Chara.SIT_GUARD) {
                        c2.life -= SHOURYU_DAMAGE;
                        c2.onGround = false;
                        c2.hit();
                        MainPanel.dageki.play();
                        c2.futtobi();
                        damage = true;
                    } else {
                        c2.life -= 1;
                        c1.x += GUARD_BACK;
                        c2.guard();
                        MainPanel.guardSE.play();
                    }
                }
            }
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    static boolean tatsumaki(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL || c2.state == Chara.SIT) {
            return false;
        }
        if (c1.state == Chara.TATSUMAKI && Math.abs(c1.y - c2.y) < MainPanel.IMG_SIZE / 2) {
            if ((c1.x < c2.x + MainPanel.IMG_SIZE / 3 && c2.x + MainPanel.IMG_SIZE / 3 < c1.x + MainPanel.IMG_SIZE)
                    || (c1.x < c2.x + MainPanel.IMG_SIZE * 2 / 3 && c2.x + MainPanel.IMG_SIZE * 2 / 3 < c1.x + MainPanel.IMG_SIZE)) {
                if (c2.guard != Chara.GUARD) {
                    c2.life -= TATSUMAKI_DAMAGE;
                    MainPanel.dageki.play();
                    c2.onGround = false;
                    c2.hit();
                    c2.futtobi();
                    damage = true;
                } else {
                    if (new Random().nextDouble() < 1.0 / 3.0) {
                        c2.life -= 1;
                        MainPanel.guardSE.play();
                    }
                    c2.guard();
                    if (c1.direction == Chara.RIGHT) {
                        c1.x -= GUARD_BACK;
                    } else {
                        c1.x += GUARD_BACK;
                    }
                }
            }
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    static boolean ultra(Chara c1, Chara c2) {
        boolean damage = false;
        if (c2.state == Chara.FALL || c1.ultraCount < 11 * 2) {
            return false;
        }
        if (c1.ultra.exist && Math.abs(c1.ultra.y - c2.y) < MainPanel.IMG_SIZE - 50) {
            if ((c1.ultra.x < c2.x + MainPanel.IMG_SIZE / 3 && c2.x + MainPanel.IMG_SIZE / 3 < c1.ultra.x + Ultra.LENGTH)
                    || (c1.ultra.x < c2.x + MainPanel.IMG_SIZE * 2 / 3 && c2.x + MainPanel.IMG_SIZE * 2 / 3 < c1.ultra.x + Ultra.LENGTH)) {
                if (!(c2.guard == Chara.GUARD || c2.guard == Chara.SIT_GUARD)) {
                    damage = true;
                    c2.hit();
                    c2.vx = 0;
                    c2.vy = 0;
                    if (c1.ultra.hitCount == 0) {
                        c1.ultra.tempSet();
                    }
                    if (c1.ultra.hitCount < ULTRA_HIT_MAX) {
                        c1.ultra.setXY_FromTempXY();
                        c1.ultra.hitCount++;
                        MainPanel.dageki.play();
                        c2.life -= ULTRA_DAMAGE;
                    } else {
                        c2.futtobi();
                        c1.ultra.exist = false;
                        MainPanel.explosion.play();
                        c2.vy = Ultra.SPEED_Y * 2;
                        if (c1.ultra.left) {
                            c2.vx = -Ultra.SPEED_X * 2;
                        } else {
                            c2.vx = Ultra.SPEED_X * 2;
                        }
                    }
                } else {
                    c2.guard();
                    c2.vx = 0;
                    c2.vy = 0;
                    if (c1.ultra.hitCount == 0) {
                        c1.ultra.tempSet();
                    }
                    if (c1.ultra.hitCount < ULTRA_HIT_MAX) {
                        MainPanel.guardSE.play();
                        c1.ultra.setXY_FromTempXY();
                        c1.ultra.hitCount++;
                        c2.life -= 1;
                    } else {
                        MainPanel.explosion.play();
                        c1.ultra.exist = false;
                    }
                }
            }
        }
        if (c2.life < 0) {
            c2.life = 0;
        }
        return damage;
    }

    static boolean attack(Chara c1, Chara c2) {
        boolean damage;
        damage = punch(c1, c2);
        damage |= punch(c2, c1);
        damage |= kick(c1, c2);
        damage |= kick(c2, c1);
        damage |= hadou(c1, c2);
        damage |= hadou(c2, c1);
        damage |= shouryu(c1, c2);
        damage |= shouryu(c2, c1);
        damage |= tatsumaki(c1, c2);
        damage |= tatsumaki(c2, c1);
        damage |= ultra(c1, c2);
        damage |= ultra(c2, c1);
        return damage;
    }
}

package kakutou;

public class Ultra {

    static final int SPEED_X = 25, SPEED_Y = 10, LENGTH = 256;
    int x, y, tempX, tempY;

    boolean left,//is•ûŒü
            exist,//‘¶Ý‚·‚é‚©‚Ç‚¤‚©
            ok;//”­“®‚Å‚«‚é‚©‚Ç‚¤‚©
    int hitCount;

    public Ultra() {
        exist = false;
        ok = true;
        hitCount = 0;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void tempSet() {
        tempX = x;
        tempY = y;
    }

    void setXY_FromTempXY() {
        x = tempX;
        y = tempY;
    }

    void setDirection(Chara c) {
        left = c.direction == Chara.LEFT;
        exist = true;
        ok = false;
    }

    void update(Chara c) {
        if (exist && c.ultraCount > 11 * 2) {
            if (left) {
                x -= SPEED_X;
            } else {
                x += SPEED_X;
            }
            y += SPEED_Y;
        }
        if (x + LENGTH < 0 || x > MainPanel.W) {
            exist = false;
        }
    }

    static void utikeshi(Ultra u1, Ultra u2) {
        if (u1.exist && u2.exist && Math.abs(u1.y - u2.y) < MainPanel.IMG_SIZE - 50) {
            if ((u1.x < u2.x && u2.x < u1.x + Ultra.LENGTH)
                    || (u1.x < u2.x + Ultra.LENGTH && u2.x + Ultra.LENGTH < u1.x + Ultra.LENGTH)) {
                if (u1.hitCount == 0) {
                    u1.tempSet();
                    u2.tempSet();
                }
                if (u1.hitCount < Attack.ULTRA_HIT_MAX) {
                    u1.setXY_FromTempXY();
                    u2.setXY_FromTempXY();
                    u1.hitCount++;
                    MainPanel.dageki.play();
                } else {
                    u1.exist = u2.exist = false;
                    MainPanel.explosion.play();
                }
            }
        }
    }
}

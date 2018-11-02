package kakutou;

public class Hadou {

    static final int SPEED = 15, LENGTH = 240;
    int x, y;
    boolean left,//is•ûŒü
            exist;//‘¶İ‚·‚é‚©‚Ç‚¤‚©

    public Hadou() {
        exist = false;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void setDirection(Chara c) {
        left = c.direction == Chara.LEFT;
        exist = true;
    }

    void update(Chara c) {
        if (exist) {
            if (left) {
                x -= SPEED;
            } else {
                x += SPEED;
            }
        }
        if (x + LENGTH < 0 || x > MainPanel.W) {
            exist = false;
        }
    }
    /**
     * 2‚Â‚Ì”g“®‚Ì‚Ô‚Â‚©‚è‡‚¢‚É‚æ‚éAÁ–Å
     */
    static void utikeshi(Hadou h1, Hadou h2) {
        if (h1.exist && h2.exist) {
            if (Math.abs(h1.x - h2.x) < LENGTH / 2) {
                h1.exist = h2.exist = false;
            }
        }
    }
}

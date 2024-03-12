package music;

import reactions.Gesture;
import reactions.Reaction;

import java.awt.*;

public class Rest extends Duration{
    public Staff staff;
    public Time time;
    public int line = 4;
    public Rest(Staff staff, Time time){
        this.staff = staff;
        this.time = time;
        addReaction(new Reaction("E-E") { // add flag to rest
            public int bid(Gesture gesture) {
                int y = gesture.vs.yM(), x1 = gesture.vs.xL(), x2 = gesture.vs.xH();
                int x = Rest.this.time.x;
                if (x1 > x || x2 < x) {return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }
            public void act(Gesture gesture) {Rest.this.incFlag();}
        });
        addReaction(new Reaction("W-W") { // decrease flag to rest
            public int bid(Gesture gesture) {
                int y = gesture.vs.yM(), x1 = gesture.vs.xL(), x2 = gesture.vs.xH();
                int x = Rest.this.time.x;
                if (x1 > x || x2 < x) {return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }
            public void act(Gesture gesture) {Rest.this.decFlag();}
        });
        addReaction(new Reaction("DOT") { // dot
            public int bid(Gesture gesture) {
                int yR = Rest.this.y();
                int xR = Rest.this.time.x;
                int x = gesture.vs.xM(), y = gesture.vs.yM();
                if (x < xR || x > xR + 40 || y < yR - 40 || y > yR + 40) {return UC.noBid;}
                return Math.abs(x - xR) + Math.abs(y - yR);
            }
            public void act(Gesture gesture) {Rest.this.cycleDot();}
        });
    }
    public int y(){return staff.yLine(line);}
    public void show(Graphics g){
        int H = staff.H(), y = y();
        if (nFlag ==  -2){Glyph.REST_W.showAt(g, H, time.x, y);}
        if (nFlag ==  -1){Glyph.REST_H.showAt(g, H, time.x, y);}
        if (nFlag ==  0){Glyph.REST_Q.showAt(g, H, time.x, y);}
        if (nFlag ==  1){Glyph.REST_1F.showAt(g, H, time.x, y);}
        if (nFlag ==  2){Glyph.REST_2F.showAt(g, H, time.x, y);}
        if (nFlag ==  3){Glyph.REST_3F.showAt(g, H, time.x, y);}
        if (nFlag ==  4){Glyph.REST_4F.showAt(g, H, time.x, y);}
        int xO = UC.gapRestToFirstDot, sp = UC.gapBetweenAugDot;
        for (int i = 0; i < nDot; i++){g.fillOval(time.x + xO + i*sp, y - 3*H/2, H*2/3, H*2/3);}
    }
}

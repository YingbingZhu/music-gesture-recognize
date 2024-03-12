package music;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{
    public Staff staff;
    public int line;
    public Time time;
    public Glyph forcedGlyph = null; // special forced one
    public Stem stem = null; // can be null;
    public boolean wrongSide = false;
    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        this.time.heads.add(this);
        line = staff.LineOfY(y);
        System.out.println("Line :" + line);
        addReaction(new Reaction("S-S") {
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                int W = Head.this.W(), hY = Head.this.y();
                if (y1 > y || y2 < y){return UC.noBid;}
                int hL = Head.this.time.x, hR = hL + W; // hL left
                if (x < hL - W||x > hR + W){return UC.noBid;}
                if (x < hL + W/2){return hL - x;}
                if (x > hR - W/2){return x - hR;}
                return UC.noBid;
            }
            public void act(Gesture gesture) {
                int x = gesture.vs.xM(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                int w = Head.this.W();
                Boolean up = (x > t.x + w/2);
                // have stem, do a stem gesture, if no stem, add stem on it
                if (Head.this.stem == null){t.stemHeads(staff, up, y1, y2);}
                else {t.unStemHeads(y1, y2);}
            }
        });
        addReaction(new Reaction("DOT") {
            public int bid(Gesture g) {
                int xH = Head.this.x(), yH = Head.this.y(), h = Head.this.staff.H(), w = Head.this.W();
                int x = g.vs.xM(), y = g.vs.yM();
                if (x < xH || x > xH + 2*w || y < yH - h || y > yH + h) {return UC.noBid;}
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }
            public void act(Gesture g) {
                if (Head.this.stem != null) {Head.this.stem.cycleDot();}
            }
        });
    }
    public void show(Graphics g){
        int H = staff.H();
        (forcedGlyph != null?forcedGlyph: normalGlyph()).showAt(g, H, x(), staff.yLine(line));
        if (stem != null) {
            int off = UC.gapRestToFirstDot, sp = UC.gapBetweenAugDot;
            for (int i = 0; i < stem.nDot; i++){
                g.fillOval(time.x + off + i*sp, y() - 3*H/2, H*2/3, H*2/3);
            }
        }
    }
    public int x(){
        int res = time.x;
        if (wrongSide){res += (stem != null && stem.isUp) ? W() : -W();}
        return res;
    }
    public Glyph normalGlyph(){
        if (stem == null){return Glyph.HEAD_Q;}
        if (stem.nFlag == -1){return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2){return Glyph.HEAD_W;}
        return Glyph.HEAD_Q;
    }
    public void deleteMass(){
        //stub
        time.heads.remove(this);

    }
    public int y(){return staff.yLine(line);}
    public int W(){return 24*staff.H()/10;}

    public void unStem(){ // head got off the stem
        if(stem!= null){
            stem.heads.remove(this);
            if (stem.heads.size() == 0){stem.deleteStem();}
            stem = null; // the head no longer refers to the stem
            wrongSide = false;
        }
    }
    public void joinStem(Stem s) {
        if (stem != null){unStem();}
        s.heads.add(this); // now s refers to head
        stem = s; // head refers to s
    }

    @Override
    public int compareTo(Head h) {
        // return line - h.line; // > 0: first one is bigger
        return (staff.iStaff != h.staff.iStaff)? staff.iStaff - h.staff.iStaff:line - h.line;
    }

    // -------------------- List --------------------
    public static class List extends ArrayList<Head>{

    }

}

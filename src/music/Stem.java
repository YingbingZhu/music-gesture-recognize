package music;

import reactions.Gesture;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Stem extends Duration implements Comparable<Stem>{
    public Staff staff;
    public Beam beam = null;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Stem(Staff staff, Head.List heads, boolean up){
        super();
        this.staff = staff; isUp = up;
        for(Head h:heads){h.unStem(); h.stem = this;}
        this.heads = heads;
        staff.sys.stems.addStem(this);
        setWrongSide();
        addReaction(new Reaction("E-E") { // add flag
            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if (x1 > xs || x2 < xs){return UC.noBid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if (y < y1 || y > y2){return UC.noBid;}
                return Math.abs(y - (y1 + y2)/2) + 55;
            }
            public void act(Gesture g) {Stem.this.incFlag();}
        });
        addReaction(new Reaction("W-W") { // dec flag
            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if (x1 > xs || x2 < xs){return UC.noBid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if (y < y1 || y > y2){return UC.noBid;}
                return Math.abs(y - (y1 + y2)/2);
            }
            public void act(Gesture g) {Stem.this.decFlag();}
        });
    }
    // factory method, get stem if there are heads that match the time
    public static Stem getStem(Staff staff, Time time, int y1, int y2, boolean up){
        Head.List heads = new Head.List();
        for (Head h: time.heads){
            int yH = h.y();
            if (yH > y1 && yH < y2){heads.add(h);} // sampling
        }
        if (heads.size() == 0){return null;} // no stem create if no heads
        Beam beam = internalStem(staff.sys, time.x, y1, y2);
        Stem res = new Stem(staff, heads, up);
        if (beam != null){beam.addStem(res);}
        return res;
    }
    public static Beam internalStem(Sys sys, int x, int y1, int y2){
        for(Stem s: sys.stems){
            if(s.beam != null && s.x() < x && s.yLow() < y2 && s.yHigh() > y1){ // cross by
                int bX = s.beam.first().x(), bY = s.beam.first().yBeamEnd();
                int eX = s.beam.last().x(), eY = s.beam.last().yBeamEnd();
                if (Beam.verticalLineCrossedSegment(x, y1, y2, bX, bY, eX, eY)){return s.beam;}
            }
        }
        return null;
    }
    public void show(Graphics g){
        if(nFlag >= -1 && heads.size() > 0){
            int x = x(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
            if (beam == null) {
                if (nFlag == 1){(isUp?Glyph.FLAG1D:Glyph.FLAG1U).showAt(g,h,x,yB);}
                if (nFlag == 2){(isUp?Glyph.FLAG2D:Glyph.FLAG2U).showAt(g,h,x,yB);}
                if (nFlag == 3){(isUp?Glyph.FLAG3D:Glyph.FLAG3U).showAt(g,h,x,yB);}
                if (nFlag == 4){(isUp?Glyph.FLAG4D:Glyph.FLAG4U).showAt(g,h,x,yB);}
            }
        }
    }
    public Head firstHead(){return heads.get(isUp?heads.size()-1:0);}
    public Head lastHead(){return heads.get(isUp?0:heads.size()-1);}
    public int yFirstHead(){Head h = firstHead(); return h.staff.yLine(h.line);}
    public int x(){Head h = firstHead(); return h.time.x + (isUp?h.W():0);}
    public int yBeamEnd(){
        Head h = lastHead();
        int line = h.line;
        line += (isUp?-7:7); // default stem one octave from last head
        int flagInc = nFlag>2?2*(nFlag-2):0; // more than 2 flags adjust stem inc
        line += (isUp?-flagInc:flagInc);
        if((isUp && line > 4) || (!isUp && line < 4)){line = 4;} // meet center if possible
        return h.staff.yLine(line);
    }
    public int yLow(){return isUp?yBeamEnd():yFirstHead();}
    public int yHigh(){return isUp?yFirstHead():yBeamEnd();}
    public void deleteStem() {staff.sys.stems.remove(this); deleteMass();} // delete from Mass class
    public void setWrongSide(){
        // called by time.stemHeads
        Collections.sort(heads);
        int i, last, next;
        if (isUp){i = heads.size()-1; last = 0; next = -1;}
        else {i = 0; last = heads.size()-1; next = 1;}
        Head pH = heads.get(i);
        pH.wrongSide = false; // first head is always right
        while (i != last){ // compare next head and previous head, if they are close, wrong side
            i+=next;
            Head nH = heads.get(i);
            nH.wrongSide = (Math.abs(nH.line - pH.line) <= 1 && !pH.wrongSide && pH.staff==nH.staff); // check if on same staff
            pH = nH;
        }
    }

    @Override
    public int compareTo(Stem s) {
        return x() - s.x();
    }

    // ----------- List --------------
    public static class List extends ArrayList<Stem>{
        public int yMin = 1_000_000, yMax = -1_000_000;
        public void addStem(Stem stem) {
            add(stem);
            if (stem.yLow() < yMin) {yMin = stem.yLow();}
            if (stem.yHigh() > yMax) {yMax = stem.yHigh();}
        }
        public void sort(){
            Collections.sort(this);
        }
        public ArrayList<Stem> allIntersectors(int x1, int y1, int x2, int y2){
            ArrayList<Stem> res = new ArrayList<>();
            for(Stem s:this){
                int x = s.x(), y = Beam.yOfX(x, x1, x2, y1, y2);
                int sLow = s.yLow(), sHigh = s.yHigh();
                if (x > x1 && x < x2 && y > s.yLow() && y < s.yHigh()){res.add(s);}

            }
            return res;
        }
        public boolean fastReject(int y1, int y2){return y2 < yMin || y1 > yMax;}
    }
}

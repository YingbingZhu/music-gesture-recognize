package music;

import java.awt.*;
import java.util.Collections;

public class Stem extends Duration{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Stem(Staff staff, boolean up){super(); this.staff = staff; isUp = up;}
    public void show(Graphics g){
        if(nFlag >= -1 && heads.size() > 0){
            int x = x(), h = staff.H(), yH = yFirstHead(), yB = yBeanEnd();
            g.drawLine(x, yH, x, yB);
        }
    }
    public Head firstHead(){return heads.get(isUp?heads.size()-1:0);}
    public Head lastHead(){return heads.get(isUp?0:heads.size()-1);}
    public int yFirstHead(){Head h = firstHead(); return h.staff.yLine(h.line);}
    public int x(){Head h = firstHead(); return h.time.x + (isUp?h.W():0);}
    public int yBeanEnd(){
        Head h = lastHead();
        int line = h.line;
        line += (isUp?-7:7); // default stem one octave from last head
        int flagInc = nFlag>2?2*(nFlag-2):0; // more than 2 flags adjust stem inc
        line += (isUp?-flagInc:flagInc);
        if((isUp && line > 4) || (!isUp && line < 4)){line = 4;} // meet center if possible
        return h.staff.yLine(line);
    }
    public void deleteStem() {deleteMass();} // delete from Mass class
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
            nH.wrongSide = (Math.abs(nH.line - pH.line) <= 1 && !pH.wrongSide);
            pH = nH;
        }
    }
}

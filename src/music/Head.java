package music;

import reactions.Mass;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass {
    public Staff staff;
    public int line;
    public Time time;
    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        line = staff.LineOfY(y);
        System.out.println("Line :" + line);
    }
    public void show(Graphics g){
        int H = staff.H();
        Glyph.HEAD_Q.showAt(g, H, time.x, staff.yTop() + line*H);
    }
    public int W(){return 24*staff.H()/10;}
    // -------------------- List --------------------
    public static class List extends ArrayList<Head>{

    }

}

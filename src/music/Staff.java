package music;

import java.awt.Graphics;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import static music.Editor.PAGE; //use this single static element

public class Staff extends Mass {
  public Sys sys;
  public int iStaff;
  public Staff.Fmt fmt;
  public Staff(Sys sys, int iStaff, Staff.Fmt staffFmt){
    super("BACK");
    this.sys = sys;
    this.fmt = staffFmt;
    this.iStaff = iStaff;

    addReaction(new Reaction("SW-SW") { //  create a node head
      public int bid(Gesture g) {
        int x = g.vs.xM(), y = g.vs.yM();
        if (x < PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
        int H = Staff.this.H(), top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
        if (y < top || y > bot) {return UC.noBid;}
        return 10;
      }
      public void act(Gesture g) {
        new Head(Staff.this, g.vs.xM(), g.vs.yM());
      }
    });
    addReaction(new Reaction("W-S") {
      public int bid(Gesture g) {
        int x = g.vs.xL(), y = g.vs.yM();
        if (x <PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
        int H = Staff.this.H(), top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
        if (y < top || y > bot) {return UC.noBid;}
        return 10;
      }

      public void act(Gesture g) {
        Time time = Staff.this.sys.getTime(g.vs.xL());
        new Rest(Staff.this, time);
      }
    });
    addReaction(new Reaction("E-S") {
      public int bid(Gesture g) {
        int x = g.vs.xL(), y = g.vs.yM();
        if (x <PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
        int H = Staff.this.H(), top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
        if (y < top || y > bot) {return UC.noBid;}
        return 10;
      }

      public void act(Gesture g) {
        Time time = Staff.this.sys.getTime(g.vs.xL());
        (new Rest(Staff.this, time)).incFlag();
      }
    });
  }
  public int yTop(){return sys.staffTop(iStaff);}
  public int yBot(){return yTop() + fmt.height();}
  public int yLine(int line){return yTop() + line*H();}
  public int LineOfY(int y){
    int H = H();
    int bias = 100;
    int top = yTop() - H*bias;
    return (y - top + H/2)/H - bias;
  }
  public int H(){return fmt.H;}
  //-----------------STAFF FMT-------------------------
  public static class Fmt{
    int H = 8; // half the space between staff lines - Useful because notes land both on lines and on spaces
    int nLines = 5; // most music is 5 lines, but there are exceptions
    int height(){return 2*H*(nLines - 1);}
    public void showAt(Graphics g, int y){
      for(int i=0; i<nLines; i++){g.drawLine(PAGE.margins.left, y+2*H*i, PAGE.margins.right, y+2*H*i);}
    }
  }


}

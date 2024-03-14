package music;

import java.awt.Graphics;
import java.util.ArrayList;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import static music.Editor.PAGE;

public class Sys extends Mass {
  public Stem.List stems = new Stem.List();
  public Time.List times;
  public ArrayList<Staff> staffs = new ArrayList<>();
  public Page page = PAGE;
  public int iSys; //which system
  public Fmt fmt; //which particular format

  public Sys(int iSys, Sys.Fmt sysFmt){
    super("BACK");
    this.fmt = sysFmt;
    this.iSys = iSys;
    for(int i = 0; i<sysFmt.size(); i++){staffs.add(new Staff(this, i, fmt.get(i)));}
    times = new Time.List(this);
    addReaction(new Reaction("E-E") { // beam stems
      @Override
      public int bid(Gesture g) {
        int x1 = g.vs.xL(), x2 = g.vs.xH(), y1 = g.vs.yL(), y2 = g.vs.yH();
        if(stems.fastReject(y1, y2)){return UC.noBid;}
        ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
        if (temp.size() < 2){return UC.noBid;}
        System.out.println("Crossed: " + temp.size() + " stems");
        Beam b = temp.get(0).beam; // if already have beam
        for (Stem s:temp){if(s.beam != b){return UC.noBid;}}
        System.out.println("All stems share same beam");
        if (b == null && temp.size() != 2) {return UC.noBid;}
        if (b != null && ((temp.get(0).nFlag != 0)||(temp.get(1).nFlag != 0))){return UC.noBid;}
        return 50;
      }
      @Override
      public void act(Gesture g) {
        int x1 = g.vs.xL(), x2 = g.vs.xH(), y1 = g.vs.yL(), y2 = g.vs.yH();
        ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
        Beam b = temp.get(0).beam; // if already have beam
        if (b == null){
          new Beam(temp.get(0), temp.get(1));
        } else {
          for(Stem s:temp){s.incFlag();}
        }
      }
    });
  }
  public int yTop(){return page.sysTop(iSys);}
  public int staffTop(int iStaff){return yTop() + fmt.staffOffset.get(iStaff);}
  public void show(Graphics g){
    int y = yTop(), x = PAGE.margins.left;
    g.drawLine(x, y, x, y + fmt.height());
  }
  public Time getTime(int x) {return times.getTime(x);}

  //---------------------------------------Sys.Fmt-------------------------------------
  public static class Fmt extends ArrayList<Staff.Fmt>{
    public ArrayList<Integer> staffOffset = new ArrayList<>();
    int height(){int last = size() - 1; return staffOffset.get(last) + get(last).height(); }
    public void showAt(Graphics g, int y){
      for(int i = 0; i < size(); i++){ get(i).showAt(g, y + staffOffset.get(i)); }
    }
  }


}

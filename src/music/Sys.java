package music;

import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Mass;
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

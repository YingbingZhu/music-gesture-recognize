package music;

import java.awt.Graphics;
import java.util.ArrayList;
import music.Staff.Fmt;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;
import static music.Editor.PAGE;

public class Page extends Mass{
  public Margins margins = new Margins();
  public Sys.Fmt sysFmt;
  public int sysGap, nSys; //nSys: count of the systems
  public ArrayList<Sys> sysList = new ArrayList<>();

  public Page(Sys.Fmt sysFmt){
    super("BACK");
    this.sysFmt = sysFmt;

    addReaction(new Reaction("E-W") { //add new staff to staff format
      public int bid(Gesture gesture) {
        int y = gesture.vs.yM();
        if(y <= (PAGE.margins.top + sysFmt.height() + 30 )){ return UC.noBid; } //ensure below the existing system;
        return 0;
      }
      public void act(Gesture gesture) {
        int y = gesture.vs.yM();
        PAGE.addNewStaff(y - PAGE.margins.top);
      }
    });

    addReaction(new Reaction("E-E") {//add new system
      public int bid(Gesture gesture) {
        int y = gesture.vs.yM();
        int yBot = PAGE.sysTop(sysList.size());
        if ( y <= yBot){ return UC.noBid; }
        return 50;
      }
      public void act(Gesture gesture) {
        int y = gesture.vs.yM();
        if( PAGE.sysList.size() == 1){
          PAGE.sysGap = y - PAGE.sysTop(1);
        }
        PAGE.addNewSys();
      }
    });

  }
  public void addNewSys(){
    sysList.add(new Sys(sysList.size(), sysFmt));
  }

  public void addNewStaff(int yOff){
    Fmt sF = new Fmt();
    int n = sysFmt.size();
    sysFmt.add(new Fmt());
    sysFmt.staffOffset.add(yOff);
    for (int i = 0; i < sysList.size(); i++) {
      Sys sys = sysList.get(i);
      sys.staffs.add(new Staff(sys, n, sF));
    }
  }

  public int sysTop(int iSys){ return margins.top + iSys * (sysFmt.height() + sysGap); }
  public void show(Graphics g){
    for (int i = 0; i < sysList.size(); i++) {
      sysFmt.showAt(g, sysTop(i));
    }
  }

  //----------------------------------Margins--------------------------
  public static class Margins{
    private static int MM = 50;
    public int top = MM;
    public int left = MM;
    public int bot = UC.mainWindowHeight - MM; //bottom
    public int right = UC.mainWindowWidth - MM;
  }

}

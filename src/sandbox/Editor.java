package sandbox;

import graphics.G;
import graphics.Window;
import music.UC;
import reactions.*;

import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;

public class Editor extends Window {
    public Layer BACK = new Layer("BACK"), FORE = new Layer("FORE");
    public static Page PAGE;
    public Editor(){
        super("Editor", UC.mainWindowWidth, UC.mainWindowHeight);
        Reaction.initialReactions.addReaction(new Reaction("E-W"){
            public int bid(Gesture g){ return 10; }
            public void act(Gesture g){
                int y = g.vs.yM();
                Sys.Fmt sysFmt = new Sys.Fmt();
                PAGE = new Page(sysFmt);
                PAGE.margins.top = y;
                PAGE.addNewSys();
                PAGE.addNewStaff(0);
                this.disable();
            }
        });
    }

    public void paintComponent(Graphics g){
        G.fillBack(g,Color.WHITE);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }
    public void mousePressed(MouseEvent me){
        Gesture.AREA.dn(me.getX(), me.getY()); repaint();}
    public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(), me.getY()); repaint();}
    public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(), me.getY()); repaint();}
    //--------------------PAGE-----------------------------
    public static class Page extends Mass{
        public Margins margins = new Margins(); public Sys.Fmt sysFmt;
        public int sysGap, nSys; // size of spacing between Sys on page, number of Sys that fit on page
        public ArrayList<Sys> sysList = new ArrayList<>();
        public Page(Sys.Fmt sf){
            super("BACK"); sysFmt = sf;
            addReaction(new Reaction("E-W"){// addNewStaff
                public int bid(Gesture g){
                    int y = g.vs.yM(); if(y <= PAGE.margins.top + sysFmt.height() + 30){return UC.noBid;}
                    return 50;
                }
                public void act(Gesture g){
                    int y = g.vs.yM();
                    PAGE.addNewStaff(y - PAGE.margins.top);
                }
            });

            addReaction(new Reaction("E-E"){// addNewSys
                public int bid(Gesture g){
                    int y = g.vs.yM(); int yBot = PAGE.sysTop(nSys);
                    if(y <= yBot){return UC.noBid;}
                    return 50;
                }
                public void act(Gesture g){
                    int y = g.vs.yM();
                    if(PAGE.nSys == 1){PAGE.sysGap = y - PAGE.sysTop(1);}
                    PAGE.addNewSys();
                }
            });
        } // page lives in the background
        int sysTop(int iSys){return margins.top + iSys*(sysFmt.height() + sysGap);}
        public void show(Graphics g){
            for(int i = 0; i<nSys; i++){sysFmt.showAt(g, sysTop(i));}
        }
        public void addNewSys(){sysList.add(new Sys(nSys++, sysFmt));}
        public void addNewStaff(int yOff){
            Staff.Fmt sf = new Staff.Fmt(); int n = sysFmt.size();
            sysFmt.add(new Staff.Fmt()); sysFmt.staffOffset.add(yOff);
            for(int i = 0; i<nSys; i++){sysList.get(i).staffs.add(new Staff(n, sf));}
        }
        //-----------------MARGINS--------------------------
        public static class Margins{
            private static int MM = 50;
            public int top = MM, left = MM, bot = UC.mainWindowWidth - MM ,right = UC.mainWindowHeight - MM;
        }
    }
    //------------------SYS-----------------------
    public static class Sys extends Mass{
        public ArrayList<Staff> staffs = new ArrayList<>();
        public Page page = PAGE; public int iSys; public Sys.Fmt fmt;
        public Sys(int iSys, Sys.Fmt sysFmt){
            super("BACK"); fmt = sysFmt; this.iSys = iSys;
            for(int i = 0; i<sysFmt.size(); i++){staffs.add(new Staff(i, sysFmt.get(i)));}
        }
        public int yTop(){return page.sysTop(iSys);}
        public void show(Graphics g){
            int y = yTop(), X = PAGE.margins.left;
            g.drawLine(X, y, X, y+fmt.height());
        }
        //--------------------SYS FMT---------------------
        public static class Fmt extends ArrayList<Staff.Fmt>{
            public ArrayList<Integer> staffOffset = new ArrayList<>();
            int height(){int last = size() - 1; return staffOffset.get(last) + get(last).height(); }
            public void showAt(Graphics g, int y){
                for(int i = 0; i < size(); i++){ get(i).showAt(g, y + staffOffset.get(i)); }
            }
        }
    }
    //-------------------STAFF-------------------------
    public static class Staff extends Mass{
        public Sys sys; public int iStaff; public Staff.Fmt fmt;
        public Staff(int iStaff, Staff.Fmt staffFmt){
            super("BACK"); fmt = staffFmt; this.iStaff = iStaff;
        }
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

    public static void main(String[] args) {
        PANEL = new Editor();
        Window.launch(); //calling a function
    }




}

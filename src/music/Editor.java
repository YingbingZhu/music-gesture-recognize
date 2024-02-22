package music;

import graphics.G;
import graphics.Window;
import reactions.*;

import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;

public class Editor extends Window {
    static {new Layer("BACK"); new Layer("NOTE"); new Layer("FORE");}
    public static Page PAGE;
    public Editor(){
        super("Music Editor", UC.mainWindowWidth, UC.mainWindowHeight);
        Reaction.initialReactions.addReaction(new Reaction("E-E"){
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
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
        if (PAGE != null) {
            Glyph.CLEF_G.showAt(g, 8, 100, PAGE.margins.top + 4*8);
//            int H = 32;
//            Glyph.HEAD_Q.showAt(g, H, 200, PAGE.margins.top + 4*H);
//            g.setColor(Color.RED);
//            g.drawRect(200, PAGE.margins.top + 3*H, 24*H/10, 2*H);
        }
    }
    public void mousePressed(MouseEvent me){
        Gesture.AREA.dn(me.getX(), me.getY()); repaint();}
    public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(), me.getY()); repaint();}
    public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(), me.getY()); repaint();}


    public static void main(String[] args) {
        PANEL = new Editor();
        Window.launch(); //calling a function
    }



}

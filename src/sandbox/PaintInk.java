package sandbox;

//import graphics.G;
import graphics.G;
import graphics.Window;
import music.UC;
import reactions.Ink;
import reactions.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static String recognized = ""; // change when draw something
    public static Ink.List inkList = new Ink.List();

    public static Shape.Prototype.List pList = new Shape.Prototype.List();
//    static{inkList.add(new Ink());};  stub:to-do
    public PaintInk() {
        super("paintInk", UC.mainWindowWidth, UC.mainWindowHeight);
    }
    public void paintComponent(Graphics g) {
        G.fillBack(g, Color.white);
        g.setColor(Color.BLACK);
        inkList.show(g);
        g.setColor(Color.red);
        Ink.BUFFER.show(g);
//        g.drawString("points: " + Ink.BUFFER.n, 600, 30);
        if (inkList.size() > 1) {
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last-1).norm);
            g.setColor(dist > UC.noMatchDistance ? Color.red:Color.BLACK);
            g.drawString("Dist: " + dist, 600, 60);
        }
        pList.show(g);
        g.drawString(recognized, 700, 40);
    }
    public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(), me.getY());repaint();}
    public void mouseReleased(MouseEvent me){
        Ink ink = new Ink();
        Shape s = Shape.recognize(ink);
        recognized = "Recognized: " + (s != null ? s.name : "UNRECOGNIZED!");
        Shape.Prototype proto;
        inkList.add(ink);
        if (pList.bestDist(ink.norm) < UC.noMatchDistance){
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norm);
        } else {
            proto = new Shape.Prototype();
            pList.add(proto);
        }
        ink.norm = proto;
        repaint();
    }
    public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(), me.getY());repaint();}
    public static void main(String[] args) {
        PANEL = new PaintInk();
        Window.launch();
    }
}

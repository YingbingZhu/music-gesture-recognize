package sandbox;

//import graphics.G;
import graphics.G;
import graphics.Window;
import music.UC;
import reactions.Ink;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();
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
        g.drawString("points: " + Ink.BUFFER.n, 600, 30);

    }
    public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(), me.getY());repaint();}
    public void mouseReleased(MouseEvent me){inkList.add(new Ink()); repaint();}
    public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(), me.getY());repaint();}
    public static void main(String[] args) {
        PANEL = new PaintInk();
        Window.launch();
    }
}

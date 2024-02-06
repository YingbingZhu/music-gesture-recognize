package reactions;

import graphics.G;
import graphics.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainer extends Window {
    public static String UNKNOWN = "<- this name currently unknown";
    public static String ILLEGAL = "<- this name is not legal";
    public static String KNOWN = "<- this is a known shape";
    public static String curName= "";
    public static String curState = ILLEGAL;
    public static Shape.Prototype.List pList = new Shape.Prototype.List();


    public ShapeTrainer() {super("ShapeTrainer", 1000, 700);}

    public void setState(){
        // stub
        curState = (curName.equals("")||curName.equals("DOT")) ? ILLEGAL:UNKNOWN;
        if (curState == UNKNOWN) {
            if (Shape.DB.containsKey(curName)) {
                curState = KNOWN;
                pList = Shape.DB.get(curName).prototypes;
            } else {pList = null;}
        }

    }
    public void paintComponent(Graphics g){
        G.fillBack(g, Color.white);
        g.setColor(Color.BLACK);
        g.drawString(curName, 600, 30);
        g.drawString(curState, 700, 30);
        g.setColor(Color.red);
        Ink.BUFFER.show(g);
        if(pList != null){pList.show(g);}
    }
    public void keyTyped(KeyEvent ke){
        char c = ke.getKeyChar(); // get typed character
        System.out.println("types: " + c);
        curName = (c==' '||c==0x0D||c==0x0A) ?"":curName+c;  // 0x0D hexadecimal
        setState();
        repaint();
    }

    public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(), me.getY());repaint();}
    public void mouseReleased(MouseEvent me){
        if (curState != ILLEGAL){
            Ink ink = new Ink();
            Shape.Prototype proto;
            if(pList == null){
                Shape s = new Shape(curName);
                Shape.DB.put(curName, s);
                pList = s.prototypes;
            }

            if (pList.bestDist(ink.norm) < UC.noMatchDistance){
                proto = Shape.Prototype.List.bestMatch;
                proto.blend(ink.norm);
            } else {
                proto = new Shape.Prototype();
                pList.add(proto);
            }
            setState(); // state is updated
            repaint();
        }
    }
    public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(), me.getY());repaint();}
    public static void main(String[] args) {PANEL = new ShapeTrainer(); Window.launch();}
}

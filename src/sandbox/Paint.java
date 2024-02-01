package sandbox;

import graphics.G;
import graphics.Window;
import sun.plugin.javascript.navig.Array;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window {
    public static int clicks = 0; // variable first
    public static Path path = new Path();
    public static Pic pic = new Pic();
    public Paint() {
        super("paint", 1000, 600);
    } // constructor second

    public void mousePressed(MouseEvent me){
        clicks ++;
        path = new Path();
        path.add(me.getPoint());
        pic.add(path);
        repaint();
    }
    public void mouseDragged(MouseEvent me){path.add(me.getPoint()); repaint();}

    public static void main(String[] args){
        PANEL = new Paint();
        Window.launch(); //calling a function
    }

    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.white); //class Color has color red
        g.fillRect(0, 0, 5000, 5000);//go right and down
        g.setColor(G.rndColor());
        g.fillOval(500, 200, 200, 200);
        g.drawLine(100,100,500,500);
        int x= 400, y = 200;
        g.drawString("dude",400,200);
        g.fillOval(400,200,2,2);
        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent();
        int d = fm.getDescent();
        int w = fm.stringWidth("dude");
        g.drawRect(x, y - a, w, a + d);
        g.setColor(Color.black);
        g.drawString("Clicks" + clicks, 600, 50);
        path.draw(g);
        pic.draw(g);

    }
    public static class Path extends ArrayList<Point> {
        public void draw(Graphics g){
            for (int i = 1; i < size(); i++){ // size() == self.size(py)
                Point p = get(i-1), n = get(i);
                g.drawLine(p.x, p.y, n.x, n.y);
                g.drawOval(n.x-1, n.y-1, 3, 3);

            }
        }
    }

    public static class Pic extends ArrayList<Path> {
        public void draw(Graphics g) {
            for (Path p:this){ // list of paths
                p.draw(g);
            }
        }
    }
}

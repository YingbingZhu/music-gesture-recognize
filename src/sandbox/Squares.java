package sandbox;

import graphics.G;
import graphics.Window;
import music.I;
import music.UC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window implements ActionListener {
    public static Timer timer;
    // current area
    public static I.Area curArea;
    public static G.VS vs = new G.VS(100, 100, 200, 300);
    public static Square theSquare = null;
    public Color color = G.rndColor();
    public static Square.List list = new Square.List();
    public static Square BACKGROUND = new Square(0, 0){
        public void dn(int x, int y){theSquare = new Square(x, y);list.add(theSquare);} // create square
        public void drag(int x, int y){theSquare.resize(x, y);}
    };  //anonymous class

    static {BACKGROUND.c = Color.white; BACKGROUND.size.set(5000, 5000); list.add(BACKGROUND);} // fix the bkg

    public Squares(){
        super("squares", UC.mainWindowWidth, UC.mainWindowHeight);
        timer = new Timer(30, this);
        timer.setInitialDelay(5000);
        timer.start();
    }

    // whether dragging or resizing
    private static boolean dragging = false;
    // overwritten by mousePressed, remember where we press
    private static G.V mouseDelta = new G.V(0, 0);

    @Override
    public void mousePressed(MouseEvent me){
        int x = me.getX(), y = me.getY();
        curArea = list.hitSquare(x, y); // either bkg or square
        curArea.dn(x, y); // bkg does one thing, the other behaves differently b/c they implement
/*
        theSquare = list.hitSquare(x, y);
        // if press on existing, it will drag
        if (theSquare != null){
            theSquare.c = G.rndColor();
            dragging = true;
            mouseDelta.set(theSquare.loc.x - x, theSquare.loc.y - y);
        } else {
            list.add(theSquare = new Square(x, y)); // capture the assignment
            dragging = false;
        }
*/
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me){
/*        int x = me.getX(), y = me.getY();
        if (dragging) {theSquare.move(mouseDelta.x + x, mouseDelta.y + y);} else {theSquare.resize(x, y);}*/
        int x = me.getX(), y = me.getY();
        curArea.drag(x, y);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        // G.fillBack(g);
        // vs.fill(g, color);
        list.draw(g);

    }

    public static void main(String[] args) {PANEL = new Squares(); Window.launch();}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();

    }


    //-------------------------------- Square ---------------------------------
    /*
        a nested class square to associate color with squares
     */
    public static class Square extends G.VS implements I.Area{
        public Color c = G.rndColor();

        // delta velocity
        public G.V dv = new G.V(G.rnd(20) - 10, G.rnd(20) - 10);
        public Square(int x, int y){super(x, y, 100, 100); dv.set(0, 0);}

        public void draw(Graphics g){fill(g, c); moveAndBounce();}

        public void resize(int x, int y){if(x > loc.x && y > loc.y){size.set(x-loc.x, y-loc.y);}}

        public void move(int x, int y){loc.set(x, y);}
        public void moveAndBounce(){loc.add(dv);
            if(loc.x < 0 && dv.x < 0){dv.x = -dv.x;}
            if(loc.y < 0 && dv.y < 0){dv.y = -dv.y;}
            if(loc.x > 1000 && dv.x > 0){dv.x = -dv.x;} // greater than screen size
            if(loc.y > 700 && dv.y > 0){dv.y = -dv.y;}
        }
        /*
         press down on the square
         */
        @Override
        public void dn(int x, int y) {
            // theSquare = this;
            mouseDelta.set(this.loc.x - x, this.loc.y - y);
        }

        @Override
        public void up(int x, int y) {}

        @Override
        public void drag(int x, int y) {
            theSquare.move(mouseDelta.x + x, mouseDelta.y + y);

        }


        // --------------------------- List ---------------------------------
        public static class List extends ArrayList<Square>{
            public void draw(Graphics g){for (Square s:this){s.draw(g);}}
            public Square hitSquare(int x, int y) {
                Square res = null;
                for (Square s : this) {
                    if (s.hit(x, y)) {res = s;}
                }
                return res;
            }
        }
    }
}

package music;

import graphics.G;
import reactions.Mass;

import java.awt.*;

public class Spline extends Mass { // BSplines implementation, CubicSplines have package functions
    int xa, ya, xb, yb, xc, yc;

    public Spline() {
        super("NOTE");
        xa = G.rnd(1000); ya = G.rnd(700);
        xb = G.rnd(1000); yb = G.rnd(700);
        xc = G.rnd(1000); yc = G.rnd(700);
    }
    public void show(Graphics g){drawSplines(xa, ya, xb, yb, xc, yc, 5, g);}
    public void drawSplines(int xa, int ya, int xb, int yb, int xc, int yc, int n, Graphics g){
        if (n == 0){g.drawLine(xa, ya, xc, yc); return;}
        int abx = (xa + xb) / 2, aby = (ya+yb)/2;
        int bcx = (xc + xb) / 2, bcy = (yc+yb)/2;
        int abcx = (abx + bcx) / 2, abcy = (aby+bcy)/2;
        drawSplines(xa, ya, abx, aby, abcx, abcy, n-1, g);
        drawSplines(abcx, abcy, bcx, bcy, xc, yc, n-1, g);
    }

    public static void main(String[] args) {

    }
}

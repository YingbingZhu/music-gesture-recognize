package reactions;


import graphics.G;
import music.I;
import music.UC;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable {
    public static HashMap<String, Shape> DB = loadShapeDB();  // database
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values(); // backed by DB, any change to DB show up in list
    public String name;
    public Prototype.List prototypes = new Prototype.List();

    public Shape(String name){this.name = name;}

    // --------------------Prototype ----------------------
    public static class Prototype extends Ink.Norm implements Serializable{
        public int nBlend; // different from Ink.Norm
        public void blend(Ink.Norm norm){blend(norm, nBlend++);}

        public static class List extends ArrayList<Prototype> implements Serializable{
            public static Prototype bestMatch; // set by best distance

            public int bestDist(Ink.Norm norm){
                bestMatch = null;
                int bestSoFar = UC.noMatchDistance;
                for (Prototype p:this){
                    int d = p.dist(norm);
                    if (d < bestSoFar) bestMatch = p; bestSoFar = d;
                }
                return bestSoFar;
            }

            private static int m = 10, w = 60;
            private static G.VS showBox = new G.VS(m, m, w, w);

            public void show(Graphics g) {
                //draw a list of boxes on top of screen
                g.setColor(Color.orange);
                for (int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i*(m + w); // 2nd box: m+m*w
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }
        }

    }

    // --------------------------- Database Functions -------------------------------
    public static HashMap<String, Shape> loadShapeDB() {
        String fileName = UC.shapeDBFile;
        HashMap<String, Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        return res;
    }

    public static void saveShapeDB(){}
    public static Shape recognize(Ink ink){ // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){return DOT;}
        Shape bestMatch = null;
        int bestSoFar = UC.noMatchDistance;
        for(Shape s:LIST) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar){bestMatch = s; bestSoFar = d;}
        }
        return bestMatch;
    } // stub


}

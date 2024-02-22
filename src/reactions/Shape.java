package reactions;


import graphics.G;
import music.I;
import music.UC;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable {
    public static Database DB = Database.load();  // database
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

            public void trainInk(Ink.Norm inkNorm) {
                if (bestDist(inkNorm) < UC.noMatchDistance) {
                    bestMatch.blend(inkNorm); // find a match, so blend
                } else {add(new Shape.Prototype());} // no match, new one created from ink buffer
            }

            private static int m = 10, w = 60;
            private static G.VS showBox = new G.VS(m, m, w, w);
            public static final int showBoxHeight = m + w;

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
    public static class Database extends HashMap<String, Shape> {
        public Database(){put("DOT", new Shape("DOT"));}
        public Shape forceGet(String name) {
            if (!containsKey(name)) {
                put(name, new Shape(name));
            }
            return get(name);
        }
        public void train(String name, Ink.Norm norm){
            if (isLegal(name)) {
                forceGet(name).prototypes.trainInk(norm);  // train a norm to the database
            }
        }
        public static boolean isLegal(String name){return !name.equals("") && !name.equals("DOT");}
        public static Database load() {
            String fileName = UC.shapeDBFile;
            Database res = null;

            try {
                System.out.println("Attempting DB load");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                res = (Database) ois.readObject();
                System.out.println("successful load - found:" + res.keySet()); // keys in the map
                ois.close();
            } catch (Exception e){
                System.out.println("load failed");
                System.out.println(e);
                res = new Database();
            };
            return res;
        }
        public static void save(){
            String fileName = UC.shapeDBFile;
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(DB);
                System.out.println("Saved " + fileName);
                oos.close();
            } catch (Exception e) {
                System.out.println("save failed");
                System.out.println(e);
            }
        }
    }



    public static Shape recognize(Ink ink){ // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){return DOT;}
        Shape bestMatch = null;
        int bestSoFar = UC.noMatchDistance;
        for(Shape s:LIST) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar){bestMatch = s; bestSoFar = d;}
        }
        if (bestMatch != null) {
            System.out.println("Recognized: " + bestMatch.name);
        } else {
            System.out.println("No");
        }
        return bestMatch;
    } // stub




}

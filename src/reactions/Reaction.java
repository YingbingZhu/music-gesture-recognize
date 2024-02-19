package reactions;

import music.I;
import music.UC;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Reaction implements I.React{ //Reaction looks for shape
    public Shape shape;
    private static Map byShape = new Map();
    public static List initialReactions = new List(); // used by undo to restart everything
    public Reaction(String shapeName){
        shape = Shape.DB.get(shapeName);
        if (shape == null){
            System.out.println("WTF? shapeDB does not know: " + shapeName);
        }
    }
    public void enable(){
        List list = byShape.getList(shape);
        if (!list.contains(this)) {list.add(this);} // add this reaction
    }
    public void disable(){
        List list = byShape.getList(shape);
        list.remove(this); // remove from list
    }
    public static Reaction best(Gesture g){
        return byShape.getList(g.shape).lowBid(g);
    }
    public static void nuke(){ // set undo
        byShape = new Map();
        initialReactions.enable();
    }
    // -------------------------List------------------------
    public static class List extends ArrayList<Reaction>{
        public void addReaction(Reaction r){
            add(r);
            r.enable();
        }
        public void removeReaction(Reaction r){
            remove(r);
            r.disable();
        }
        public void clearAll(){
            for (Reaction r: this) {
                r.disable();
            }
            clear();
        }
        public Reaction lowBid(Gesture g) {
            // can return null
             Reaction res = null;
             int bestSoFar = UC.noBid;
             for (Reaction r:this){
                 int b = r.bid(g);
                 if (b < bestSoFar) {bestSoFar = b; res = r;}
             }
             return res;
        }
        public void enable(){for(Reaction r:this){r.enable();}}
    }

    // -------------------------Map------------------------
    public static class Map extends HashMap<Shape, Reaction.List>{
        public List getList(Shape shape){
            // always succeed
            List res = get(shape);
            if (res == null) {res = new List(); put(shape, res);}
            return res;
        }
    }
}

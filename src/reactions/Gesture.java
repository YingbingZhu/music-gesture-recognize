package reactions;

import graphics.G;
import music.I;

import java.util.ArrayList;

public class Gesture {
    public static List UNDO = new List();
    public Shape shape;
    public G.VS vs;
    //  private: singleton pattern and factory methods
    private Gesture(Shape shape, G.VS vs){this.shape = shape; this.vs = vs;}
    public static Gesture getNew(Ink ink){
        // can return null
        Shape s = Shape.recognize(ink);
        return (s == null ? null: new Gesture(s, ink.vs));
    }
    private void doGesture(){ // add to undo stack
        Reaction r = Reaction.best(this);
        if(r != null){UNDO.add(this);r.act(this);}
    }
    private void redoGesture(){ // don't add to undo stack
        Reaction r = Reaction.best(this);
        if(r != null){r.act(this);}
    }
    public static void undo(){
        if(UNDO.size() > 0){
            UNDO.remove(UNDO.size()-1);
            Layer.nuke();
            Reaction.nuke();
            UNDO.redo();
        }
    }
    public static I.Area AREA = new I.Area() {  //anonymous class
        @Override
        public boolean hit(int x, int y) {return true;}
        @Override
        public void dn(int x, int y) {Ink.BUFFER.dn(x, y);}
        @Override
        public void drag(int x, int y) {Ink.BUFFER.drag(x, y);}
        @Override
        public void up(int x, int y) {
            Ink.BUFFER.add(x, y);   // last point into the buffer
            Ink ink = new Ink();
            Gesture ges = Gesture.getNew(ink);  // can fail
            Ink.BUFFER.clear();
            if (ges != null) {
                if (ges.shape.name.equals("N-N")){
                    undo();
                } else {
                    ges.doGesture();
                }
            }
        }
    };

    // ------------------ list --------------------
    public static class List extends ArrayList<Gesture>{
        private void redo(){for(Gesture g:this){g.redoGesture();}}
    }

}

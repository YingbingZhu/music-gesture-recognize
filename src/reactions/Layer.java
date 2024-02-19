package reactions;

import music.I;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
    deal with z-order
 */
public class Layer extends ArrayList<I.Show> implements I.Show{ // put interface in arraylist,
    public static HashMap<String, Layer> byName = new HashMap<>(); // constructor use byName
    public static Layer ALL = new Layer("ALL");

    public String name;

    public Layer(String name){
        this.name = name;
        if (!name.equals("ALL")) {
            ALL.add(this); // add a new layer
        }
        byName.put(name, this);
    }
    public void show(Graphics g){for(I.Show item:this){item.show(g);}}
    public static void nuke(){for(I.Show lay:ALL){((Layer) lay).clear();}} // cast lay to Layer
}

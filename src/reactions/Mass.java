package reactions;
import music.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show {
    public Layer layer;
    public Mass(String layerName){
        layer = Layer.byName.get(layerName);
        if (layer != null){layer.add(this);}  // mass into layer
        else {
            System.out.println("Bad layer name: " + layerName);
        }
    }

    public void delete(){
        clearAll(); // clear all the reactions from the list, shape map
        layer.remove(this); // remove mass from layer
    }

    public void show(Graphics g){}
}

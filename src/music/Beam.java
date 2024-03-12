package music;

import reactions.Mass;

public class Beam extends Mass{
    public Stem.List stems = new Stem.List();
    public Beam(Stem f, Stem l) {super("NOTE"); addStem(f);addStem(l);}
    public Stem first(){return stems.get(0);}
    public Stem last(){return stems.get(stems.size()-1);}
    public void deleteBeam(){for(Stem s:stems){s.beam = null;}deleteMass();}
    public void addStem(Stem s){if (s.beam == null){stems.add(s);s.beam = this;stems.sort();}}
    public static int yOfX(int x, int x1, int x2, int y1, int y2){
        int dy = y2 - y1, dx = x2 - x1;
        return (x - x1)*dy/dx + y1;
    }
    public static int mx1, my1, mx2, my2; // coordinates for master beam, the first beam
    public static int yOfX(int x){
        int dy = my2 - my1, dx = mx2 - mx1;
        return (x - mx1)*dy/dx + my1;
    }
    public static void setMasterBeam(int x1, int y1, int x2, int y2){mx1 = x1; mx2 = x2; my1 = y1; my2 = y2;}
    public void setMasterBeam(){mx1 = first().x(); my1 = first().yBeamEnd();mx2 = last().x(); my2 = last().yBeamEnd();}
}

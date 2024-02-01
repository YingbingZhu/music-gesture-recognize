package reactions;
import graphics.G;
import music.I;
import music.UC;

import java.awt.*;
import java.util.ArrayList;

public class Ink extends G.Pl implements I.Show {
    public static Buffer BUFFER = new Buffer();
    public static G.VS TEMP = new G.VS(100, 100, 100, 100);
    public Ink(){
        super(UC.normSampleSize); // sub-sample buffer
        Ink.BUFFER.subSample(this);
        G.V.T.set(BUFFER.bbox, TEMP);
        transform();
        G.V.T.set(TEMP, BUFFER.bbox.getNewVS());
        transform();
//        super(BUFFER.n);
//        System.out.println("buffer cnt:" + BUFFER.n);
//        for(int i=0;i<BUFFER.n;i++){
//            points[i].set(BUFFER.points[i]);
//        }
    }

    @Override
    public void show(Graphics g) {
       this.draw(g);
    }
    // --------------------------- Buffer ------------------------
    public static class Buffer extends G.Pl implements I.Show, I.Area{
        public static final int MAX = UC.inkBufferMax;
        public int n; // number of points in buffer
        public G.BBox bbox = new G.BBox();
        private Buffer(){super(MAX);} // build a singleton,
        public void clear(){n=0;}
        public void add(int x, int y){if(n < MAX){points[n++].set(x, y);bbox.add(x, y);}} // after operation, n++
        @Override
        public void show(Graphics g) {this.drawN(g, n);bbox.draw(g);}
        @Override
        public boolean hit(int x, int y) {return true;}
        @Override
        public void dn(int x, int y) {clear();add(x, y);bbox.set(x, y);}
        public void subSample(G.Pl pl) {
            int k = pl.size(); // # of points in the pl
            for (int i = 0; i < k; i++) {
                pl.points[i].set(this.points[i * (n - 1)/ ( k - 1)]); // linear function
            }
        }
        @Override
        public void up(int x, int y) {add(x, y);}
        @Override
        public void drag(int x, int y) {add(x, y);}

    }
    // --------------------------- List----------------------------
    public static class List extends ArrayList<Ink> implements I.Show {
        @Override
        public void show(Graphics g) {
            for (Ink ink: this){
                ink.show(g);
                if (ink.size() > 0){
                    System.out.println("(" + ink.points[0].x + ", " + ink.points[0].y + ")");
                };
            }
        }
    }

    // ------------------------ Norm --------------
    public static class Norm extends G.Pl{
        public static final int N = UC.normSampleSize, MAX = UC.maxNormCoordinate;

        public static final G.VS NCS = new G.VS(0, 0, MAX, MAX); // normalized coordinates

        public Norm() {
            super(N);
            BUFFER.subSample(this);
            G.V.T.set(BUFFER.bbox, NCS); // transform
            transform(); // transform of points
        }

        public void drawAt(Graphics g, G.VS vs) {
            G.V.T.set(NCS, vs);
            for (int i = 1; i < N;i++) {
                g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());
            }
        }



    }
}

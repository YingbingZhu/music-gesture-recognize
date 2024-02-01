package music;

import java.awt.*;

/*

 */
public interface I {
    /*

     */
    public static interface Area{
        public boolean hit(int x, int y);
        public void dn(int x, int y); // down
        public void up(int x, int y);
        public void drag(int x, int y);

    }

    public static interface Show{
        public void show(Graphics g);
    }

}

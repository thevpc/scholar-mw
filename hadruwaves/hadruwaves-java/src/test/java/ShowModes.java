import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.Physics;
import net.thevpc.scholar.hadruwaves.WallBorders;

/**
 * Created by vpc on 5/20/17.
 */
public class ShowModes {
    public static void main(String[] args) {
//        Plot.plot(new CExp(1,1,0, Domain.forBounds(0,10,0,10)));
//        Plot.plot(Maths.add(Maths.cos(Maths.X),Maths.cos(Maths.X)));
        //System.out.println(Physics.lambda(25*Physics.GHZ));
//        for (WallBorders wallBorders : WallBorders.values()) {
//            show(wallBorders.toString());
//        }
        //show("EEEE",0.0229,0.0102);
//        show("MEEEE", 0, -50/2 * Physics.MILLIMETER, 850 * Physics.MILLIMETER, 50 * Physics.MILLIMETER, 4.79 * Physics.GHZ);

        //show("EEEM", 0, -50/2 * Physics.MILLIMETER, 850 * Physics.MILLIMETER, 50 * Physics.MILLIMETER, 4.79 * Physics.GHZ);
//        for (WallBorders wallBorders : WallBorders.values()) {
//            show(wallBorders.toString(), 0, -50/2 * Maths.MILLIMETER, 850 * Maths.MILLIMETER, 50 * Maths.MILLIMETER, 4.79 * Maths.GHZ);
//        }
        Maths.Config.setCacheExpressionPropertiesEnabled(false);
        Physics.plotWallBorders(WallBorders.PPPP);
//        Physics.plotWallBorders(WallBorders.EEME, 0, -50 / 2 * Maths.MILLIMETER, 850 * Maths.MILLIMETER, 50 * Maths.MILLIMETER, 4.79 * Maths.GHZ);


//        for (WallBorders wallBorders : WallBorders.values()) {
//            show(wallBorders.toString(), 0, -50 / 2 * Maths.MILLIMETER, 850 * Maths.MILLIMETER, 50 * Maths.MILLIMETER, 4.79 * Maths.GHZ);
//        }
//        show("MMEM", 0, -50/2 * Maths.MILLIMETER, 850 * Maths.MILLIMETER, 50 * Maths.MILLIMETER, 4.79 * Maths.GHZ);
//        show("EEEE", 0, -50/2 * Maths.MILLIMETER, 850 * Maths.MILLIMETER, 50 * Maths.MILLIMETER, 4.79 * Maths.GHZ);
    }
}

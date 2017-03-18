package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */

import epmc.ptaxdta.IntegerValueInterval;

import java.util.ArrayList;
import java.util.Vector;

public class RegionElement {
    private RegionSpace space;
    private IntegerValueInterval [] J;
    private ArrayList<Integer> fracOrder;
    private ArrayList<Integer>  D;

    public RegionElement(RegionSpace space,IntegerValueInterval [] J, ArrayList<Integer> fracOrder,ArrayList<Integer>  D) {
        this.space      = space;
        this.J          = J;
        this.fracOrder  = fracOrder;
        this.D          = D;
    }

    public RegionSpace getSpace() {
        return space;
    }

    public void setSpace(RegionSpace space) {
        this.space = space;
    }

    public int getDemension() {
        return this.space.getDemension();
    }
}

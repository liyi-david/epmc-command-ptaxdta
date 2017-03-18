package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */

import epmc.ptaxdta.IntegerValueInterval;

import java.util.ArrayList;
import java.util.Vector;

public class RegionElement {
    private RegionSpace space;
    private IntegerValueInterval[] J;
    private ArrayList<Integer> fracOrder;
    private int D;

    public RegionElement(RegionSpace space, IntegerValueInterval[] J, ArrayList<Integer> fracOrder, int D) {
        this.space = space;
        this.J = J;
        this.fracOrder = fracOrder;
        this.D = D;
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

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < this.getDemension(); i++) {
            res += this.getSpace().getClockName()[i] + " : " + this.J[i] + "\n";
        }

        if (this.fracOrder.size() != 0) {
            res += this.getSpace().getClockName()[fracOrder.get(0)];
            for (int i = 1; i < this.fracOrder.size(); i++) {
                String symbol = (((this.D >> (i - 1)) & 1) == 1) ? "=" : "<" ;
                res += symbol + this.getSpace().getClockName()[this.fracOrder.get(i)];

            }
            res += "\n";
        }
        return res;
    }
}

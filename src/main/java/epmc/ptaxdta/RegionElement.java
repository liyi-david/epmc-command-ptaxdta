package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */
import com.alibaba.fastjson.*;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionElement {
    private RegionSpace space;
    private IntegerValueInterval[] J; // [0,this.getDimension)
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

    public int getDimension() {
        return this.space.getDimension();
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < this.getDimension(); i++) {
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

    public JSONObject generateJSON(){
//        TreeMap<String,Integer> config = new TreeMap<String,Integer>();
//        JsonBuilderFactory factory = Json.createBuilderFactory(config);
//
//        JsonObjectBuilder builder =  factory.createObjectBuilder();
        ArrayList v = new ArrayList();
        for(int i=0; i < this.getDimension(); i++){
            IntegerValueInterval interv = this.J[i];
            if (interv.isPoint()){ // not necessary
                HashMap m = new HashMap();
                m.put("op","=");
                m.put("left",this.space.getClockName()[i]);
                m.put("right",interv.pointValue());
                v.add(m);
            }
            else {
                HashMap m = new HashMap();
                m.put("op",(interv.lowerClosed == 1) ? "≤" : "<");
                m.put("left",interv.lower);
                m.put("right",this.space.getClockName()[i]);
                v.add(m);

                m = new HashMap();
                m.put("op",(interv.upperClosed == 1) ? "≤" : "<");
                m.put("left",this.space.getClockName()[i]);
                m.put("right",interv.upper);
                v.add(m);
            }
        }
        if (this.fracOrder.size() > 1) {
            for (int i = 1; i < this.fracOrder.size(); i++) {
                String symbol = (((this.D >> (i - 1)) & 1) == 1) ?  "≤" : "<" ;
                HashMap d = new HashMap();
                d.put("op","+");
                d.put("left",this.space.getClockName()[this.fracOrder.get(i)]);
                d.put("right",this.J[this.fracOrder.get(i-1)].lower - this.J[this.fracOrder.get(i)].lower);

                HashMap m = new HashMap();
                m.put("op",symbol);
                m.put("left",this.space.getClockName()[this.fracOrder.get(i-1)]);
                m.put("right",d);

                v.add(m);
            }
        }

        HashMap res = new HashMap();
        HashMap cur = res;
        for(int i=0; i < v.size() - 1; i++){
            HashMap next = new HashMap();
            cur.put("op","∧");
            cur.put("left",v.get(i));
            cur.put("right",next);
            cur = next;
        }
        cur.put("right",v.get(v.size()-1));
//        return 0;
        return (JSONObject) JSON.toJSON(res);
    }
}

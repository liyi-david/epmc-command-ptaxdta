package epmc.command.util;

import epmc.error.EPMCException;
import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.Region;
import epmc.ptaxdta.pta.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by lijianlin on 17/4/6.
 */
public class UtilProductV2 {

    private ArrayList<ClockConstraint> h;

    public ModelPTA prod(ModelPTA pta, ModelPTA dta) throws EPMCException {
        // TODO: assertions
        ModelPTA result = new ModelPTA("Product[" + pta.getName() + "," + dta.getName() + "]");
        result.setContextValue(pta.getContextValue());

        // clocks
        result.clocks = new ClocksPTA();
//		result.clocks.clocknames = (ArrayList<String>) pta.clocks.clocknames.clone();
//		result.clocks.clocknames.addAll((Collection<? extends String>) dta.clocks.clocknames.clone());

        ClocksPTA X1 = pta.getSpace().getExternalClocks();
        ClocksPTA X2 = dta.getSpace().getExternalClocks();
        result.clocks.clocknames.addAll(X1.clocknames);
        result.clocks.clocknames.addAll(X2.clocknames);

        ClockSpace space = new ClockSpace(result.clocks);
        space.setModel((Model) pta);
        result.setSpace(space);

        Region zero = Region.ZERO(dta.getSpace());// regions use dta space
        Region NULL = zero;

        dta.getSpace().setBoundary(new int[]{0, 4, 6});
//        dta.getSpace().explore();
        ArrayList<Region> G = dta.getSpace().getElements();

        // TODO: find a solution to write simpler clock constraint
//		for (int i = 0; i < result.clocks.clocknames.size(); i ++) {
//			zerocc.setAnd(new AtomConstraint(i + 1, 0, 0, false));
//		}

//        ArrayList<LocationPTAProduct> visited = new ArrayList<>();
        //TODO visited : L x Q x G -> {0,1}

        Queue<LocationPTAProduct> Q = new LinkedList<LocationPTAProduct>();

        APSet S = dta.getAP();


        for (int i = S.empty(); i < S.bound(); i++) {
            LabelPTA l = S.LabelWithSet(i);
            System.out.println(l);
        }

        for (LocationPTA l : dta.locations.getLocations()){
            LocationPTABasic q = (LocationPTABasic) l;
            this.h = new ArrayList<>();
            this.dfs(0,dta,q);
        }
        // NOTE: actions of dta are actually labels or atomic propositions
        result.actions = (ArrayList<ActionPTA>) pta.actions.clone();
        return result;
    }
    public void dfs(int ch,ModelPTA dta,LocationPTABasic q){
//        System.out.println(ch);
        APSet S = dta.getAP();
        if(ch == S.bound()){
            System.out.println("\nT_" + q.getName() +"  have");
            for (ClockConstraint g : this.h) {
                System.out.println(g);
            }
            return;
        }
        LabelPTA label = S.LabelWithSet(ch);
        ArrayList<TransitionPTA> E_q = dta.transitions.get(q);
        ArrayList<ClockConstraint> guards = new ArrayList<>();
        for (TransitionPTA e_q : E_q) {
            if (e_q.action.equals(label)){
                guards.add(e_q.guard);

            }
        }
        if (guards.size() == 0){
            ClockConstraint TOP = ClockConstraint.TOP(dta.getSpace());
            this.h.add(TOP);
            dfs(ch + 1,dta,q);
            this.h.remove(this.h.size()-1);
        }
        for (ClockConstraint g : guards) {
            this.h.add(g);
//            System.out.println(g);
            dfs(ch + 1,dta,q);
            this.h.remove(this.h.size()-1);
        }

    }
}

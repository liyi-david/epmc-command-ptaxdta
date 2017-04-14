package epmc.command.util;

import epmc.error.EPMCException;
import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.UtilDBM;
import epmc.ptaxdta.pta.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by lijianlin on 17/4/6.
 */
public class UtilProductV2 {

    private ArrayList<ClockConstraint> temph;

    public ModelPTA prod(ModelPTA pta, ModelPTA dta) throws EPMCException {
    	if (dta.getName() == pta.getName()) {
    		dta.setName(dta.getName() + "-prop");
    	}
    	
        ModelPTA result = new ModelPTA("Product[" + pta.getName() + "," + dta.getName() + "]");
        result.setContextValue(pta.getContextValue());

        /*
         * algorthm sketch
         * 1. locations - done.
         * 2. initial location - tbd.
         * 3. clocks - done.
         * 4. actions - done.
         * 5. invariants - done. need further check
         * 6. enab(guard) - tbd.
         * 7. prob(transition) - tbd. 
         */


        // -------------------------------------- CLOCKS ------------------------------------------
        // 3.1 construct clock set
        result.clocks = new ClocksPTA();

        ClocksPTA X1 = pta.getSpace().getExternalClocks();
        ClocksPTA X2 = dta.getSpace().getExternalClocks();
        result.clocks.clocknames.addAll(X1.clocknames);
        result.clocks.clocknames.addAll(X2.clocknames);
        
        // 3.2 construct clock spce
        ClockSpace space = new ClockSpace(result.clocks);
        space.setModel((Model) result);
        result.setSpace(space);

        Queue<LocationPTAProductV2> Q = new LinkedList<LocationPTAProductV2>();

       
        // -------------------------------------- initial location ------------------------------------------
        for (LocationPTA initm : pta.initialLocations.getLocations()) {
            LabelPTA initmLable = pta.label.get(initm);
            for (LocationPTA initp : dta.initialLocations.getLocations()) {
                ArrayList<TransitionPTA> E_initp = dta.transitions.get(initp);
                for (TransitionPTA e_initp : E_initp) {
                    LabelPTA b = (LabelPTA) e_initp.action;
                    if (initmLable.equals(b)) {
                    	// ClockConstraint inv = (ClockConstraint) pta.invariants.get(initm).clone();
                    	// FIXME use clone here will lead to error
                        ClockConstraint inv = (ClockConstraint) pta.invariants.get(initm);
                    	
                        ClockConstraint newInv = ClockConstraint.TOP(space);
                        newInv.setAnd(UtilDBM.UDBMString2Federation(
                                inv.toUDBMString(),
                                result.getSpace()
                        ));

                        LocationPTA loc = new LocationPTAProductV2(initm, e_initp.target.get(0));
                        loc.setModel(result);
                        result.invariants.put(loc, newInv);
                        result.locations.addLocation(loc);
                        result.initialLocations.addLocation(loc);
                        Q.add((LocationPTAProductV2) loc);
                    }
                }

            }
        }

        // -------------------------------- TQ ACTIONS ------------------------------------
        // T is used to store all the possible (Tq)s
        HashMap<LocationPTABasic,ArrayList<HashMap<ActionPTA, ClockConstraint>>> T =
                new HashMap<LocationPTABasic, ArrayList<HashMap<ActionPTA,ClockConstraint>>>();

        //  moved outside locpta-for-loop
        for (LocationPTA locdta : dta.locations.getLocations()) {
            // find all possible actions (i.e. Tqs) from the current location
            // IMPORTANT !!! only if is hasn't been explored before
            assert (locdta instanceof LocationPTABasic);
            this.temph = new ArrayList<>();
            this.dfs(0, dta, (LocationPTABasic)locdta, T);
            System.out.println("T_" + locdta.getName());
        }

        // ------------------------------- ACTIONS ------------------------------------
        result.actions = new ArrayList<>();
        for (LocationPTA locdta : dta.locations.getLocations()) {
            for (ActionPTA actpta : pta.actions) {
                for (HashMap<ActionPTA, ClockConstraint> acttq : T.get(locdta)) {
                    result.actions.add(
                            new ActionStandardPTAProductV2(actpta, acttq)
                    );
                }
            }
        }
        while (!Q.isEmpty()) {

            LocationPTAProductV2 head = Q.poll();
            LocationPTA l0 = head.getPTAloc();
            LocationPTA q0 = head.getDTAloc();

            ArrayList<TransitionPTA> E_l0 = pta.transitions.get(l0);
            ArrayList<TransitionPTA> E_q0 = dta.transitions.get(q0);

            if ((E_l0 == null) || (E_q0 == null)) continue;

            for (TransitionPTA e_l : E_l0) {
                ActionStandardPTA a = (ActionStandardPTA) e_l.action;
                ClockConstraint g0 = e_l.guard;

                for (HashMap<ActionPTA, ClockConstraint> h : T.get(q0)) { // (l,q) \in L_x and  temph \in Tq

                    for (int i = 0; i < e_l.target.size(); i++) {
                        LocationPTA l1 = e_l.target.get(i);
                        ClocksPTA Y1 = e_l.rstClock.get(i);
                        LabelPTA Ll1 = pta.label.get(l1);
                        Double prob0 = e_l.prob.get(i);

                        // PTA l0 --- g0 : a --- * --- prob0,Y1 ---> l1

                        for (TransitionPTA e_q : E_q0) {
                            LabelPTA b = (LabelPTA) e_q.action;
                            ClockConstraint g1 = e_q.guard;
                            for (int j = 0; j < e_q.target.size(); j++) {
                                LocationPTA q1 = e_q.target.get(j);
                                ClocksPTA Y2 = e_q.rstClock.get(j);
                                // (q0,b,g,Y2,q1) \in \delta
                                // DTA q0 --- g1 : b , Y2 ---> q1
                                ClockConstraint hLl1 = UtilProductV2.lookUpMap(h,Ll1);

                                if ((Ll1.equals(b)) && (hLl1.satisfy(g1))) {
                                    // if (q,L(l′),temph(L(l′)),Y ∩ X2, q′) ∈ ∆.
                                    // if (10, Ll1, hLl1, Y2, q1) \in delta

                                    // (l0,q0) --- g0 and conj_h : (a,h) --- * prob0, Y1 U Y2 ---> (l1,q1)

                                    LocationPTAProductV2 state = new LocationPTAProductV2(l1, q1);
                                    int idx = result.locations.getLocations().indexOf(state);
                                    Boolean isVisited = idx >= 0;
                                    if (!isVisited) {
                                        // ------ LOCATIONS ------
//                                        state.setModel(result);
                                        result.locations.addLocation(state);
                                        ClockConstraint newInv = ClockConstraint.TOP(space);
                                        newInv.setAnd(UtilDBM.UDBMString2Federation(
                                                pta.invariants.get(state.getPTAloc()).toUDBMString(),
                                                result.getSpace()
                                        ));
                                        
                                        result.invariants.put(
                                                state,
                                                newInv
                                        );
                                        Q.add(state);
                                    }
                                    else {
                                        state = (LocationPTAProductV2) result.locations.getLocations().get(idx);
                                    }

                                    ClockConstraint guard = ClockConstraint.TOP(result.getSpace());
                                    guard.setAnd(
                                            UtilDBM.UDBMString2Federation(g0.toUDBMString(),
                                                    result.getSpace()
                                            )
                                    );
                                    ClockConstraint conj_h = this.h_conjunction(h,dta.getSpace());
                                    guard.setAnd(
                                            UtilDBM.UDBMString2Federation(conj_h.toUDBMString(),
                                                    result.getSpace()
                                            )
                                    );

                                    ClocksPTA Y = new ClocksPTA();
                                    Y.clocknames.addAll(Y1.clocknames);
                                    Y.clocknames.addAll(Y2.clocknames);

                                    System.out.println(head + "   ---  gurd " + guard.toString() + " = " + g0.toString() + " and " + conj_h.toString() + " : (" + e_l.action.contentString() + ", "+ h +")"
                                                            + "---*---" + prob0 + "," + Y1 + " U " + Y2 + " --->   " + state);
                                    result.addBranchingFrom(head,new ActionStandardPTAProductV2(e_l.action,h),guard)
                                            .addTarget(prob0,Y,state);

                                }

                            }

                        }
                    }


                }
            }
        }

//        for (LocationPTABasic q : T.keySet()) {
//            ArrayList<ArrayList<ClockConstraint>> Tq = T.get(q);
//            System.out.println("T_" + q.getName());
//            for (ArrayList<ClockConstraint> temph : Tq){
//                System.out.println();
//                for (ClockConstraint g : temph) {
//                    System.out.println(g);
//                }
//                System.out.println("conj over sigma : " + this.h_conjunction(temph,dta.getSpace()));
//                System.out.println();
//            }
//        }
//
        return result;
    }
    
    public void dfs(
    		int ch, ModelPTA dta,LocationPTABasic q,
    		HashMap<LocationPTABasic,ArrayList<HashMap<ActionPTA, ClockConstraint>>> T)
    {

    	// @Jianlin: here I removed the APSet, and use actions instead
    	// and we always assume that the actions are already filled with set of labels
    	// automatically before executing this function
    	
        if(ch == dta.actions.size()){
//            System.out.println("\nT_" + q.getName() +"  have");
//            for (ClockConstraint g : this.temph) {
//                System.out.println(g);
//            }
            ArrayList<HashMap<ActionPTA, ClockConstraint>> Tq = T.get(q);
            if (Tq == null){
                Tq = new ArrayList<>();
                T.put(q,Tq);
            }

            // from ArrayList<CC> to HashMap<Action, CC>
            // maybe we call it a schedule?
            HashMap<ActionPTA, ClockConstraint> schedule =
            		new HashMap<ActionPTA, ClockConstraint> ();
            for (ActionPTA act : dta.actions) {
            	schedule.put(act, temph.get(dta.actions.indexOf(act)));
            }
            Tq.add(schedule);
            return;
        }
        
        LabelPTA label = (LabelPTA) dta.actions.get(ch);
        ArrayList<TransitionPTA> E_q = dta.transitions.get(q);
        ArrayList<ClockConstraint> guards = new ArrayList<>();
        for (TransitionPTA e_q : E_q) {
            if (e_q.action.equals(label)){
                guards.add(e_q.guard);

            }
        }
//        assert guards.size() > 0;
//        System.out.println("sigma = " + ch + ", q = " + q.getName() + ", gurad size " + guards.size());
        if (guards.size() == 0){
            ClockConstraint TOP = ClockConstraint.TOP(dta.getSpace());
            this.temph.add(TOP);
            dfs(ch + 1,dta,q,T);
            this.temph.remove(this.temph.size()-1);
        }
        for (ClockConstraint g : guards) {
            this.temph.add(g);
//            System.out.println(g);
            dfs(ch + 1,dta,q,T);
            this.temph.remove(this.temph.size()-1);
        }

    }
    private ClockConstraint h_conjunction(HashMap<ActionPTA, ClockConstraint> h,ClockSpace space){
        ClockConstraint res = ClockConstraint.TOP(space);
        for (ActionPTA act : h.keySet()) {
            res.setAnd(h.get(act));
        }
        return res;
    }
    public static ClockConstraint lookUpMap(HashMap<ActionPTA,ClockConstraint> map, LabelPTA l){
        for (ActionPTA key : map.keySet()){
            if (key.equals(l)){
                return map.get(key);
            }
        }
        return null;
    }
}

package epmc.command.util;

import epmc.error.EPMCException;
import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.Region;
import epmc.ptaxdta.UtilDBM;
import epmc.ptaxdta.pta.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
        space.setModel((Model) pta);
        result.setSpace(space);
        
        // T is used to store all the possible (Tq)s
        HashMap<LocationPTABasic,ArrayList<HashMap<ActionPTA, ClockConstraint>>> T =
        		new HashMap<LocationPTABasic, ArrayList<HashMap<ActionPTA,ClockConstraint>>>();
        
        for (LocationPTA locpta : pta.locations.getLocations()) {
        	for (LocationPTA locdta : dta.locations.getLocations()) {
        		// -------------------------------- LOCATIONS --------------------------------------
        		// add this location to the result set
        		LocationPTA currentLoc = 
        				result.locations.addLocation(new LocationPTAProductV2(locpta, locdta));
        		
        		// fix the corresponding invariants
        		// TODO: check if it is the proper way to convert a clock constraint in the 
        		// original pta to a literally same constraint in the product pta
        		ClockConstraint newInv = ClockConstraint.TOP(space);
        		newInv.setAnd(UtilDBM.UDBMString2Federation(
						pta.invariants.get(locpta).toUDBMString(),
						result.getSpace()
						));

        		result.invariants.put(
        				currentLoc,
        				newInv
        				);
        		
        		// -------------------------------- TQ ACTIONS ------------------------------------
        		// find all possible actions (i.e. Tqs) from the current location
        		// IMPORTANT !!! only if is hasn't been explored before
        		if (!T.containsKey(locdta)) {
        			assert (locdta instanceof LocationPTABasic);
        			this.h = new ArrayList<>();
                    this.dfs(0, dta, (LocationPTABasic)locdta, T);
                    
                    // System.out.println(T.get(locdta));
        		}
        		
        		// ------------------------------- TRANSITIONS ------------------------------------
        		result.actions = new ArrayList<>();
        		for (ActionPTA actpta : pta.actions) {
        			for (HashMap<ActionPTA, ClockConstraint> acttq : T.get(locdta)) {
        				result.actions.add(
        						new ActionStandardPTAProductV2(actpta, acttq)
        						);
        				
        				// TODO: calculate the guard of its corresponding transition
        				// TODO: calculate the assignments of its corresponding transition
        			}
        		}
        	}
        }


//        for (LocationPTABasic q : T.keySet()) {
//            ArrayList<ArrayList<ClockConstraint>> Tq = T.get(q);
//            System.out.println("T_" + q.getName());
//            for (ArrayList<ClockConstraint> h : Tq){
//                System.out.println();
//                for (ClockConstraint g : h) {
//                    System.out.println(g);
//                }
//                System.out.println("conj over sigma : " + this.h_conjunction(h,dta.getSpace()));
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
//            for (ClockConstraint g : this.h) {
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
            	schedule.put(act, h.get(dta.actions.indexOf(act)));
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
            this.h.add(TOP);
            dfs(ch + 1,dta,q,T);
            this.h.remove(this.h.size()-1);
        }
        for (ClockConstraint g : guards) {
            this.h.add(g);
//            System.out.println(g);
            dfs(ch + 1,dta,q,T);
            this.h.remove(this.h.size()-1);
        }

    }
    private ClockConstraint h_conjunction(ArrayList<ClockConstraint> h,ClockSpace space){
        ClockConstraint res = ClockConstraint.TOP(space);
        for (ClockConstraint g : h) {
            res.setAnd(g);
        }
        return res;
    }

}

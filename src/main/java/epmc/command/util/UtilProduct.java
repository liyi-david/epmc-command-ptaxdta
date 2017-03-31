package epmc.command.util;

import java.util.*;

import epmc.jani.model.Action;
import epmc.jani.model.Location;
import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.Region;
import epmc.ptaxdta.pta.model.*;
import epmc.udbm.AtomConstraint;

public class UtilProduct {
	public static ModelPTA prod(ModelPTA pta, ModelPTA dta) {
		// TODO: assertions
		ModelPTA result = new ModelPTA("Product[" + pta.getName() + "," + dta.getName() + "]");
		result.setContextValue(pta.getContextValue());
		
		// clocks
		result.clocks = new ClocksPTA();
		result.clocks.clocknames = (ArrayList<String>) pta.clocks.clocknames.clone();
		result.clocks.clocknames.addAll((Collection<? extends String>) dta.clocks.clocknames.clone());
		
		ClockSpace space = new ClockSpace(result.clocks);
		space.setModel((Model) pta);
		Region zero = Region.ZERO(space);

		// TODO: find a solution to write simpler clock constraint
//		for (int i = 0; i < result.clocks.clocknames.size(); i ++) {
//			zerocc.setAnd(new AtomConstraint(i + 1, 0, 0, false));
//		}

		for (LocationPTA initm : pta.initialLocations.getLocations()) {
			for (LocationPTA initp : dta.initialLocations.getLocations()) {
				LocationPTAProduct loc = new LocationPTAProduct(initm, initp, zero);
				loc.setModel(result);
				ClockConstraint invx = (ClockConstraint) pta.invariants.get(loc).clone();
				invx.setAnd(zero);
				result.invariants.put(loc, invx);
				result.locations.addLocation(loc);
			}
		}

		Queue<LocationPTAProduct> Q = new LinkedList<LocationPTAProduct>();
		while (!Q.isEmpty()){
			LocationPTAProduct head = Q.poll();
			LocationPTA l0 = head.getPTAloc();
            LocationPTA q0 = head.getDTAloc();
            Region      R0 = head.getRegion();

            ArrayList<TransitionPTA> E_l0 = pta.transitions.get(l0);
            ArrayList<TransitionPTA> E_q0 = pta.transitions.get(q0);

            for (TransitionPTA e_l : E_l0) {
                String  a  = e_l.action.contentString();
                for (int i = 0; i < e_l.target.size(); i++) {
                    LocationPTA l1 = e_l.target.get(i);
                    for (TransitionPTA e_q : E_q0) {
                        for (int j = 0; j < e_q.target.size(); j++) {
                            LocationPTA q1 = e_q.target.get(j);



                        }

                    }
                }
            }

		}
		/* TODO
		 * 1. find the upper bound of each clock variable
		 * 2. explore the region space
		 * 3. create transitions based on two rules
		 *    - time evolution
		 *    - product transition (refer to Fu's paper)
		 */



		// NOTE: actions of dta are actually labels or atomic propositions
		result.actions = (ArrayList<String>) pta.actions.clone();
		return result;
	}
}

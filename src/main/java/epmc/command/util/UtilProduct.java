package epmc.command.util;

import java.util.*;

import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.Region;
import epmc.ptaxdta.pta.model.*;

public class UtilProduct {
	public static ModelPTA prod(ModelPTA pta, ModelPTA dta) {
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
		Region zero = Region.ZERO(space);

		// TODO: find a solution to write simpler clock constraint
//		for (int i = 0; i < result.clocks.clocknames.size(); i ++) {
//			zerocc.setAnd(new AtomConstraint(i + 1, 0, 0, false));
//		}

        ArrayList<LocationPTAProduct> visited = new ArrayList<>();
        //TODO visited : L x Q x G -> {0,1}

        Queue<LocationPTAProduct> Q = new LinkedList<LocationPTAProduct>();

		for (LocationPTA initm : pta.initialLocations.getLocations()) {
			for (LocationPTA initp : dta.initialLocations.getLocations()) {
				LocationPTAProduct loc = new LocationPTAProduct(initm, initp, zero);
				loc.setModel(result);
				ClockConstraint invx = (ClockConstraint) pta.invariants.get(initm).clone();
				invx.setAnd(zero);
				result.invariants.put(loc, invx);
				result.locations.addLocation(loc);
				Q.add(loc);
				visited.add(loc);
			}
		}

		while (!Q.isEmpty()){

//		    if(visited.size()>100) continue;

			LocationPTAProduct head = Q.poll();
			LocationPTA l0 = head.getPTAloc();
            LocationPTA q0 = head.getDTAloc();
            Region      R0 = head.getRegion();

            ArrayList<TransitionPTA> E_l0 = pta.transitions.get(l0);
            ArrayList<TransitionPTA> E_q0 = dta.transitions.get(q0);

            if ((E_l0 == null) || (E_q0 == null)) continue;
            for (TransitionPTA e_l : E_l0) {
                ActionStandardPTA a  = (ActionStandardPTA) e_l.action;
                ClockConstraint g0 = e_l.guard;

                for (int i = 0; i < e_l.target.size(); i++) {
                    LocationPTA l1 = e_l.target.get(i);
                    ClocksPTA Y1 = e_l.rstClock.get(i);
                    LabelPTA Ll1 = pta.label.get(l1);
                    Double prob0 = e_l.prob.get(i);
                    // PTA l0 --- g0 : a --- * --- prob0,Y1 ---> l1

                    for (TransitionPTA e_q : E_q0) {
                        LabelPTA b = (LabelPTA)e_q.action;
                        ClockConstraint g1 = e_q.guard;

                        for (int j = 0; j < e_q.target.size(); j++) {
                            LocationPTA q1 = e_q.target.get(j);
                            ClocksPTA Y2 = e_q.rstClock.get(j);
                            // (q0,b,g,Y2,q1) \in \delta
							// DTA q0 --- g1 : b , Y2 ---> q1

                            if ((Ll1.equals(b)) && (R0.isModelof(g1))){
                                Region R1 = R0.clone();
                                R1.reset(space.findClockbyName(Y2));
//								System.out.println("R1.equals(R0)" + R1.equals(R0));

                                // (l0,q0,R0) --- g0 : a --- * prob1,Y1 U Y2 ---> (l1,q1,R1)
								// R1 = R0[ Y1 U Y2 := 0 ]
                                LocationPTAProduct state = new LocationPTAProduct(l1,q1,R1);
                                //TODO LocationPTAProduct equals
                                int idx = visited.indexOf(state);
                                Boolean isVisited = visited.indexOf(state) >= 0; //TODO visited
                                if(!isVisited){
                                    System.out.println(state);
                                    //TODO set visited
                                    Q.add(state);
                                    visited.add(state);
                                }
                                System.out.println();
                            }

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

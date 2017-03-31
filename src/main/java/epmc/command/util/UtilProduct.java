package epmc.command.util;

import java.util.ArrayList;
import java.util.Collection;

import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.Region;
import epmc.ptaxdta.pta.model.ClocksPTA;
import epmc.ptaxdta.pta.model.LocationPTA;
import epmc.ptaxdta.pta.model.LocationPTAProduct;
import epmc.ptaxdta.pta.model.ModelPTA;
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
		ClockConstraint zerocc = ClockConstraint.TOP(space);

		// TODO: find a solution to write simpler clock constraint
		for (int i = 0; i < result.clocks.clocknames.size(); i ++) {
			zerocc.setAnd(new AtomConstraint(i + 1, 0, 0, false));
		}
		
		for (LocationPTA initm : pta.initialLocations.getLocations()) {
			for (LocationPTA initp : dta.initialLocations.getLocations()) {
				LocationPTAProduct loc = new LocationPTAProduct(initm, initp, zerocc);
				loc.setModel(result);
				result.invariants.put(loc, zerocc);
				result.locations.addLocation(loc);
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

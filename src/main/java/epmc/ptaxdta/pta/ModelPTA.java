package epmc.ptaxdta.pta;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonValue;

import epmc.ptaxdta.RegionElement;
import epmc.jani.model.ModelJANI;

/**
 * 
 * @author liyi
 *
 */
public class ModelPTA implements ElementPTA {
	
	public ArrayList<LocationPTA> locations; 
	public ArrayList<String> actions;
	public ClocksPTA clocks;
	
	public HashMap<LocationPTA, RegionElement> invariants;
	public HashMap<LocationPTA, ArrayList<TransitionPTA>> transitions;

	public ModelJANI toJANI() {
		// TODO
		return null;
	}
	
	public boolean isDTA() {
		for (LocationPTA loc : locations) {
			
			HashMap<String, TransitionPTA> trmap = new HashMap<String, TransitionPTA>();
			for (TransitionPTA tr : transitions.get(loc.toString())) {
				if (!tr.isValid() || !tr.isSingleDestination()) return false;
				
				// check if the edge is unique, considering its action and guard
				if (trmap.containsKey(tr.action)) {
					// TODO override TransitionPTA.equals
					if (tr.equals(trmap.get(tr))) {
						// if this transition is exactly equal to its previous one, then we
						continue;
					} else return false;
				} else {
					trmap.put(tr.action, tr);
				}
			}
		}
		
		return true;
	}
	
	public static ModelPTA prod(ModelPTA pta, ModelPTA dta) {
		assert dta.isDTA();
		
		return null;
	}

	@Override
	public JsonValue toJani() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * this function should never be invoked
	 */
	public void setModel(ModelPTA model) {
		assert false;
		
	}

	@Override
	/**
	 * this function should never be invoked
	 */
	public ModelPTA getModel() {
		assert false;
		return null;
	}
}

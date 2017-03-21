package epmc.ptaxdta.pta;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonValue;

import epmc.jani.model.Edge;
import epmc.ptaxdta.RegionElement;

/**
 * a PTATransition describe a set of probabilistic transitions which
 * are labelled with the same action
 * @author liyi
 *
 */
public class TransitionPTA implements ElementPTA {

	// reference to the model where it belongs
	private ModelPTA model;
	
	public LocationPTA source;
	
	public String action;
	// guard condition of this transition
	public RegionElement guard;
	
	public ArrayList<Double> prob;
	public ArrayList<ClocksPTA> rstClock;
	public ArrayList<LocationPTA> target;
	
	public Edge toJANI() {
		// TODO convert the transition to JSON object first, and use Edge.Parse to
		// handle it
		return null;
	}
	
	/**
	 * A transition is called <i>valid</i> if and only if it satisfies <hr />
	 * <ul>
	 *   <li> lengths of prob, rstClock and target should be equal</li>
	 *   <li> sum of probabilities must be 1</li>
	 * </ul>
	 * @return a boolean value indicating if the transition is valid
	 */
	public boolean isValid() {
		// information of transitions should be consistent
		if (prob.size() != rstClock.size() || rstClock.size() != target.size()) {
			return false;
		}
		
		// sum of probabilities should be 1
		double sumprob = 1;
		for (Double pr : prob) sumprob += pr;
		if (sumprob != 1.0) return false;
		
		return true;
	}
	
	public boolean isSingleDestination() {
		return this.prob.size() == 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TransitionPTA)) return false;
		TransitionPTA tr = (TransitionPTA) obj;
		
		// FIXME currently we only need to compare two single-destination transitions
		assert this.isSingleDestination();
		assert tr.isSingleDestination();
		
		return
				this.source.equals(tr.source) &&
				this.action.equals(tr.action) &&
				this.prob.get(0) == tr.prob.get(0) &&
				this.rstClock.get(0).equals(tr.rstClock.get(0)) &&
				this.guard.equals(tr.guard) &&
				this.target.get(0).equals(tr.target.get(0));
	}

	public ModelPTA getModel() {
		return model;
	}

	public void setModel(ModelPTA model) {
		this.model = model;
	}

	@Override
	public JsonValue toJani() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

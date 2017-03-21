package epmc.ptaxdta.pta;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonValue;

import epmc.expression.Expression;
import epmc.jani.model.Action;
import epmc.jani.model.AssignmentSimple;
import epmc.jani.model.Assignments;
import epmc.jani.model.Destination;
import epmc.jani.model.Destinations;
import epmc.jani.model.Edge;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.Probability;
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
	
	public ArrayList<Double> prob = new ArrayList<Double>();
	public ArrayList<ClocksPTA> rstClock = new ArrayList<ClocksPTA>();
	public ArrayList<LocationPTA> target = new ArrayList<LocationPTA>();

	private String name;
	
	
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
	public JANINode toJani() {
		Edge edge = new Edge();
		
		edge.setLocation((Location) this.source.toJani());
		edge.setAction(new Action());
		edge.getAction().setName(this.action);
		
		
		edge.setDestinations(new Destinations());
		for (int i = 0; i < this.prob.size(); i ++) {
			Destination dest = new Destination();
			dest.setLocation((Location) this.target.get(i).toJani());
			dest.setProbability(new Probability());
			// dest.getProbability().setExp(new Expression());
			
			dest.setAssignments(new Assignments());
			for (String clk : this.rstClock.get(i).clocknames) {
				AssignmentSimple asn = new AssignmentSimple();
				// TODO add assignments
				// dest.getAssignments().add(asn);
			}
			edge.getDestinations().addDestination(dest);
		}
		
		return edge;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Used to create new branches in the certain transition
	 * @param prob probability of this branch
	 * @param rstClocks a set of clocks that needs to reset in this branch
	 * @param target target location of this branch
	 * @return the transition itself, to support following chain-style invocation
	 */
	public TransitionPTA addTarget(double prob, ClocksPTA rstClocks, LocationPTA target) {
		assert rstClocks != null;
		
		this.prob.add(prob);
		this.rstClock.add(rstClocks);
		this.target.add(target);
		return this;
	}
	
}

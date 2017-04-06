package epmc.ptaxdta.pta.model;

import java.util.ArrayList;
import java.util.List;

import epmc.error.EPMCException;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.TimeProgress;

public class LocationPTABasic implements LocationPTA {
	
	private String name = null;
	private ModelPTA model = null;
	
	public LocationPTABasic(String name) {
		assert name != null;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}


	public ModelPTA getModel() {
		return model;
	}

	public void setModel(ModelPTA model) {
		this.model = model;
	}

	@Override
	public JANINode toJani(ModelJANI modelref) throws EPMCException {
		Location loc = new Location();
		loc.setName(this.name);
		//TODO set invariant
		if (this.model.invariants.containsKey(this)) {
			TimeProgress inv = new TimeProgress();
			inv.setExp(this.model.invariants.get(this).toExpression());
			inv.setModel(modelref);
			loc.setTimeProgress(inv);
		}
		if (this.model.label.containsKey(this)) {
			// loc.setComment(this.model.label.get(this).contentString());
			//TODO put label in Jani
		}
		return loc;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o){
		LocationPTA l = (LocationPTA) o;
		return this.name.equals(l.getName());
	}

	@Override
	public ArrayList<String> getVariables() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("Location-Index-" + this.model.getName());
		return result;
	}

	@Override
	public ArrayList<Integer> getSerialized() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(this.model.locations.getLocations().indexOf(this));
		return result;
	}


}

package epmc.ptaxdta.pta.model;

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
	public JANINode toJani(ModelJANI modelref) {
		Location loc = new Location();
		loc.setName(this.name);
		//TODO set invariant
		TimeProgress inv = new TimeProgress();
//		inv.setExp(this.model.invariants.get(this).getExp());
		loc.setTimeProgress(inv);
		return loc;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}

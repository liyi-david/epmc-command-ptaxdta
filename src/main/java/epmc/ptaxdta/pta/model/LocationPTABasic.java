package epmc.ptaxdta.pta.model;

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
		
		return loc;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}

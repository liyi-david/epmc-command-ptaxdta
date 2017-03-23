package epmc.ptaxdta.pta;

import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.ModelJANI;

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
		return loc;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}

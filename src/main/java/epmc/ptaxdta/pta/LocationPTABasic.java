package epmc.ptaxdta.pta;

import javax.json.JsonValue;

import epmc.jani.model.Location;

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
	public JsonValue toJani() {
		// TODO Auto-generated method stub
		return null;
	}

}

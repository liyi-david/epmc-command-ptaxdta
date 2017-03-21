package epmc.ptaxdta.pta;

import javax.json.JsonValue;

import epmc.jani.model.JANINode;
import epmc.jani.model.Location;

public class LocationPTAProduct implements LocationPTA {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JANINode toJani() {
		// TODO Auto-generated method stub
		Location loc = new Location();
		return loc;
	}

	@Override
	public void setModel(ModelPTA model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ModelPTA getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

}

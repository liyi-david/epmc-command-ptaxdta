package epmc.ptaxdta.pta.model;

import java.util.ArrayList;
import java.util.HashMap;

import epmc.error.EPMCException;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.Locations;
import epmc.jani.model.ModelJANI;

public class LocationsPTA implements ElementPTA {
	
	private ArrayList<LocationPTA> locations = new ArrayList<LocationPTA>();
	
	private boolean ifInitialLocations = false;
	private ModelPTA model;
	
	private HashMap<String, LocationPTA> mapByName = new HashMap<String, LocationPTA>();

	public boolean isIfInitialLocations() {
		return ifInitialLocations;
	}

	public LocationsPTA(ModelPTA model) {
		this.setModel(model);
	}
	
	public LocationPTA addLocation(LocationPTA loc) {
		if (!locations.contains(loc)) {
			locations.add(loc);
			loc.setModel(this.model);
			this.mapByName.put(loc.getName(), loc);
		} else {
			// returns the reference of previously existing object
			return locations.get(locations.indexOf(loc));
		}
		return loc;
	}
	
	public LocationPTA getLocationByName(String name) {
		return this.mapByName.get(name);
	}
	
	/**
	 * the {@code getLocations} function is used to obtain an iterable object
	 * including the locations
	 * @return an arraylist containing the locations
	 */
	public ArrayList<LocationPTA> getLocations() {
		return locations;
	}

	@Override
	/**
	 * transfer a set of pta locations to a set of jani locations
	 */
	public JANINode toJani(ModelJANI modelref) throws EPMCException {
		// we assume that the toJani() function can be only called
		// if it is the total set of locations
		assert this.ifInitialLocations == false;
		
		Locations locations = new Locations();
		for (LocationPTA loc : this.locations) {
			locations.add((Location) loc.toJani(modelref));
		}
		
		return locations;
	}

	@Override
	public void setModel(ModelPTA model) {
		this.model = model;
	}

	@Override
	public ModelPTA getModel() {
		return model;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setInitFlag() {
		this.ifInitialLocations = true;
	}

}

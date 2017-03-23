package epmc.ptaxdta.pta.model;

import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.ModelJANI;
import epmc.ptaxdta.RegionElement;

public class LocationPTAProduct implements LocationPTA {
	
	private LocationPTA ptaloc;
	private LocationPTA taloc;
	private RegionElement region;
	
	public LocationPTAProduct(LocationPTA ptaloc, LocationPTA taloc, RegionElement region) {
		this.setPtaloc(ptaloc);
		this.setTaloc(taloc);
		this.setRegion(region);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JANINode toJani(ModelJANI modelref) {
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

	public LocationPTA getPtaloc() {
		return ptaloc;
	}

	public void setPtaloc(LocationPTA ptaloc) {
		this.ptaloc = ptaloc;
	}

	public LocationPTA getTaloc() {
		return taloc;
	}

	public void setTaloc(LocationPTA taloc) {
		this.taloc = taloc;
	}

	public RegionElement getRegion() {
		return region;
	}

	public void setRegion(RegionElement region) {
		this.region = region;
	}

}

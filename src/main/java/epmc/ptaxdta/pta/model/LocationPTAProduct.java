package epmc.ptaxdta.pta.model;

import epmc.error.EPMCException;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.TimeProgress;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.Region;

public class LocationPTAProduct implements LocationPTA {
	
	private LocationPTA ptaloc;
	private LocationPTA taloc;
	private ClockConstraint region;
	private ModelPTA model;
	
	public LocationPTAProduct(LocationPTA ptaloc, LocationPTA taloc, ClockConstraint region) {
		this.setPtaloc(ptaloc);
		this.setTaloc(taloc);
		this.setRegion(region);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "(" + ptaloc.getName() + "," + taloc.getName() + ")";
	}

	@Override
	public JANINode toJani(ModelJANI modelref) throws EPMCException {
		Location loc = new Location();
		loc.setName(this.getName());
		loc.setModel(modelref);
		
		// set invariant
		if (this.model.invariants.containsKey(this)) {
			TimeProgress inv = new TimeProgress();
			inv.setExp(this.model.invariants.get(this).toExpression());
			inv.setModel(modelref);
			loc.setTimeProgress(inv);
		}
		return loc;
	}

	@Override
	public void setModel(ModelPTA model) {
		this.model = model;
	}

	@Override
	public ModelPTA getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	@Override
	public void setName(String name) {
		assert false;
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

	public ClockConstraint getRegion() {
		return region;
	}

	public void setRegion(ClockConstraint region) {
		this.region = region;
	}

}

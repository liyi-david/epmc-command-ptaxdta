package epmc.ptaxdta.pta.model;

import java.util.ArrayList;

import epmc.error.EPMCException;
import epmc.jani.model.JANINode;
import epmc.jani.model.ModelJANI;

public class ClocksPTA implements ElementPTA {
	
	public ArrayList<String> clocknames = new ArrayList<String>();

	public ClocksPTA(ArrayList<String> clocknames) {
		this.clocknames = clocknames;
	}

	public ClocksPTA(String ...args) {
		for (String identifier : args) {
			clocknames.add(identifier);
		}
	}

	@Override
	public JANINode toJani(ModelJANI modelref) throws EPMCException {
		// TODO Auto-generated method stub
		return null;
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
	public boolean equals(Object obj) {
		if (!(obj instanceof ClocksPTA)) return false;
		return clocknames.equals(((ClocksPTA)obj).clocknames);
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

}

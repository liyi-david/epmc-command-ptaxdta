package epmc.ptaxdta.pta;

import java.util.ArrayList;

import javax.json.JsonValue;

import epmc.jani.model.JANINode;

public class ClocksPTA implements ElementPTA {
	
	public ArrayList<String> clocknames = new ArrayList<String>();
	
	public ClocksPTA(String ...args) {
		for (String identifier : args) {
			clocknames.add(identifier);
		}
	}

	@Override
	public JANINode toJani() {
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

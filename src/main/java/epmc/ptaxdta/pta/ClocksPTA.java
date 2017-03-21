package epmc.ptaxdta.pta;

import java.util.ArrayList;

import javax.json.JsonValue;

public class ClocksPTA implements ElementPTA {
	
	public ArrayList<String> clocknames = new ArrayList<String>();

	@Override
	public JsonValue toJani() {
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

}

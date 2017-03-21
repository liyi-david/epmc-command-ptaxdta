package epmc.ptaxdta.pta;

import javax.json.JsonValue;

public interface ElementPTA {
	
	public JsonValue toJani();
	
	public void setModel(ModelPTA model);
	
	public ModelPTA getModel();

}

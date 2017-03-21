package epmc.ptaxdta.pta;

import epmc.jani.model.JANINode;

public interface ElementPTA {
	
	public JANINode toJani();
	
	public void setModel(ModelPTA model);
	
	public ModelPTA getModel();
	
	public void setName(String name);
	
	public String getName();

}

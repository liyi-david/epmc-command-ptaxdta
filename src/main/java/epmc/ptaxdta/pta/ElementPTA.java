package epmc.ptaxdta.pta;

import epmc.error.EPMCException;
import epmc.jani.model.JANINode;
import epmc.jani.model.ModelJANI;

public interface ElementPTA {
	
	public JANINode toJani(ModelJANI modelref) throws EPMCException;
	
	public void setModel(ModelPTA model);
	
	public ModelPTA getModel();
	
	public void setName(String name);
	
	public String getName();

}

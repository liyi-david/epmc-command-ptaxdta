package epmc.ptaxdta;

import static org.junit.Assert.*;

import org.junit.Test;

import epmc.ptaxdta.pta.*;

public class Pta2Jani {

	@Test
	public void test() {
		ModelPTA pta = new ModelPTA();
		pta.actions.add("i");
		
		pta.locations.add(new LocationPTABasic("l0"));
		pta.locations.add(new LocationPTABasic("l1"));
		pta.locations.add(new LocationPTABasic("l2"));
		
		System.out.print(pta.toJani().toString());
	}

}

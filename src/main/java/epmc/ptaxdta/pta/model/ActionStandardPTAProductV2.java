package epmc.ptaxdta.pta.model;

import java.util.HashMap;

import epmc.ptaxdta.ClockConstraint;

public class ActionStandardPTAProductV2 implements ActionPTA {

	private ActionPTA actPta;
	private HashMap<ActionPTA, ClockConstraint> actTq;
	
	public ActionStandardPTAProductV2 (ActionPTA apta, HashMap<ActionPTA, ClockConstraint> atq) {
		actPta = apta;
		actTq = atq;
	}
	
	@Override
	public String contentString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(ActionPTA a) {
		// TODO Auto-generated method stub
		return false;
	}

}

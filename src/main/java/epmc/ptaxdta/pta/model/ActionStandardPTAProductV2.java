package epmc.ptaxdta.pta.model;

import java.util.HashMap;
import java.util.Set;

import epmc.ptaxdta.ClockConstraint;

public class ActionStandardPTAProductV2 implements ActionPTA {

	private ActionPTA actPta;
	private HashMap<ActionPTA, ClockConstraint> actTq;

	public ActionPTA getActPta() {
		return actPta;
	}

	public HashMap<ActionPTA, ClockConstraint> getActTq() {
		return actTq;
	}

	public ActionStandardPTAProductV2 (ActionPTA apta, HashMap<ActionPTA, ClockConstraint> atq) {
		actPta = apta;
		actTq = atq;
	}
	
	@Override
	public String contentString() {
		return "(" + this.actPta.contentString() +","+ this.actTq.toString() + ")";
	}

	@Override
	public boolean equals(ActionPTA a) {
		ActionStandardPTAProductV2 rhs = (ActionStandardPTAProductV2) a;
		Set<ActionPTA> K1 = this.actTq.keySet();
		Set<ActionPTA> K2 =  rhs.actTq.keySet();
		if (K1.size() != K2.size()) return false;

		for (ActionPTA k : K1) {
			ClockConstraint g1 = this.actTq.get(k);
			ClockConstraint g2 =  rhs.actTq.get(k);
			if ((g1 == null) || (g2 == null)){
				return false;
			}
			if (!g1.getFed().eq(g2.getFed())){
				return false;
			}
		}
		return this.actPta.equals(rhs.getActPta());
	}

}

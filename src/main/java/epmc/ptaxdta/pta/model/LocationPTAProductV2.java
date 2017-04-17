package epmc.ptaxdta.pta.model;

import java.util.ArrayList;

import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.expression.standard.ExpressionOperator;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.TimeProgress;
import epmc.value.OperatorAnd;

public class LocationPTAProductV2 implements LocationPTA {
	
	private LocationPTA PTAloc;
	private LocationPTA DTAloc;
	private ModelPTA model;
	
	public LocationPTAProductV2(LocationPTA PTAloc, LocationPTA DTAloc) {
		this.setPTAloc(PTAloc);
		this.setDTAloc(DTAloc);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "(" + PTAloc.getName() + "," + DTAloc.getName() + ")";
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

	public LocationPTA getPTAloc() {
		return PTAloc;
	}

	public void setPTAloc(LocationPTA PTAloc) {
		this.PTAloc = PTAloc;
	}

	public LocationPTA getDTAloc() {
		return DTAloc;
	}

	public void setDTAloc(LocationPTA DTAloc) {
		this.DTAloc = DTAloc;
	}

	@Override
	public boolean equals(Object l){
		if (!(l instanceof LocationPTAProductV2)){
			return false;
		}
		LocationPTAProductV2 state = (LocationPTAProductV2) l;
		
		return this.PTAloc.equals(state.getPTAloc()) &&
			   this.DTAloc.equals(state.getDTAloc());
	}
	@Override
	public String toString(){
		return "(" + this.PTAloc.getName() + ", " + this.DTAloc.getName() + /*", " + this.region.toStr() +*/ ")";
	}

	@Override
	public ArrayList<String> getVariables() {
		ArrayList<String> varspta = this.PTAloc.getVariables();
		ArrayList<String> varsdta = this.DTAloc.getVariables();
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(varspta);
		result.addAll(varsdta);
		return result;
	}

	@Override
	public ArrayList<Integer> getSerialized() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> valspta = this.PTAloc.getSerialized();
		ArrayList<Integer> valsdta = this.DTAloc.getSerialized();
		result.addAll(valspta);
		result.addAll(valsdta);
		return result;
	}
	
	@Override
	public ArrayList<Integer> getScopeSizes() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> valspta = this.PTAloc.getScopeSizes();
		ArrayList<Integer> valsdta = this.DTAloc.getScopeSizes();
		result.addAll(valspta);
		result.addAll(valsdta);
		return result;
	}

	@Override
	public Expression getInvariant() throws EPMCException {
//		Expression result = new ExpressionOperator.Builder()
//				.setOperator(this.model.getContextValue().getOperator(OperatorAnd.IDENTIFIER))
//				.setOperands(this.PTAloc.getInvariant(), this.DTAloc.getInvariant())
//				.build();
//		return result;
		return this.PTAloc.getInvariant();
	}
}

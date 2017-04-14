package epmc.ptaxdta.pta.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import epmc.jani.model.*;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.UtilDBM;
import epmc.time.JANITypeClock;
import epmc.value.ContextValue;
import epmc.value.OperatorAnd;
import epmc.value.OperatorEq;
import epmc.value.OperatorLe;
import epmc.value.OperatorOr;
import epmc.value.TypeBoolean;
import epmc.value.TypeInteger;
import epmc.value.TypeReal;
import epmc.value.UtilValue;
import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.expression.standard.ExpressionIdentifierStandard;
import epmc.expression.standard.ExpressionLiteral;
import epmc.expression.standard.ExpressionOperator;
import epmc.graph.LowLevel;
import epmc.graph.Semantics;
import epmc.jani.model.component.ComponentAutomaton;
import epmc.jani.model.component.ComponentSynchronisationVectors;
import epmc.jani.model.component.SynchronisationVectorElement;
import epmc.jani.model.type.JANITypeBounded;
import epmc.jani.model.type.JANITypeInt;
import epmc.modelchecker.Engine;
import epmc.modelchecker.Model;
import epmc.modelchecker.Properties;
import epmc.prism.exporter.processor.JANI2PRISMConverter;

/**
 * 
 * @author liyi
 *
 */
public class ModelPTA implements ElementPTA, Model {
	
	private String name;
	private ContextValue contextValue;
	public LocationsPTA locations = new LocationsPTA(this);
	public ArrayList<ActionPTA> actions = new ArrayList<ActionPTA>();
	public ClocksPTA clocks = new ClocksPTA();
	public APSet AP = null;

	public LocationsPTA initialLocations = new LocationsPTA(this);
	
	public HashMap<LocationPTA, ClockConstraint> invariants =
			new HashMap<LocationPTA, ClockConstraint>();
	public HashMap<LocationPTA, LabelPTA> label =
			new HashMap<LocationPTA, LabelPTA>();
	public HashMap<LocationPTA, ArrayList<TransitionPTA>> transitions =
			new HashMap<LocationPTA, ArrayList<TransitionPTA>>();

	private ClockSpace space;

	public void setSpace(ClockSpace space) {
		this.space = space;
	}

	public ClockSpace getSpace() {
		return space;
	}

	public ModelPTA(String name) {
		this.name = name;
		
		// initialize model references
		this.locations.setModel(this);
		this.initialLocations.setModel(this);
		
		this.initialLocations.setInitFlag();
	}
	
	public boolean isDTA() {
		for (LocationPTA loc : locations.getLocations()) {
			
			HashMap<String, TransitionPTA> trmap = new HashMap<String, TransitionPTA>();
			for (TransitionPTA tr : transitions.get(loc)) {
				if (!tr.isValid() || !tr.isSingleDestination()) return false;
				
				// check if the edge is unique, considering its action and guard
				if (trmap.containsKey(tr.action.contentString())) {
					// TODO override TransitionPTA.equals
					if (tr.equals(trmap.get(tr))) {
						// if this transition is exactly equal to its previous one, then we
						continue;
					} else return false;
				} else {
					trmap.put(tr.action.contentString(), tr);
				}
			}
		}
		
		return true;
	}

	@Override
	public JANINode toJani(ModelJANI modelref) {
		assert modelref == null;
		
		ModelJANI jani = new ModelJANI();
		jani.setContext(this.contextValue);
		
//		ClockConstraint.context = this.contextValue;
//		ClockConstraint.model = jani;
		
		try {
			jani.setSemantics("pta");
			jani.setName(this.name);
			
			ComponentAutomaton system = new ComponentAutomaton();
			system.setModel(jani);
			
			Automaton automaton = new Automaton();
			automaton.setModel(jani);
			automaton.setName("main");
			system.setAutomaton(automaton);
			
			Automata automata = new Automata();
			automata.setModel(jani);
			automata.addAutomaton(automaton);
			jani.setAutomata(automata);

			// convert locations
			automaton.setLocations((Locations) locations.toJani(jani));
			automaton.setInitialLocations(new HashSet<Location>());
			
			// convert initial locations
			for (LocationPTA loc : initialLocations.getLocations()) {
				automaton.getInitialLocations().add((Location) loc.toJani(jani));
			}
			
			// add clocks
			Variables vars = new Variables();
			
			for (String clk : this.clocks.clocknames) {
				Variable var = new Variable();
				var.setModel(jani);
				var.setType(new JANITypeClock());
				var.setName(clk);
				var.setInitial(
						new ExpressionLiteral.Builder()
						.setValue(
								UtilValue.newValue(
										TypeInteger.get(this.contextValue),
										0
										)
						)
						.build()
						);
				vars.addVariable(var);
			}
			
			automaton.setVariables(vars);
			jani.setGlobalVariables(new Variables());

			// convert edges
			automaton.setEdges(new Edges());
			
			for (ArrayList<TransitionPTA> trs : this.transitions.values()) {
				
				for (TransitionPTA tr : trs) {
					automaton.getEdges().add((Edge) tr.toJani(jani));
				}
				
			}
			
			system.setModel(jani);
			jani.setSystem(system);
		} catch (EPMCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jani;
	}
	
	/**
	 * 
	 * @param modelref
	 * @return a jani model with only one location
	 */
	public JANINode toSingleJani(ModelJANI modelref) {
		assert modelref == null;
		
		ModelJANI jani = new ModelJANI();
		jani.setContext(this.contextValue);
		jani.setModelConstants(new Constants());
		jani.setActions(new Actions());
		
		
		try {
			jani.setSemantics("pta");
			jani.setName(this.name);
			
			ComponentSynchronisationVectors system = new ComponentSynchronisationVectors();
			system.setModel(jani);
			
			Automaton automaton = new Automaton();
			automaton.setModel(jani);
			automaton.setName("main");
			system.setElements(new ArrayList<>());
			system.setSyncs(new ArrayList<>());
			
			Automata automata = new Automata();
			automata.setModel(jani);
			automata.addAutomaton(automaton);
			jani.setAutomata(automata);

			Locations singleLocSet = new Locations();
			Location loc = new Location();
			loc.setModel(jani);
			loc.setName("single location (converted)");
			
			singleLocSet.add(loc);
			
			automaton.setLocations(singleLocSet);
			automaton.setInitialLocations(new HashSet<Location>());
			automaton.getInitialLocations().add(loc);
			
			// invariants
			
			Expression inv = new ExpressionLiteral.Builder()
					.setValue(
							UtilValue.newValue(
									TypeBoolean.get(this.contextValue),
									"false"
									)
					)
					.build();
			
			for (LocationPTA location : this.locations.getLocations()) {
				inv = new ExpressionOperator.Builder()
						.setOperator(this.contextValue.getOperator(OperatorOr.IDENTIFIER))
						.setOperands(inv, location.getInvariant()) 
						.build();
						
			}
			
			loc.setTimeProgress(new TimeProgress());
			loc.getTimeProgress().setExp(inv);
			loc.getTimeProgress().setModel(jani);

			
			// add clocks
			Variables vars = new Variables();
			
			for (String clk : this.clocks.clocknames) {
				Variable var = new Variable();
				var.setModel(jani);
				var.setType(new JANITypeClock());
				var.setName(clk);
				var.setInitial(
						new ExpressionLiteral.Builder()
						.setValue(
								UtilValue.newValue(
										TypeInteger.get(this.contextValue),
										0
										)
						)
						.build()
						);
				vars.addVariable(var);
			}
			
			// add location-relative variables
			// this function only handles the automata with only one initial location
			// otherwise we may need a set of internal transitions to randomly initialize it
			assert this.initialLocations.getLocations().size() == 1;
			
			ArrayList<Variable> locIndexes = new ArrayList<Variable> ();
			ArrayList<String> varnames = this.initialLocations.getLocations().get(0).getVariables();
			ArrayList<Integer> defaultvals = this.initialLocations.getLocations().get(0).getSerialized();
			ArrayList<Integer> scopeSizes = this.initialLocations.getLocations().get(0).getScopeSizes();
			
			assert varnames.size() == defaultvals.size();
			
			for (int i = 0; i < varnames.size(); i ++) {
				Variable var = new Variable();
				var.setModel(jani);
				JANITypeBounded type = new JANITypeBounded();
				
				Expression lbound = new ExpressionLiteral.Builder()
						.setValue(
								UtilValue.newValue(
										TypeInteger.get(this.contextValue),
										0
										)
						)
						.build();
				
				Expression ubound = new ExpressionLiteral.Builder()
						.setValue(
								UtilValue.newValue(
										TypeInteger.get(this.contextValue),
										scopeSizes.get(i) - 1
										)
						)
						.build();
				
				type.setLowerBound(lbound);
				type.setUpperBound(ubound);
				type.setModel(jani);
				
				type.setContextValue(this.contextValue);
				
				var.setType(type);
				var.setName(varnames.get(i));
				var.setInitial(
					new ExpressionLiteral.Builder()
					.setValue(
							UtilValue.newValue(
									TypeInteger.get(this.contextValue),
									defaultvals.get(i)
									)
					)
					.build()
					);
				
				vars.addVariable(var);
				locIndexes.add(var);
			}
			
			// automaton.setVariables(vars);
			jani.setGlobalVariables(vars);

			// convert edges
			automaton.setEdges(new Edges());
			int numAct = 0;
			HashMap<String, Action> formula2action = new HashMap<String, Action>();
			
			
			for (ArrayList<TransitionPTA> trs : this.transitions.values()) {
				
				for (TransitionPTA tr : trs) {
					Edge edge = (Edge) tr.toJani(jani);
					
					// if this action is used before
					if (formula2action.containsKey(edge.getAction().getName())) {
						edge.setAction(formula2action.get(edge.getAction().getName()));
					} else {
						// register this action
						Action act = new Action();
						act.setComment(edge.getAction().getName());
						act.setName("act" + formula2action.size());
						act.setModel(jani);
						formula2action.put(act.getComment(), act);
						edge.setAction(act);
					}
					
					if (!jani.getActions().containsKey(edge.getAction().getName())) {
						jani.getActions().addAction(edge.getAction());
					} else {
						edge.setAction(jani.getActions().get(edge.getAction().getName()));
					}
					
					// modify the guard, so that it can make sure it is only fired when
					// the state satisfies the location constraint i.e. the guard must keeps its
					// source location
					
					ArrayList<Integer> sourcevals = this.locations.getLocationByName(edge.getLocation().getName()).getSerialized();
					for (int i = 0; i < sourcevals.size(); i ++) {
						Expression varid = new ExpressionIdentifierStandard.Builder()
								.setName(locIndexes.get(i).getName())
								.build();
						
						Expression value = new ExpressionLiteral.Builder()
								.setValue(
										UtilValue.newValue(
												TypeInteger.get(this.contextValue),
												sourcevals.get(i)
												)
								)
								.build();
						
						Expression expeq = new ExpressionOperator.Builder()
								.setOperator(this.contextValue.getOperator(OperatorEq.IDENTIFIER))
								.setOperands(varid,value) 
								.build();
						
						Expression finalexp = new ExpressionOperator.Builder()
								.setOperator(this.contextValue.getOperator(OperatorAnd.IDENTIFIER))
								.setOperands(edge.getGuard().getExp(), expeq) 
								.build();
						
						edge.getGuard().setExp(finalexp);
					}
					
					edge.setLocation(loc);
					for (Destination dest : edge.getDestinations()) {
						
						// modify the assignments, redirecting to the state that corresponds
						// to its target location through reassignment of the location-index
						// variables
						
						ArrayList<Integer> vals = this.locations.getLocationByName(dest.getLocation().getName()).getSerialized();
						for (int i = 0; i < vals.size(); i ++) {
							
							AssignmentSimple asn = new AssignmentSimple();
							asn.setRef(locIndexes.get(i));
							asn.setValue(
									new ExpressionLiteral.Builder()
					                .setValue(UtilValue.newValue(TypeInteger.get(this.contextValue), vals.get(i).toString()))
					                .build()
									);
							asn.setModel(jani);
							
							dest.getAssignments().add(asn);
						}
						dest.setLocation(loc);
					}
					
					automaton.getEdges().add(edge);
				}
				
			}
			
			system.setModel(jani);
			jani.setSystem(system);
			
		} catch (EPMCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jani;
	}

//	public String toPrism() throws EPMCException {
//		JANI2PRISMConverter converter = new JANI2PRISMConverter((ModelJANI) this.toSingleJani(null));
//		StringBuilder builderModel = converter.convertModel();
//		return builderModel.toString();
//	}
	@Override
	/**
	 * this function should never be invoked
	 */
	public void setModel(ModelPTA model) {
		assert false;
		
	}

	@Override
	/**
	 * this function should never be invoked
	 */
	public ModelPTA getModel() {
		assert false;
		return null;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public ContextValue getContextValue() {
		return contextValue;
	}

	public void setContextValue(ContextValue contextValue) {
		this.contextValue = contextValue;
	}

	public TransitionPTA addConnectionFrom(LocationPTA source, ActionPTA action, ClockConstraint guard) {
		
		TransitionPTA newtr = new TransitionPTA();
		newtr.setModel(this);
		newtr.source = source;
		
		newtr.guard = guard;
		newtr.action = action;
		
		if (!this.transitions.containsKey(source)) {
			this.transitions.put(source, new ArrayList<TransitionPTA>());
		}
		
		this.transitions.get(source).add(newtr);
		return newtr;
		
	}
	public TransitionPTA addBranchingFrom(LocationPTA source, ActionPTA action, ClockConstraint guard) {


		if (!this.transitions.containsKey(source)) {
			this.transitions.put(source, new ArrayList<TransitionPTA>());
		}

		ArrayList<TransitionPTA> E = this.transitions.get(source);
		for (TransitionPTA e : E){
			if (e.action.equals(action)){ // Do not need to check guard.equals()
				return e;
			}
		}

		TransitionPTA newtr = new TransitionPTA();
		newtr.setModel(this);
		newtr.source = source;

		newtr.guard = guard;
		newtr.action = action;
		this.transitions.get(source).add(newtr);

		return newtr;
	}
	
	public void setFinalLocation(LocationPTA loc) {
		
		for (ActionPTA act : this.actions) {
			ClockConstraint ccRemain = ClockConstraint.TOP(space);
			if (this.transitions.containsKey(loc)) {
				for (TransitionPTA tran : this.transitions.get(loc)) {
					if (tran.action.equals(act)) {
						ccRemain.setFed(ccRemain.getFed().minusOp(tran.guard.getFed()));
					}
				}
			}
			
			if (!ccRemain.toString().equals("false")) {
				String [] strCcRemains = ccRemain.toUDBMString().split("\\|\\|");
				for (String subCcRemains : strCcRemains) {
					this.addConnectionFrom(loc, act, UtilDBM.UDBMString2CC(subCcRemains, this.space))
						.addTarget(1, new ClocksPTA(), loc);
				}
			}
			
		}
	}

	public void addTrapLocation() {
		/* trap location is used only in a DTA, i.e., a property instead of the model
		 * itself. further more, it's important not to make any further modification
		 * after invocation of this method
		 */
		// FIXME
		// assert this.isDTA();
		
		LocationPTA traploc = new LocationPTABasic("TRAP");
		traploc.setModel(this);
		this.invariants.put(traploc, ClockConstraint.TOP(this.space));
		
		/*
		 * for all location *l* and action *a*, we want to compute the maximum condition formula
		 * *g_trap* that is disjoint with any other guards with source location *l* and action *a*,
		 * and target *g_trap* to the trap location 
		 */
		for (LocationPTA loc : this.locations.getLocations()) {
			for (ActionPTA act : this.actions) {
				ClockConstraint ccRemain = ClockConstraint.TOP(space);
				if (this.transitions.containsKey(loc)) {
					for (TransitionPTA tran : this.transitions.get(loc)) {
						if (tran.action.equals(act)) {
							ccRemain.setFed(ccRemain.getFed().minusOp(tran.guard.getFed()));
						}
					}
				}
				
				if (!ccRemain.toString().equals("false")) {
					String [] strCcRemains = ccRemain.toUDBMString().split("\\|\\|");
					for (String subCcRemains : strCcRemains) {
						this.addConnectionFrom(loc, act, UtilDBM.UDBMString2CC(subCcRemains, this.space))
							.addTarget(1, new ClocksPTA(), traploc);
					}
				}
			}
		}
		this.locations.addLocation(traploc);
		this.transitions.put(traploc, new ArrayList<TransitionPTA>());

		for (ActionPTA act : this.actions) {
			this.addConnectionFrom(traploc, act, ClockConstraint.TOP(this.space)).addTarget(1, new ClocksPTA(), traploc);
		}

	}
	
	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(ContextValue context) {
		this.contextValue = context;	
	}

	@Override
	public void read(InputStream... inputs) throws EPMCException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Semantics getSemantics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LowLevel newLowLevel(Engine engine, Set<Object> graphProperties, Set<Object> nodeProperties,
			Set<Object> edgeProperties) throws EPMCException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getPropertyList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addLabels(String... labels) {
		ArrayList<ArrayList<String>> lbls = new ArrayList<ArrayList<String>>();
		lbls.add(new ArrayList<String>());
		for (String label : labels) {
			ArrayList<ArrayList<String>> lblsn = new ArrayList<ArrayList<String>>();
			for (ArrayList<String> lbl : lbls) {
				ArrayList<String> newlbl = (ArrayList<String>) lbl.clone();
				newlbl.add(label);
				lblsn.add(newlbl);
			}
			lbls.addAll(lblsn);
		}
		
		String [] template = {};
		for (ArrayList<String> lbl : lbls) {
			this.actions.add(new LabelPTA(lbl.toArray(template)));
		}
	}

	public APSet getAP() {
		return AP;
	}

	public void setAP(APSet AP) {
		this.AP = AP;
	}

	public String toPrism() throws EPMCException {
		ModelJANI singlejani = (ModelJANI) this.toSingleJani(null);
		JANI2PRISMConverter converter = new JANI2PRISMConverter(singlejani);
    	StringBuilder modelSB = converter.convertModel();
		return modelSB.toString();
	}
}

package epmc.ptaxdta.pta.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import epmc.jani.model.*;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.time.JANITypeClock;
import epmc.value.ContextValue;
import epmc.value.OperatorAnd;
import epmc.value.OperatorEq;
import epmc.value.OperatorLe;
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
import epmc.jani.model.type.JANITypeInt;
import epmc.modelchecker.Engine;
import epmc.modelchecker.Model;
import epmc.modelchecker.Properties;

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
			for (TransitionPTA tr : transitions.get(loc.toString())) {
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
				vars.addVariable(var);
			}
			
			// TODO somehow clocks are not written into the JANI model
			automaton.setVariables(vars);

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

			Locations singleLocSet = new Locations();
			Location loc = new Location();
			loc.setModel(jani);
			loc.setName("single location (converted)");
			singleLocSet.add(loc);
			
			automaton.setLocations(singleLocSet);
			automaton.setInitialLocations(new HashSet<Location>());
			automaton.getInitialLocations().add(loc);
			
			// add clocks
			Variables vars = new Variables();
			
			for (String clk : this.clocks.clocknames) {
				Variable var = new Variable();
				var.setModel(jani);
				var.setType(new JANITypeClock());
				var.setName(clk);
				vars.addVariable(var);
			}
			
			// add location-relative variables
			// this function only handles the automata with only one initial location
			// otherwise we may need a set of internal transitions to randomly initialize it
			assert this.initialLocations.getLocations().size() == 1;
			
			ArrayList<Variable> locIndexes = new ArrayList<Variable> ();
			ArrayList<String> varnames = this.initialLocations.getLocations().get(0).getVariables();
			ArrayList<Integer> defaultvals = this.initialLocations.getLocations().get(0).getSerialized();
			
			assert varnames.size() == defaultvals.size();
			
			for (int i = 0; i < varnames.size(); i ++) {
				Variable var = new Variable();
				var.setModel(jani);
				var.setType(new JANITypeInt());
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
				// TODO: use initial value to represent initial location
				
				vars.addVariable(var);
				locIndexes.add(var);
			}
			
			automaton.setVariables(vars);

			// convert edges
			automaton.setEdges(new Edges());
			
			for (ArrayList<TransitionPTA> trs : this.transitions.values()) {
				
				for (TransitionPTA tr : trs) {
					Edge edge = (Edge) tr.toJani(jani);
					
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
		
		// TODO implement guards
		newtr.guard = guard;
		newtr.action = action;
		
		if (!this.transitions.containsKey(source)) {
			this.transitions.put(source, new ArrayList<TransitionPTA>());
		}
		
		this.transitions.get(source).add(newtr);
		
		return newtr;
		
	}

	public void addTrapLocation() {
		/* trap location is used only in a DTA, i.e., a property instead of the model
		 * itself. further more, it's important not to make any further modification
		 * after invocation of this method
		 */
		assert this.isDTA();
		
		LocationPTA traploc = new LocationPTABasic("TRAP");
		traploc.setModel(this);
		
		for (LocationPTA loc : this.locations.getLocations()) {
			for (ActionPTA act : this.actions) {
				ClockConstraint ccRemain = ClockConstraint.TOP(space);
				for (TransitionPTA tran : this.transitions.get(loc)) {
					if (tran.action.equals(act)) {
						// TODO:
					}
				}
			}
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
}

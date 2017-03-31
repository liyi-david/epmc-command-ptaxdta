package epmc.ptaxdta.pta.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import epmc.jani.model.*;
import epmc.ptaxdta.ClockConstraint;
import epmc.time.JANITypeClock;
import epmc.value.ContextValue;
import epmc.error.EPMCException;
import epmc.graph.LowLevel;
import epmc.graph.Semantics;
import epmc.jani.model.component.ComponentAutomaton;
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
	public ArrayList<String> actions = new ArrayList<String>();
	public ClocksPTA clocks = new ClocksPTA();
	
	public LocationsPTA initialLocations = new LocationsPTA(this);
	
	public HashMap<LocationPTA, ClockConstraint> invariants =
			new HashMap<LocationPTA, ClockConstraint>();
	public HashMap<LocationPTA, ArrayList<TransitionPTA>> transitions =
			new HashMap<LocationPTA, ArrayList<TransitionPTA>>();

	
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

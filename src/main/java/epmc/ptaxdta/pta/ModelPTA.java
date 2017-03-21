package epmc.ptaxdta.pta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.json.JsonValue;

import epmc.ptaxdta.RegionElement;
import epmc.value.ContextValue;
import epmc.error.EPMCException;
import epmc.jani.model.Automata;
import epmc.jani.model.Automaton;
import epmc.jani.model.Edge;
import epmc.jani.model.Edges;
import epmc.jani.model.JANINode;
import epmc.jani.model.Location;
import epmc.jani.model.Locations;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.component.Component;
import epmc.jani.model.component.ComponentAutomaton;

/**
 * 
 * @author liyi
 *
 */
public class ModelPTA implements ElementPTA {
	
	private String name;
	private ContextValue contextValue;
	
	public LocationsPTA locations = new LocationsPTA(); 
	public ArrayList<String> actions = new ArrayList<String>();
	public ClocksPTA clocks = new ClocksPTA();
	
	public LocationsPTA initialLocations = new LocationsPTA();
	
	public HashMap<LocationPTA, RegionElement> invariants =
			new HashMap<LocationPTA, RegionElement>();
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
				if (trmap.containsKey(tr.action)) {
					// TODO override TransitionPTA.equals
					if (tr.equals(trmap.get(tr))) {
						// if this transition is exactly equal to its previous one, then we
						continue;
					} else return false;
				} else {
					trmap.put(tr.action, tr);
				}
			}
		}
		
		return true;
	}
	
	public static ModelPTA prod(ModelPTA pta, ModelPTA dta) {
		assert dta.isDTA();
		
		return null;
	}

	@Override
	public JANINode toJani() {
		ModelJANI jani = new ModelJANI();
		jani.setContext(this.contextValue);
		try {
			jani.setSemantics("pta");
			jani.setName(this.name);
			
			ComponentAutomaton system = new ComponentAutomaton();
			Automaton automaton = new Automaton();
			automaton.setModel(jani);
			automaton.setName("main");
			system.setAutomaton(automaton);
			
			Automata automata = new Automata();
			automata.addAutomaton(automaton);
			jani.setAutomata(automata);
			
			// convert locations
			automaton.setLocations((Locations) locations.toJani());
			automaton.setInitialLocations(new HashSet<Location>());
			
			// convert initial locations
			for (LocationPTA loc : initialLocations.getLocations()) {
				automaton.getInitialLocations().add((Location) loc.toJani());
			}
			
			// TODO how invariants are handled?
			
			// convert edges
			automaton.setEdges(new Edges());
			
			for (ArrayList<TransitionPTA> trs : this.transitions.values()) {
				
				for (TransitionPTA tr : trs) {
					automaton.getEdges().add((Edge) tr.toJani());
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

	public TransitionPTA addConnectionFrom(LocationPTA source, String action, String guard) {
		
		TransitionPTA newtr = new TransitionPTA();
		newtr.source = source;
		// TODO implement guards
		newtr.guard = null;
		newtr.action = action;
		
		if (!this.transitions.containsKey(source)) {
			this.transitions.put(source, new ArrayList<TransitionPTA>());
		}
		
		this.transitions.get(source).add(newtr);
		
		return newtr;
		
	}
}

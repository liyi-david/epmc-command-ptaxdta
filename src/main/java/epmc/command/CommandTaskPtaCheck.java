package epmc.command;


import epmc.error.EPMCException;
import epmc.messages.OptionsMessages;
import epmc.modelchecker.CommandTask;
import epmc.modelchecker.Log;
import epmc.modelchecker.Model;
import epmc.modelchecker.ModelChecker;
import epmc.options.Options;
import epmc.ptaxdta.pta.model.ClocksPTA;
import epmc.ptaxdta.pta.model.LocationPTA;
import epmc.ptaxdta.pta.model.LocationPTABasic;
import epmc.ptaxdta.pta.model.ModelPTA;
import epmc.value.ContextValue;

public class CommandTaskPtaCheck implements CommandTask {
    public final static String IDENTIFIER = "ptacheck";
    private ModelChecker modelChecker;

    private Options options;
    private Log log;
    private ContextValue contextValue;
    
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
    
    private void initialize() {
    	this.options = modelChecker.getModel().getContextValue().getOptions();
        this.log = this.options.get(OptionsMessages.LOG);
        this.setContextValue(modelChecker.getModel().getContextValue());
    }

    @Override
    public void setModelChecker(ModelChecker modelChecker) {
        this.modelChecker = modelChecker;
    }

    @Override
    public void executeInServer() {
    	initialize();
    	try {
			check();
		} catch (EPMCException e) {
			log.send(e);
		}
        
    }
    
    public void check() throws EPMCException {
        // testing code
        Model model = modelChecker.getModel();        
        ModelPTA pta = new ModelPTA("task-complete");
        
        testCC(model);
        
        pta.setContextValue(model.getContextValue());
		pta.actions.add("i");
		
		pta.clocks.clocknames.add("x");
		
		LocationPTA l0 = pta.initialLocations.addLocation(
				pta.locations.addLocation(new LocationPTABasic("l0"))
				);
		LocationPTA l1 = pta.locations.addLocation(new LocationPTABasic("l1"));
		LocationPTA l2 = pta.locations.addLocation(new LocationPTABasic("l2"));
		
		// addConnection source, guard, probability, resetClocks, target
		
		pta.addConnectionFrom(l0, "i", "")
			.addTarget(0.1, new ClocksPTA("x"), l0)
			.addTarget(0.9, new ClocksPTA("x"), l1);
		
		pta.addConnectionFrom(l1, "i", "")
			.addTarget(0.2, new ClocksPTA("x"), l1)
			.addTarget(0.8, new ClocksPTA("x"), l2);
		
		System.out.print(pta.toJani(null).toString());
		
    }
    
    public void testCC(Model model) {
    	// TODO
    }

	public ContextValue getContextValue() {
		return contextValue;
	}

	public void setContextValue(ContextValue contextValue) {
		this.contextValue = contextValue;
	}
    
}

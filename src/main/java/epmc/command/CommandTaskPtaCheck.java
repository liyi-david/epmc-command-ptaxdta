package epmc.command;


import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.prism.model.ModelPRISM;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.ModelJANIConverter;
import epmc.messages.OptionsMessages;
import epmc.modelchecker.CommandTask;
import epmc.modelchecker.Log;
import epmc.modelchecker.Model;
import epmc.modelchecker.ModelChecker;
import epmc.modelchecker.RawProperty;
import epmc.options.Options;
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
        this.contextValue = modelChecker.getModel().getContextValue();
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
        
        if (model instanceof ModelJANIConverter) {
        	ModelJANI janimodel = ((ModelJANIConverter)model).toJANI(true);
        	System.out.println(janimodel.toString());
        	
        } else {
        	System.out.println("model cannot be converted to JANI.");
        }
    }
    
}

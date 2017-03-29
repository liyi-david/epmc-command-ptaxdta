package epmc.command;


import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.expression.standard.ExpressionIdentifier;
import epmc.expression.standard.ExpressionIdentifierStandard;
import epmc.expression.standard.ExpressionLiteral;
import epmc.expression.standard.ExpressionOperator;
import epmc.messages.OptionsMessages;
import epmc.modelchecker.CommandTask;
import epmc.modelchecker.Log;
import epmc.modelchecker.Model;
import epmc.modelchecker.ModelChecker;
import epmc.options.Options;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.RegionSpace;
import epmc.ptaxdta.pta.model.ClocksPTA;
import epmc.ptaxdta.pta.model.LocationPTA;
import epmc.ptaxdta.pta.model.LocationPTABasic;
import epmc.ptaxdta.pta.model.ModelPTA;
import epmc.value.*;
import epmc.udbm.*;

import javax.rmi.CORBA.Util;

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

    private Expression buildIntervalIneuality(String name,int l,int r){
		Model model = modelChecker.getModel();
		Expression x = new ExpressionIdentifierStandard.Builder()
				.setName(name)
				.build();

		Expression t1 = new ExpressionLiteral.Builder()
				.setValue(
						UtilValue.newValue(
								TypeInteger.get(model.getContextValue()),
								l)
				)
				.build();

		Expression t2 = new ExpressionLiteral.Builder()
				.setValue(
						UtilValue.newValue(
								TypeInteger.get(model.getContextValue()),
								r)
				)
				.build();
    	Expression c1 = new ExpressionOperator.Builder()
				.setOperator(model.getContextValue().getOperator(OperatorLe.IDENTIFIER))
				.setOperands(t1,x) // l <=x
				.build();
		Expression c2 = new ExpressionOperator.Builder()
				.setOperator(model.getContextValue().getOperator(OperatorLe.IDENTIFIER))
				.setOperands(x,t2) // x <= r
				.build();
		Expression res = new ExpressionOperator.Builder()
				.setOperator(model.getContextValue().getOperator(OperatorAnd.IDENTIFIER))
				.setOperands(c1,c2)
				.build();
		return res;
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

		Expression top = new ExpressionLiteral.Builder()
				.setValue(
						UtilValue.newValue(
								TypeBoolean.get(model.getContextValue()),
								"true")
				)
				.build();

		pta.invariants.put(l0,new ClockConstraint(top,model));
		pta.invariants.put(l1,new ClockConstraint(top,model));
		pta.invariants.put(l2,new ClockConstraint(top,model));

		// addConnection source, guard, probability, resetClocks, target

		Expression g1 = this.buildIntervalIneuality("x",1,2);
		Expression g2 = this.buildIntervalIneuality("x",2,3);

		pta.addConnectionFrom(l0, "i", new ClockConstraint(g1,model))
			.addTarget(0.1, new ClocksPTA("x"), l0)
			.addTarget(0.9, new ClocksPTA("x"), l1);
		
		pta.addConnectionFrom(l1, "i", new ClockConstraint(g2,model))
			.addTarget(0.2, new ClocksPTA("x"), l1)
			.addTarget(0.8, new ClocksPTA("x"), l2);
		
		System.out.print(pta.toJani(null).toString());
		
    }

    public void testCC(Model model) throws EPMCException {
		System.load("/Users/lijianlin/Projects/epmc/plugins/command-ptaxdta/src/main/java/epmc/udbm/udbm_int.so");
//        int x = udbm_int.fact(3);
//        System.out.println(x);

		VarNamesAccessor v = new VarNamesAccessor();
		v.setClockName(0,"*");
		v.setClockName(1,"x");
		v.setClockName(2,"y");


		Constraint c1 = new Constraint(1,0,1,true),
				c2 = new Constraint(2,1,0,false),
				c3 = new Constraint(0,2,0,true);

		Federation f1 = new Federation(3,c1),
				f2 = new Federation(3,c2),
				f3 = new Federation(3,c3);
		Federation f123 = f1.andOp(f2.andOp(f3));

		System.out.print(f123.toStr(v));
		/*
		RegionSpace space = new RegionSpace(new String[]{"a", "b","c","d","e"},new int []{1,1,1,2,2},model);
		space.explore();

//		ClockConstraint cc = new ClockConstraint();

		Expression t[] = new Expression[3];
		String name[] = new String[]{"x","y","z"};
		for (int i=0; i < 3; i++){
			Expression x = new ExpressionIdentifierStandard.Builder()
					.setName(name[i])
					.build();
			Expression c = new ExpressionLiteral.Builder()
					.setValue(UtilValue.newValue(TypeInteger.get(model.getContextValue()), "998"))
					.build();
			t[i] = new ExpressionOperator.Builder()
					.setOperator(contextValue.getOperator(OperatorLe.IDENTIFIER))
					.setOperands(x,c)
					.build();
			System.out.println(t[i]);
		}
		Expression all = new ExpressionOperator.Builder()
				.setOperator(contextValue.getOperator(OperatorAnd.IDENTIFIER))
				.setOperands(t)
				.build();
		System.out.println(all);
		// TODO
		*/
    }

	public ContextValue getContextValue() {
		return contextValue;
	}

	public void setContextValue(ContextValue contextValue) {
		this.contextValue = contextValue;
	}
    
}

package epmc.command;


import epmc.command.util.UtilProduct;
import epmc.error.EPMCException;
import epmc.messages.OptionsMessages;
import epmc.modelchecker.CommandTask;
import epmc.modelchecker.Log;
import epmc.modelchecker.Model;
import epmc.modelchecker.ModelChecker;
import epmc.options.Options;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.UtilDBM;
import epmc.ptaxdta.pta.model.ClocksPTA;
import epmc.ptaxdta.pta.model.LocationPTA;
import epmc.ptaxdta.pta.model.LocationPTABasic;
import epmc.ptaxdta.pta.model.ModelPTA;
import epmc.udbm.AtomConstraint;
import epmc.value.*;

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

    private ClockConstraint buildIntervalIneuality(ClockSpace space, String name,int l,int r){
		Model model = modelChecker.getModel();
        Integer x = space.getClockbyName(name);
        AtomConstraint cl = new AtomConstraint(0,x,-l,false);
        AtomConstraint cr = new AtomConstraint(x,0,r,false);
		ClockConstraint c = ClockConstraint.TOP(space);
		c.setAnd(cl).setAnd(cr);
		return c;
        /*
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
		*/
	}

    public void check() throws EPMCException {
        UtilDBM.LoadUDBM();
        ModelPTA pta = ModelPTAExample();
        ModelPTA dta = ModelDTAExample();
        
		
		System.out.println("[Source Model as PTA] \n" + pta.toJani(null).toString());
		System.out.println("[Property as DTA] \n" + dta.toJani(null).toString());
		
		System.out.println(UtilProduct.prod(pta, dta).toJani(null).toString());
    }
    
    private ModelPTA ModelDTAExample() {
		ModelPTA dta = new ModelPTA("task-complete-prop");
		Model model = modelChecker.getModel();
		
		dta.setContextValue(model.getContextValue());
		dta.actions.add("alpha");
		dta.clocks.clocknames.add("y");
		dta.clocks.clocknames.add("z");
		
		LocationPTA q0 = dta.initialLocations.addLocation(
				dta.locations.addLocation(new LocationPTABasic("q0"))
				);
		LocationPTA q1 = dta.locations.addLocation(new LocationPTABasic("q1"));
		LocationPTA q2 = dta.locations.addLocation(new LocationPTABasic("q2"));
		LocationPTA q3 = dta.locations.addLocation(new LocationPTABasic("q3"));

        ClockSpace space = new ClockSpace(dta.clocks);
        space.setModel(model);

        ClockConstraint top = ClockConstraint.TOP(space);

		dta.invariants.put(q0,top);
		dta.invariants.put(q1,top);
		dta.invariants.put(q2,top);
		dta.invariants.put(q3,top);

		ClockConstraint g12 = this.buildIntervalIneuality(space,"y",0,3);
		ClockConstraint g23 = this.buildIntervalIneuality(space,"z",0,6).setAnd(
				this.buildIntervalIneuality(space,"y",0,4)
				);

		dta.addConnectionFrom(q0, "alpha", top)
			.addTarget(1, new ClocksPTA("y"), q1);
		
		dta.addConnectionFrom(q1, "alpha", top)
			.addTarget(1, new ClocksPTA(), q1);
		
		dta.addConnectionFrom(q1, "beta", g12)
			.addTarget(1, new ClocksPTA("y"), q2);
		
		dta.addConnectionFrom(q2, "beta", top)
			.addTarget(1, new ClocksPTA(), q2);
		
		dta.addConnectionFrom(q2, "gamma", g23)
			.addTarget(1, new ClocksPTA(), q3);
		
		return dta;
	}

	public ModelPTA ModelPTAExample() throws EPMCException {
    	Model model = modelChecker.getModel();        
        ModelPTA pta = new ModelPTA("task-complete");
        
        pta.setContextValue(model.getContextValue());
		pta.actions.add("i");
		pta.clocks.clocknames.add("x");
		
		LocationPTA l0 = pta.initialLocations.addLocation(
				pta.locations.addLocation(new LocationPTABasic("l0"))
				);
		LocationPTA l1 = pta.locations.addLocation(new LocationPTABasic("l1"));
		LocationPTA l2 = pta.locations.addLocation(new LocationPTABasic("l2"));

        ClockSpace space = new ClockSpace(pta.clocks);
        space.setModel(model);

        ClockConstraint top = ClockConstraint.TOP(space);

		pta.invariants.put(l0,top);
		pta.invariants.put(l1,top);
		pta.invariants.put(l2,top);

		ClockConstraint g1 = this.buildIntervalIneuality(space,"x",1,2);
		ClockConstraint g2 = this.buildIntervalIneuality(space,"x",2,3);

		pta.addConnectionFrom(l0, "i", g1)
			.addTarget(0.1, new ClocksPTA("x"), l0)
			.addTarget(0.9, new ClocksPTA("x"), l1);
		
		pta.addConnectionFrom(l1, "i", g2)
			.addTarget(0.2, new ClocksPTA("x"), l1)
			.addTarget(0.8, new ClocksPTA("x"), l2);
		
		return pta;
    }

    public void testCC(Model model) throws EPMCException {
        UtilDBM.LoadUDBM();

//        ClocksPTA clocks = new ClocksPTA("x");
//        ClockSpace space = new ClockSpace(clocks);

//        ClockConstraint c = new ClockConstraint(space);
//        c.setInit();
//        Expression s0 = UtilDBM.UDBMString2Expression(c.toString(),model);

//        Expression s1 = UtilDBM.UDBMString2Expression("(x==1 && 0<y && y<1) || (x==1)",model);
//        System.out.println(s1);
//        Expression s2 = UtilDBM.UDBMString2Expression("(1<y && x<1 && y-x<=1)",model);
//        System.out.println(s2);

//        System.load("/Users/lijianlin/Projects/epmc/plugins/command-ptaxdta/src/main/java/epmc/udbm/udbm_int.so");
////        int x = udbm_int.fact(3);
////        System.out.println(x);
//
//		VarNamesAccessor v = new VarNamesAccessor();
//		v.setClockName(0,"*");
//		v.setClockName(1,"x");
//		v.setClockName(2,"y");
//
//
//		AtomConstraint c1 = new AtomConstraint(1,0,1,true),
//				c2 = new AtomConstraint(2,1,0,false),
//				c3 = new AtomConstraint(0,2,0,true);
//
//		Federation f1 = new Federation(3,c1);
//		f1 = f1.andOp(c2).andOp(c3);
//        f1.updateValue(1,1);
//
//        System.out.print(f1.toStr(v));

		ClockSpace s = new ClockSpace(new String[]{"x", "y"},new int []{5,5},model);
		s.explore();
/*
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

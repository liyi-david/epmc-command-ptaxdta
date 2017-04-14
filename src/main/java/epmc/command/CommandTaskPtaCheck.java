package epmc.command;


import epmc.command.util.UtilProductV2;
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
import epmc.ptaxdta.pta.model.*;
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

		UtilProductV2 util = new UtilProductV2();
		ModelPTA result = util.prod(pta, dta);
		
		System.out.println("[Result]");
		System.out.println(result.toJani(null));

		System.out.println("[Result to single]");
		System.out.println(result.toSingleJani(null));
		System.out.println(result.toPrism());

        ModelPTA pta2 = ModelPTAExample2();
        System.out.println(pta2.toJani(null));

        ModelPTA dta2 = ModelDTAExample2();
        System.out.println(dta2.toJani(null));
	}
    
    private ModelPTA ModelDTAExample() {
		ModelPTA dta = new ModelPTA("task_complete_prop");
		Model model = modelChecker.getModel();
		
		dta.setContextValue(model.getContextValue());
		dta.addLabels("alpha", "beta", "gamma");
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

        dta.setSpace(space);
        ClockConstraint top = ClockConstraint.TOP(space);

		dta.invariants.put(q0,top);
		dta.invariants.put(q1,top);
		dta.invariants.put(q2,top);
		dta.invariants.put(q3,top);

		ClockConstraint g12 = this.buildIntervalIneuality(space,"y",0,3);
		ClockConstraint g23 = this.buildIntervalIneuality(space,"z",0,6).setAnd(
				this.buildIntervalIneuality(space,"y",0,4)
				);

		dta.addConnectionFrom(q0, new LabelPTA("alpha"), top)
			.addTarget(1, new ClocksPTA("y"), q1);
		
		dta.addConnectionFrom(q1, new LabelPTA("alpha"), top)
			.addTarget(1, new ClocksPTA(), q1);
		
		dta.addConnectionFrom(q1, new LabelPTA("beta"), g12)
			.addTarget(1, new ClocksPTA("y"), q2);
		
		dta.addConnectionFrom(q2, new LabelPTA("beta"), top)
			.addTarget(1, new ClocksPTA(), q2);
		
		dta.addConnectionFrom(q2, new LabelPTA("gamma"), g23)
			.addTarget(1, new ClocksPTA(), q3);

		dta.setAP(new APSet("alpha","beta","gamma"));
		
		dta.setFinalLocation(q3);
		dta.addTrapLocation();
		return dta;
	}

	public ModelPTA ModelPTAExample() throws EPMCException {
    	Model model = modelChecker.getModel();        
        ModelPTA pta = new ModelPTA("task_complete");
        
        pta.setContextValue(model.getContextValue());
		pta.actions.add(new ActionStandardPTA("i"));
		pta.clocks.clocknames.add("x");
		
		LocationPTA l0 = pta.initialLocations.addLocation(
				pta.locations.addLocation(new LocationPTABasic("l0"))
		);

		LocationPTA l1 = pta.locations.addLocation(new LocationPTABasic("l1"));
		LocationPTA l2 = pta.locations.addLocation(new LocationPTABasic("l2"));

        ClockSpace space = new ClockSpace(pta.clocks);
        space.setModel(model);

        pta.setSpace(space);

        ClockConstraint top = ClockConstraint.TOP(space);

		pta.invariants.put(l0,UtilDBM.UDBMString2CC("(x <= 2)", space));
		pta.invariants.put(l1,UtilDBM.UDBMString2CC("(x <= 3)", space));
		pta.invariants.put(l2,top);

		pta.label.put(l0,new LabelPTA("alpha"));
		pta.label.put(l1,new LabelPTA("beta"));
		pta.label.put(l2,new LabelPTA("gamma"));


		ClockConstraint g1 = UtilDBM.UDBMString2CC("(1 <= x) && (x <= 2)", space);
		ClockConstraint g2 = UtilDBM.UDBMString2CC("(2 <= x) && (x <= 3)", space);

		pta.addConnectionFrom(l0, new ActionStandardPTA("i"), g1)
			.addTarget(0.1, new ClocksPTA("x"), l0)
			.addTarget(0.9, new ClocksPTA("x"), l1);
		
		pta.addConnectionFrom(l1, new ActionStandardPTA("i"), g2)
			.addTarget(0.2, new ClocksPTA("x"), l1)
			.addTarget(0.8, new ClocksPTA("x"), l2);

		pta.addConnectionFrom(l2, new ActionStandardPTA("i"),top)
				.addTarget(1,new ClocksPTA(),l2);

		pta.setAP(new APSet("alpha","beta","gamma"));

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
    private ModelPTA ModelDTAExample2() {
        ModelPTA dta = new ModelPTA("Navigation_prop");
        Model model = modelChecker.getModel();

        dta.setContextValue(model.getContextValue());
        dta.addLabels("alpha", "beta", "gamma");
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

        dta.setSpace(space);
        ClockConstraint top = ClockConstraint.TOP(space);

        dta.invariants.put(q0,top);
        dta.invariants.put(q1,top);
        dta.invariants.put(q2,top);
        dta.invariants.put(q3,top);

        ClockConstraint g_0_y_4 = this.buildIntervalIneuality(space,"y",0,4);
        ClockConstraint g_0_y_5 = this.buildIntervalIneuality(space,"y",0,5);

        ClockConstraint g_y_4_z_30 = this.buildIntervalIneuality(space,"z",0,30).setAnd(
                this.buildIntervalIneuality(space,"y",0,4)
        );
        ClockConstraint g_y_5_z_30 = this.buildIntervalIneuality(space,"z",0,30).setAnd(
                this.buildIntervalIneuality(space,"y",0,5)
        );


        dta.addConnectionFrom(q0, new LabelPTA("alpha"), top)
                .addTarget(1, new ClocksPTA("y"), q1);
        dta.addConnectionFrom(q0, new LabelPTA("beta"), top)
                .addTarget(1, new ClocksPTA("y"), q1);

        //TODO check q2 ->
        dta.addConnectionFrom(q1, new LabelPTA("alpha"), g_0_y_4)
                .addTarget(1, new ClocksPTA("y"), q1);
        dta.addConnectionFrom(q1, new LabelPTA("beta"), g_0_y_4)
                .addTarget(1, new ClocksPTA("y"), q2);
        dta.addConnectionFrom(q1, new LabelPTA("gamma"), g_y_4_z_30)
                .addTarget(1, new ClocksPTA(), q3);

        dta.addConnectionFrom(q2, new LabelPTA("alpha"), g_0_y_5)
                .addTarget(1, new ClocksPTA("y"), q1);
        dta.addConnectionFrom(q2, new LabelPTA("beta"), g_0_y_5)
                .addTarget(1, new ClocksPTA("y"), q2);
        dta.addConnectionFrom(q2, new LabelPTA("gamma"), g_y_5_z_30)
                .addTarget(1, new ClocksPTA(), q3);

        dta.setFinalLocation(q3);
        dta.addTrapLocation();

        dta.setAP(new APSet("alpha","beta","gamma"));


        return dta;
    }

	public ModelPTA ModelPTAExample2() throws EPMCException {
		Model model = modelChecker.getModel();
		ModelPTA pta = new ModelPTA("Navigation");

		pta.setContextValue(model.getContextValue());
		pta.actions.add(new ActionStandardPTA("i"));
		pta.clocks.clocknames.add("x");

		LocationPTA l00 = pta.initialLocations.addLocation(
				pta.locations.addLocation(new LocationPTABasic("l-0-0"))
		);

		LocationPTA l01 = pta.locations.addLocation(new LocationPTABasic("l-0-1"));
		LocationPTA l02 = pta.locations.addLocation(new LocationPTABasic("l-0-2"));

		LocationPTA l10 = pta.locations.addLocation(new LocationPTABasic("l-1-0"));
		LocationPTA l11 = pta.locations.addLocation(new LocationPTABasic("l-1-1"));

		LocationPTA l21 = pta.locations.addLocation(new LocationPTABasic("l-2-1"));
		LocationPTA l22 = pta.locations.addLocation(new LocationPTABasic("l-2-2"));

		ClockSpace space = new ClockSpace(pta.clocks);
		space.setModel(model);

		pta.setSpace(space);

		ClockConstraint top = ClockConstraint.TOP(space);

		pta.invariants.put(l00,top);
		pta.invariants.put(l01,top);
		pta.invariants.put(l02,top);

		pta.invariants.put(l10,top);
		pta.invariants.put(l11,top);

		pta.invariants.put(l21,top);
		pta.invariants.put(l22,top);

		pta.label.put(l00,new LabelPTA("beta"));
		pta.label.put(l01,new LabelPTA("alpha"));
		pta.label.put(l02,new LabelPTA("alpha"));

		pta.label.put(l10,new LabelPTA("beta"));
		pta.label.put(l11,new LabelPTA("alpha"));

		pta.label.put(l21,new LabelPTA("beta"));
		pta.label.put(l22,new LabelPTA("gamma"));

		ClockConstraint g1 = this.buildIntervalIneuality(space,"x",1,2);
		ClockConstraint g2 = this.buildIntervalIneuality(space,"x",2,3);

		pta.addConnectionFrom(l00, new ActionStandardPTA("i"), g2)
				.addTarget(0.5, new ClocksPTA("x"), l01)
				.addTarget(0.5, new ClocksPTA("x"), l11);
		pta.addConnectionFrom(l01, new ActionStandardPTA("i"), g1)
				.addTarget(1.0 / 3, new ClocksPTA("x"), l02)
				.addTarget(1.0 / 3, new ClocksPTA("x"), l11)
                .addTarget(1.0 / 3, new ClocksPTA("x"), l00);
        pta.addConnectionFrom(l02, new ActionStandardPTA("i"), g1)
                .addTarget(1.0, new ClocksPTA("x"), l01);

        pta.addConnectionFrom(l10, new ActionStandardPTA("i"), g2)
                .addTarget(0.5, new ClocksPTA("x"), l11)
                .addTarget(0.5, new ClocksPTA("x"), l00);
        pta.addConnectionFrom(l11, new ActionStandardPTA("i"), g1)
                .addTarget(1.0 / 3, new ClocksPTA("x"), l21)
                .addTarget(1.0 / 3, new ClocksPTA("x"), l10)
                .addTarget(1.0 / 3, new ClocksPTA("x"), l01);

        pta.addConnectionFrom(l21, new ActionStandardPTA("i"), g2)
                .addTarget(0.5, new ClocksPTA("x"), l11)
                .addTarget(0.5, new ClocksPTA("x"), l22);

        pta.addConnectionFrom(l22, new ActionStandardPTA("i"),top)
				.addTarget(1,new ClocksPTA(),l22);
        //TODO l_2,2 x \in [2,3] ???

		pta.setAP(new APSet("alpha","beta","gamma"));

		return pta;
	}

	public ContextValue getContextValue() {
		return contextValue;
	}

	public void setContextValue(ContextValue contextValue) {
		this.contextValue = contextValue;
	}
    
}

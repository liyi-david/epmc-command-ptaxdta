package epmc.ptaxdta;

import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.jani.model.ModelJANI;
import epmc.expression.standard.*;
import epmc.modelchecker.Model;
import epmc.value.OperatorAnd;

/**
 * Created by lijianlin on 17/3/22.
 */
public class ClockConstraint {
    private Expression exp;
    private Model model = null;

//    public ClockConstraint(RegionElement e) {
//
//    }

    public ClockConstraint(Expression exp,Model model) {
        this.exp   = exp;
        this.model = model;
    }
    public ClockConstraint(RegionElement R,Model model) throws EPMCException {
        this.exp   = R.toExpression();
        this.model = model;
    }

    public Expression getExp() {
        return exp;
    }

    public ClockConstraint and(Expression e){
        Expression res = new ExpressionOperator.Builder()
                .setOperator(this.model.getContextValue().getOperator(OperatorAnd.IDENTIFIER))
                .setOperands(this.exp,e)
                .build();
        this.exp = res;
        return this;
    }

    public ClockConstraint and(RegionElement e) throws EPMCException {
        return this.and(e.toExpression());
    }
    public ClockConstraint and(ClockConstraint c){
        return this.and(c.getExp());
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(ModelJANI model) {
        this.model = model;
    }

    public static void main(String[] args) throws EPMCException {

//        Options options = UtilOptionsEPMC.newOptions();
//        ClockConstraint.context = new ContextValue(options);
//        ClockConstraint.model   = new ModelJANI();
//        ClockConstraint.model.setContext(ClockConstraint.context);
//        c1.setModel(ClockConstraint.model);
//        "guard":{
//            "exp":{
//                "op":"∧",
//                        "left":{
//                    "op":"=",
//                            "left":"x",
//                            "right":1
//                },
//                "right":{
//                    "op":"≥",
//                            "left":"x",
//                            "right":2
//                }
//            }
//        },
        /*
        TreeMap<String,Integer> config = new TreeMap<String,Integer>();
        ExpressionParser parser = new ExpressionParser(ClockConstraint.model, null,false);

        JsonBuilderFactory factory = Json.createBuilderFactory(config);

        JsonObject obj1 = factory.createObjectBuilder()
                .add("op","∧")
                .add("left",factory.createObjectBuilder()
                    .add("op","=")
                    .add("left","x")
                    .add("right",1).build())
                .add("right",factory.createObjectBuilder()
                        .add("op","<")
                        .add("left","y")
                        .add("right",2).build()).build();
//        TreeMap<String,JANIIdentifier> identifier =;
        JANIExpression c1 = parser.parseAsJANIExpression(obj1);
        System.out.println(c1);

        JsonObject obj2 = factory.createObjectBuilder()
                .add("op","=")
                .add("left","y")
                .add("right",1).build();
//        TreeMap<String,JANIIdentifier> identifier =;
        System.out.println(obj1.getValueType());
        JANIExpression c2 = parser.parseAsJANIExpression(obj2);
        System.out.println(c2);

        JANIExpression c3 = new JANIExpressionOperatorBinary();

        System.out.println(c1);
        */
    }
}

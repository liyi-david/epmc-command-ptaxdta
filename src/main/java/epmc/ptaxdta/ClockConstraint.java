package epmc.ptaxdta;

import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.udbm.*;

/**
 * Created by lijianlin on 17/3/22.
 */
public class ClockConstraint  implements Cloneable {
//    private Expression exp;
    private Federation fed;
    private ClockSpace space;
//    private Model model = null;

//    private fed_t fed;
//    public ClockConstraint(Region e) {
//
//    }

//    public ClockConstraint(Expression exp,Model model) {
//        this.exp   = exp;
//        this.model = model;
//    }
//    public ClockConstraint(Region R,Model model) throws EPMCException {
//        this.exp   = R.toExpression();
//        this.model = model;
//    }

    static public ClockConstraint BOT(ClockSpace space){
        ClockConstraint bot = new ClockConstraint(space);
        return bot;
    }
    static public ClockConstraint TOP(ClockSpace space){
        ClockConstraint top = new ClockConstraint(space);
        top.setInit();
        return top;
    }
    private ClockConstraint(ClockSpace space) {
        this.space = space;
        this.fed = new Federation(space.getDimension());
    }

    public void setFed(Federation fed) {
        this.fed = fed;
    }

    public void setSpace(ClockSpace space) {
        this.space = space;
    }

    public ClockConstraint setInit(){
        this.fed.setInit();
        return this;
    }
    public ClockConstraint setAnd(AtomConstraint c){
        this.fed = this.fed.andOp(c);
        return this;
    }
    public ClockConstraint setAnd(Federation f){
        this.fed = this.fed.andOp(f);
        return this;
    }
    public ClockConstraint setAnd(ClockConstraint c){
        this.fed = this.fed.andOp(c.fed);
        return this;
    }
    public ClockConstraint setAnd(Region R) {
        this.fed = this.fed.andOp(R.fed);
        return this;
    }

    public Expression toExpression() throws EPMCException {
        VarNamesAccessor v = this.space.getVarNamesAccessor();
        return  UtilDBM.UDBMString2Expression(this.fed.toStr(v),this.space.getModel());
    }

    @Override
    public String toString() {
        if(this.space != null){
            return this.fed.toStr(this.space.getVarNamesAccessor());
        }
        else {
            return this.fed.toStr(new VarNamesAccessor());
        }
    }
    public String toUDBMString() {
        if(this.space != null){
            return this.fed.toStr(this.space.getVarNamesAccessor());
        }
        else {
            return this.fed.toStr(new VarNamesAccessor());
        }
    }
    //    public Expression getExp() {
//        return exp;
//    }

//    public ClockConstraint and(Expression e){
//        Expression res = new ExpressionOperator.Builder()
//                .setOperator(this.model.getContextValue().getOperator(OperatorAnd.IDENTIFIER))
//                .setOperands(this.exp,e)
//                .build();
//        this.exp = res;
//        return this;
//    }


//    public ClockConstraint and(Region e) throws EPMCException {
//        return this.and(e.toExpression());
//    }
//    public ClockConstraint and(ClockConstraint c){
//        return this.and(c.getExp());
//    }

//    public Model getModel() {
//        return this.model;
//    }

    public Federation getFed() {
        return fed;
    }

//    public void setModel(Model model) {
//        this.model = model;
//    }
    @Override
    public Object clone()  {
        try {
            ClockConstraint c =  (ClockConstraint) super.clone();
            c.setSpace(this.space);
            Federation f = new Federation(this.space.getDimension());
            f.setInit();
            f.andOp(this.fed);
            c.setFed(f);
            return c;
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;
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
    public boolean satisfy(ClockConstraint g){
        return this.fed.le(g.getFed());
    }
}

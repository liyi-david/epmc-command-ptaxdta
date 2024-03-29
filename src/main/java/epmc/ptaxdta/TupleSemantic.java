package epmc.ptaxdta;

import com.sun.xml.internal.fastinfoset.algorithm.IEEE754FloatingPointEncodingAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lijianlin on 17/4/3.
 */

public class TupleSemantic implements Cloneable {
    public ClockSpace space;
    public IVInterval[] J; // [0,this.getDimension)

    public ArrayList<Pair<Integer,Integer>> fracOrder;
    ////    private int D;    combined with FracOrder

    public int getSymbol(int index){ // [1,fracOrder.size()-1]
        return this.fracOrder.get(index).getLeft();
    }

    public int getOrder(int index){
        return this.fracOrder.get(index).getRight();
    }

    public TupleSemantic(ClockSpace space, IVInterval[] J, ArrayList<Integer> fracOrder, int D) {
        this.space = space;
        this.J = J;
        this.fracOrder = new ArrayList<Pair<Integer,Integer>>();
        for(int i=0; i < fracOrder.size(); i++){
            if(i==0){
                this.fracOrder.add(Pair.of(null,fracOrder.get(i)));
            }
            else {
                this.fracOrder.add(Pair.of( (D>>(i-1)) & 1, fracOrder.get(i) ));
            }
        }
    }

    public TupleSemantic(ClockSpace space, IVInterval[] J, ArrayList<Pair<Integer,Integer>>  fracOrder) {
        this.space = space;
        this.J = J.clone();
        this.fracOrder = (ArrayList<Pair<Integer,Integer>>) fracOrder.clone();
    }

/*
    public Expression toExpression() throws EPMCException {
        String symbol[] = new String[]{OperatorLt.IDENTIFIER, OperatorLe.IDENTIFIER};
        // 0 for not closed (<), 1 for closed (<=)

        Model model = this.space.getModel();
        ArrayList v = new ArrayList();

        for(int i=0; i < this.space.getDimension(); i++) { // i for clock
            IVInterval interv = this.J[i];


            Expression c = new ExpressionLiteral.Builder()
                    .setValue(UtilValue.newValue(TypeInteger.get(model.getContextValue()),
                            Integer.toString(interv.lower)))
                    .build();
            Expression x = new ExpressionIdentifierStandard.Builder()
                    .setName(this.space.getClockName()[i])
                    .build();
            // TODO collection Expressions for clock name and reuse them
            // FIXME too much Expression for the same clock name were generated
            Expression t = new ExpressionOperator.Builder()
                    .setOperator(model.getContextValue().getOperator(symbol[interv.lowerClosed]))
                    .setOperands(c, x) // c </<= x
                    .build();
            v.add(t);

            if (interv.upper != IVInterval.INF) {
                c = new ExpressionLiteral.Builder()
                        .setValue(UtilValue.newValue(TypeInteger.get(model.getContextValue()),
                                Integer.toString(interv.upper)))
                        .build();
                x = new ExpressionIdentifierStandard.Builder()
                        .setName(this.space.getClockName()[i])
                        .build();
                t = new ExpressionOperator.Builder()
                        .setOperator(model.getContextValue().getOperator(symbol[interv.upperClosed]))
                        .setOperands(x, c) //x </<= c
                        .build();
                v.add(t);
            }
        }

        if (this.fracOrder.size() > 1) {
            for (int i = 1; i < this.fracOrder.size(); i++) {
                int isEq = this.getSymbol(i); //((this.D >> (i - 1)) & 1);
                int clockX = this.getOrder(i - 1);
                int clockY = this.getOrder(i);
                int intDiff = this.J[clockX].lower - this.J[clockY].lower;

                Expression d = new ExpressionLiteral.Builder()
                        .setValue(UtilValue.newValue(TypeInteger.get(model.getContextValue()),
                                Integer.toString(intDiff)))
                        .build();

                Expression x = new ExpressionIdentifierStandard.Builder()
                        .setName(this.space.getClockName()[clockX])
                        .build();
                Expression y = new ExpressionIdentifierStandard.Builder()
                        .setName(this.space.getClockName()[clockY])
                        .build();

                // y + d
                Expression right = new ExpressionOperator.Builder()
                        .setOperator(model.getContextValue().getOperator(OperatorAdd.IDENTIFIER))
                        .setOperands(y, d)
                        .build();

                // x < y + d
                Expression t = new ExpressionOperator.Builder()
                        .setOperator(model.getContextValue().getOperator(symbol[isEq]))
                        .setOperands(x, right) // x </<= y + integer part of x - integer part of y
                        .build();
                v.add(t);
            }
        }
        // conjunction of every thing
        Expression res = new ExpressionOperator.Builder()
                .setOperator(model.getContextValue().getOperator(OperatorAnd.IDENTIFIER))
                .setOperands(v)
                .build();
        return res;
        //TODO : did't check how much do ExpressionOperator support the arbitrary arity

    }
*/
    @Override
    public String toString()  { //for DEBUG
//            try {
//                return this.toExpression().toString();
//            } catch (EPMCException e) {
//                e.printStackTrace();
//            }
        String res = "";
        for (int i = this.space.getFirstExternalClock(); i < this.space.getDimension(); i++) {
            res += this.space.getClockName()[i] + " : " + this.J[i] + "\n";
        }

        if (this.fracOrder.size() != 0) {
            res += this.space.getClockName()[this.getOrder(0)];
            for (int i = 1; i < this.fracOrder.size(); i++) {
//                String symbol = (((this.D >> (i - 1)) & 1) == 1) ? "=" : "<" ;
                String symbol = new String[]{"<","="}[this.getSymbol(i)];
                res += symbol + this.space.getClockName()[this.getOrder(i)];

            }
            res += "\n";
        }
        return res;
//            return super.toString();
    }
    public TupleSemantic reset(ArrayList<Integer> X){ // setReset
        for (int i=0; i < X.size(); i++) {
            int clock = X.get(i);
            this.J[clock] = new IVInterval(1, 0, 0, 1);
            for (int j = 0; j < this.fracOrder.size(); j++) {
                if(this.fracOrder.get(j).getRight()==clock){
                    if(j==0){
                        if(this.fracOrder.size() >1) {
                            this.fracOrder.get(1).left = null;
                        }
                    }
                    else if( (0 < j) && (j < this.fracOrder.size() -1)) {
                        int symbol = this.getSymbol(j) * this.getSymbol(j+1);
                        this.fracOrder.get(j+1).left = symbol;

                    }
                    this.fracOrder.remove(j);
                    break;
                }
            }

        }
        return this;
    }
    public TupleSemantic timeSuccessor(){
        IVInterval[] succJ = this.J.clone();
        ArrayList<Pair<Integer,Integer>> order = (ArrayList<Pair<Integer,Integer>>) this.fracOrder.clone();
        // order can be empty but should not be null

        ArrayList<Integer> X = new ArrayList<>();
        for (int i = this.fracOrder.size() - 1; i >= 0; i++){
            Integer ClockX = this.getOrder(i);
            Integer symbol = this.getSymbol(i);
            X.add(ClockX);
            if(symbol != 1) break;
        }

        if(X.size() > 0){
            for (Integer x : X) {
                succJ[x] = succJ[x].successor(this.space.getBoundary()[x]);
                fracOrder.remove(fracOrder.size() - 1);
            }

            return new TupleSemantic(this.space,succJ,order);
        }

        final IVInterval[] fJ = succJ;
        List<IVInterval> ListJ =
                IntStream.range(0, succJ.length)
                        .mapToObj(i -> i == 0 ? fJ[i] : fJ[i].successor(this.space.getBoundary()[i]))
                        .collect(Collectors.toList());
//        System.out.println(ListJ);
        Object [] a = ListJ.toArray();

        succJ = (IVInterval [])ListJ.toArray(new IVInterval[0]);

        return new TupleSemantic(this.space,succJ,order);
    }

    @Override
    public TupleSemantic clone(){
        return new TupleSemantic(this.space,this.J,this.fracOrder);
    }
        /*
    public int resloveTerm(Expression term){ // TODO

        if (term instanceof ExpressionOperator) {
            Operator o = ((ExpressionOperator) term).getOperator();
            ArrayList<Expression> oprands = (ArrayList<Expression>) ((ExpressionOperator) term).getOperands();
            if (o instanceof OperatorAdd){ //binary
                return this.resloveTerm(oprands.get(0)) + this.resloveTerm(oprands.get(1));
            }
        }
        else if(term instanceof ExpressionIdentifierStandard) {
            String name = ((ExpressionIdentifierStandard) term).getName();
            int x = this.space.findClockbyName(name);

        }
        else if(term instanceof ExpressionLiteral){
//            return ((ExpressionLiteral) term).getValue()
        }
        return 0;
    }
    public boolean isModelof(Expression cc){ //TODO
        if (cc instanceof ExpressionOperator) {
            Operator o = ((ExpressionOperator) cc).getOperator();
            ArrayList<Expression> oprands = (ArrayList<Expression>) ((ExpressionOperator) cc).getOperands();
            if( o instanceof OperatorAnd) { // arbitrary nary for boolean connectives supported
                for (int i = 0; i < oprands.size(); i++) {
                    if(!(this.isModelof(oprands.get(i)))) {
                        return false;
                    }
                }
                return true;
            }
            else if ( o instanceof  OperatorOr) {
                for (int i = 0; i < oprands.size(); i++) {
                    if(!(this.isModelof(oprands.get(i)))) {
                        return true;
                    }
                }
                return false;
            }
            else if ( o instanceof  OperatorNot) {
                return ! this.isModelof(oprands.get(0));
            }
            else if ( o instanceof OperatorLe) { // binary
                return (this.resloveTerm(oprands.get(0)) <= this.resloveTerm(oprands.get(1)));
            }
        }
        if( cc instanceof  ExpressionLiteral){

        }
        return false;
    }*/
        /*
    public JsonObject toJsonObject() {

        String jsonString = this.toJSON().toJSONString();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        return object;
    }
    public JSONObject toJSON(){
//        TreeMap<String,Integer> config = new TreeMap<String,Integer>();
//        JsonBuilderFactory factory = Json.createBuilderFactory(config);
//
//        JsonObjectBuilder builder =  factory.createObjectBuilder();
        ArrayList v = new ArrayList();
        for(int i=0; i < this.getDimension(); i++){
            IVInterval interv = this.J[i];
            if (interv.isPoint()){ // not necessary
                HashMap m = new HashMap();
                m.put("op","=");
                m.put("left",this.space.getClockName()[i]);
                m.put("right",interv.pointValue());
                v.add(m);
            }
            else {
                HashMap m = new HashMap();
                m.put("op",(interv.lowerClosed == 1) ? "≤" : "<");
                m.put("left",interv.lower);
                m.put("right",this.space.getClockName()[i]);
                v.add(m);

                m = new HashMap();
                m.put("op",(interv.upperClosed == 1) ? "≤" : "<");
                m.put("left",this.space.getClockName()[i]);
                m.put("right",interv.upper);
                v.add(m);
            }
        }
        if (this.fracOrder.size() > 1) {
            for (int i = 1; i < this.fracOrder.size(); i++) {
                String symbol = (((this.D >> (i - 1)) & 1) == 1) ?  "≤" : "<" ;
                HashMap d = new HashMap();
                d.put("op","+");
                d.put("left",this.space.getClockName()[this.fracOrder.get(i)]);
                d.put("right",this.J[this.fracOrder.get(i-1)].lower - this.J[this.fracOrder.get(i)].lower);

                HashMap m = new HashMap();
                m.put("op",symbol);
                m.put("left",this.space.getClockName()[this.fracOrder.get(i-1)]);
                m.put("right",d);

                v.add(m);
            }
        }

        HashMap res = new HashMap();
        HashMap cur = res;
        for(int i=0; i < v.size() - 1; i++){
            HashMap next = new HashMap();
            cur.put("op","∧");
            cur.put("left",v.get(i));
            cur.put("right",next);
            cur = next;
        }
        cur.put("right",v.get(v.size()-1));
//        return 0;
        return (JSONObject) JSON.toJSON(res);
    }  */
}
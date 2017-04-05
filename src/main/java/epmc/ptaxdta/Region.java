package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */
import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.udbm.AtomConstraint;
import epmc.udbm.Federation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * a clock is a Integer number (use as Index of J and clockName)
 * the Domain of clocks is N_{\ge0}
 */
public class Region implements Cloneable {

    private ClockSpace space;
    private TupleSemantic tuple;

    //private int name; // TODO for encode

    public TupleSemantic getTuple() {
        return tuple;
    }

    public Federation fed;
    static public Region ZERO(ClockSpace space){
        IVInterval[] J = new IVInterval[space.getDimension()];
        for (int i = 0; i < space.getDimension(); i++) {
            J[i] = new IVInterval(1,0,0,1);
        }
        return new Region(space,J,new ArrayList<Integer>(),0);
    }

    public Region(TupleSemantic tuple) {
        this.tuple = tuple;
        this.space = tuple.space;
        this.fed = this.resolveIntegerPartConstrain(this.space,tuple.J);
        this.fed = this.fed.andOp(this.resolveFractionalConstrain(this.space,tuple.J,tuple.fracOrder));
    }

    public Region(ClockSpace space, Federation fed) {
        this.space = space;
        this.fed   = fed;
    }

    public Region(ClockSpace space, IVInterval[] J, ArrayList<Integer> fracOrder, int D) {
        this.space = space;
        this.fed = this.resolveIntegerPartConstrain(space,J);
        this.fed = this.fed.andOp(this.resolveFractionalConstrain(space,J,fracOrder,D));
        this.tuple = new TupleSemantic(space,J,fracOrder,D);

    }

    private Federation resolveIntegerPartConstrain(ClockSpace space, IVInterval[] J){
        Federation res = new Federation(space.getDimension());
        res.setInit();
        for(int i=1; i < this.getDimension(); i++) { // i for clock //start from the second clock
            IVInterval interv = J[i];//TODO do the corresponding change in ClockSpace.explore
            res = res.andOp(new AtomConstraint(0,i,-interv.lower,!interv.isLowerClosed()));
            // 0 - x < / <= -l

            if (interv.upper != IVInterval.INF) {
                res = res.andOp(new AtomConstraint(i,0,interv.upper,!interv.isUpperClosed()));
            }
            // x - 0 < / <= r
        }
        return res;
    }
    private Federation resolveFractionalConstrain(ClockSpace space, IVInterval[] J, ArrayList<Integer> fracOrder, int D){
        List<Pair<Integer,Integer>> combine =
                IntStream.range(0, fracOrder.size())
                    .mapToObj(i -> (Pair<Integer,Integer>)Pair.of(fracOrder.get(i), i == 0 ? -1 : (D >> (i -1) ) & 1 ))
                    .collect(Collectors.toList());
        ArrayList<Pair<Integer,Integer>> Order = new ArrayList<Pair<Integer,Integer>>(combine);

        return this.resolveFractionalConstrain(space,J,Order);
    }

    private Federation resolveFractionalConstrain(ClockSpace space, IVInterval[] J, ArrayList<Pair<Integer,Integer>> fracOrder){
        Federation res = new Federation(space.getDimension());
        res.setInit();
        if (fracOrder.size() > 1) {
            for (int i = 1; i < fracOrder.size(); i++) {
                int clockX = fracOrder.get(i-1).getLeft();
                int clockY = fracOrder.get(i).getLeft();
                int intDiff = J[clockX].lower - J[clockY].lower;

                // x -y < / <= d
                boolean isEq = fracOrder.get(i).getRight() == 1;
//                boolean isEq = ((D >> (i - 1)) & 1) == 1 ;
                if(isEq){
//                    res = res.andOp(new AtomConstraint(clockX,clockY, intDiff,false));
//                    res = res.andOp(new AtomConstraint(clockY,clockX,-intDiff,false));
                    res.update(clockX,clockY,intDiff);
                }
                else{
                    res = res.andOp(new AtomConstraint(clockX,clockY,intDiff,true));
                }

            }
        }
        return res;
    }

    @Override
    public Region clone() {
        Federation TOP = new Federation(this.getSpace().getDimension());
        TOP.setInit();
        TOP = TOP.andOp(this.fed);
        TupleSemantic newTuple = this.tuple.clone();
        Region res = new Region(this.space,TOP);
        res.tuple = newTuple;
        //TODO refactor
        return res;

    }
    public ClockSpace getSpace() {
        return space;
    }
    public void setSpace(ClockSpace space) {
        this.space = space;
    }
    public int getDimension() {
        return this.space.getDimension();
    }



    public Region reset(ArrayList<Integer> X){
        X.forEach(x ->
            this.fed.updateValue(x,0)
        );
        this.tuple.reset(X);
        return this;
    }

    public Region successor(){
        TupleSemantic succTuple = this.tuple.timeSuccessor();
        Region succ = new Region(succTuple);
        return succ;
    }

    public Expression toExpression() throws EPMCException {
        return UtilDBM.UDBMString2Expression(this.fed.toStr(this.getSpace().getVarNamesAccessor()),this.space.getModel());
    }
    @Override
    public String toString()  {
        try {
            return this.toExpression().toString();
        } catch (EPMCException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
    public String toStr()  {
        return this.fed.toStr(this.getSpace().getVarNamesAccessor());
    }
    public boolean isModelof(ClockConstraint g){
        return this.fed.le(g.getFed());
    }
    public boolean equals(Object o){
        Region R = (Region)o;
        return this.fed.eq(R.fed);
    }
}

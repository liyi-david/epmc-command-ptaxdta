package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */
import epmc.error.EPMCException;
import epmc.expression.Expression;
import java.util.*;
import epmc.modelchecker.Model;
import epmc.ptaxdta.pta.model.ClocksPTA;
import epmc.udbm.VarNamesAccessor;

public class ClockSpace {
    private int dimension = 0 ; // number of clocks
    private String[] clockName;
    private HashMap<String,Integer> clockOrder;
    private int [] boundary;
//    private ClocksPTA Clocks;

    // 0 for [0,0], 1 for (0,1),
    // 2 for [1,1], 3 for (1,2)
    // 2*cx for  [cx,cx], 2*cx +1  (cx,oo)

    private IntegerValueInterval [] interval;
    private int openCount = 0;
    private int [] choice;
    private boolean[] used;
    private ArrayList<Integer> permutation;
//    private ArrayList<Integer> D;
    private int s;
    private Model model;

    public Model getModel() {
        return model;
    }

    private ArrayList<Region> elements;

    public void setModel(Model model) {
        this.model = model;
    }

    public ClockSpace(ClocksPTA clocks) {
//        Clocks = clocks;
        this.dimension = clocks.clocknames.size() + 1;
        this.initClockNames(clocks.clocknames);
    }

    public ClockSpace(String []name, int [] bound, Model model){
        this.dimension = name.length + 1;
        this.boundary = new int [this.dimension];
        this.boundary[0] = 0;
        for (int i = 0; i < bound.length; i++) {
            this.boundary[i+1] = bound[i];
        }
        this.model     = model;
        List<String> nameList = Arrays.asList(name);
        this.initClockNames(new ArrayList<String>(nameList));
    }

    private void initClockNames(ArrayList<String> names) {
        this.clockName = new String[this.dimension];
        this.clockName[0] = "*";
        this.clockOrder = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            this.clockName[i+1] = names.get(i);
            this.clockOrder.put(names.get(i),i+1);
        }
    }
    public VarNamesAccessor getVarNamesAccessor(){
        VarNamesAccessor v = new VarNamesAccessor();
        for(int i = 0; i < this.clockName.length; i++){
            v.setClockName(i,this.clockName[i]);
        }
        return v;
    }

    public int getClockbyName(String name) {
        return this.clockOrder.get(name);
    }

    public int getDimension() {
        return this.dimension;
    }

    public String[] getClockName() {
        return clockName;
    }

    public static void main(String[] args) {
        ClockSpace space = new ClockSpace(new String[]{"x", "y","z"},new int []{2,2,2},null);
        space.explore();
    }
    public void explore(){
        this.interval    = new IntegerValueInterval [this.dimension];
        this.openCount   = 0;
        this.choice      = new int[this.dimension];
        this.permutation = new ArrayList<Integer>();
//        this.D           = new ArrayList<Integer>();
        this.elements    = new ArrayList<Region>();
        // TODO init all vars
        this.dfsInterval(1);
        System.out.print(this.elements.size() + "region elements explored");
    }
    private String debuginfo(){
        String res = "";
        for (int i = 0; i < this.getDimension(); i++) {
            res += this.clockName[i] + " : " + this.interval[i] + "\n";
        }

        if (this.permutation.size() != 0) {
            res += this.clockName[this.permutation.get(0)];
            for (int i = 1; i < this.permutation.size(); i++) {
                String symbol = (((this.s >> (i - 1)) & 1) == 1) ? "=" : "<" ;
//                String symbol = new String[]{"<","="}[this.getSymbol(i)];
                res += symbol + this.clockName[this.permutation.get(i)];
            }
            res += "\n";
        }
        return res;
    }
    private void generateNewRegion(){
        Region e = new Region(this,this.interval.clone(),this.permutation,this.s);
        this.elements.add(e);
        System.out.println("\n======\n" + this.elements.size());
        System.out.println(e);
        System.out.println(this.debuginfo());


//            System.out.println(e.toJSON());
//            System.out.println("e is instance of JsonValue : " + (e instanceof JsonValue));
//            JsonValue jobj = e.toJsonObject();
//            System.out.println(jobj);
//            System.out.println("jobj is instance of JsonValue : " + (jobj instanceof JsonValue));

//        Expression t = null;
//        try {
//            ArrayList<Integer> X = new ArrayList<>();
//            X.add(1);
//            t = e.toExpression();
//            System.out.println(t);
//
//            t = e.reset(X).toExpression();// TODO e.clone().reset(X)
//            System.out.println(t);
//
//        } catch (EPMCException e1) {
//            e1.printStackTrace();
//        }
    }

    private void enumerateSubsets(){
        if(this.permutation.size() <= 1) {
            this.s = 0;
            this.generateNewRegion();
            return;
        }
        int BOUND = 1 << (this.permutation.size() - 1);
//        int ALL   = BOUND - 1;
        for(this.s = 0; this.s < BOUND; this.s++){
            this.generateNewRegion();
        }
    }


    private void dfsPermutation(int step){ // step -> current permutation length
        if(step == this.openCount){
            this.enumerateSubsets();
        }
        else {

            for(int i=1; i<this.dimension; i++)
                if(!this.used[i] && (this.choice[i] % 2 == 1)){
                    this.used[i] = true;
                    this.permutation.add(i);
                    this.dfsPermutation(step+1);
                    this.permutation.remove(this.permutation.size()-1);
                    this.used[i] = false;
                }
        }
    }

    private void dfsInterval(int step){ // step -> clock[step]
        if (step == this.dimension){
//            if (this.openCount >=1) {
                this.used        = new boolean[this.dimension];
                this.dfsPermutation(0);
//            }
//            int e = new Region(this,this.interval,P,D);
//            this.elements.add(e);
        }
        else {
            int end = 2 * this.boundary[step] + 2;
            for(int i=0; i < end ; i++){
                int t = i / 2;
                this.interval[step] = (i == end - 1) ? new IntegerValueInterval(0,t,IntegerValueInterval.INF,0) :
                                      (i % 2 == 0)   ? new IntegerValueInterval(1, t, t, 1) :
                                                       new IntegerValueInterval(0, t,t+1,0);
                this.choice[step] = i;
                this.openCount += (i % 2);
                this.dfsInterval(step+1);
                this.openCount -= (i % 2);
            }
        }
    }
    public int findClockbyName(String x){
        for (int i = 0; i < this.dimension; i++) {
            if (this.clockName[i].equals(x)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public String toString() {
        String res = "";
        for(int i=0; i < this.dimension; i++){
            res += this.clockName[i] + " : " + this.interval[i] +"\n";
        }
        if(this.permutation.size()<1) return res;
        res += this.clockName[this.permutation.get(0)];
        for (int i=1; i < this.permutation.size(); i++)
            res += " <= " + this.clockName[this.permutation.get(i)];
        res += "\n";
        return res ;
    }
}

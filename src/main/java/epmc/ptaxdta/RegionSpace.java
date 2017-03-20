package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */
import java.util.*;
import epmc.udbm.*;

public class RegionSpace {
    private int demension = 0 ; // number of clocks
    private String[] clockName;
    private int [] boundary;
    // 0 for [0,0], 1 for (0,1),
    // 2 for [1,1], 3 for (1,2)
    // 2*cx for  [cx,cx], 2*cx +1  (cx,oo)

    private IntegerValueInterval [] interval;
    private int openCount = 0;
    private int [] choice;
    private boolean[] used;
    private ArrayList<Integer> permutation;
    private ArrayList<Integer> D;

    private ArrayList<RegionElement> elements;

    public RegionSpace(String []name,int [] bound){
        this.clockName = name;
        this.boundary  = bound;
        this.demension = this.clockName.length;
    }

    public int getDemension() {
        return demension;
    }

    public String[] getClockName() {
        return clockName;
    }

    public static void main(String[] args) {
        System.load("/Users/lijianlin/Projects/epmc/plugins/command-ptaxdta/src/main/java/epmc/udbm/libudm-swig.so");
        VarNamesAccessor name = new VarNamesAccessor();
        name.setClockName(0,"*");
        name.setClockName(1,"x");
        name.setClockName(2,"y");

        Constraint c1 = new Constraint(1,0,1,true),
                   c2 = new Constraint(2,1,0,false),
                   c3 = new Constraint(0,2,0,true);
        Federation f1 = new Federation(3,c1),
                   f2 = new Federation(3,c2),
                   f3 = new Federation(3,c2);
        Federation t123 = f1.andOp(f2).andOp(f3);
        System.out.println(t123.toStr(name));

        RegionSpace space = new RegionSpace(new String[]{"x", "y","z"},new int []{5,7,2});
        space.explore();
    }
    private void explore(){
        this.interval    = new IntegerValueInterval [this.demension];
        this.openCount   = 0;
        this.choice      = new int[this.demension];
        this.permutation = new ArrayList<Integer>();
        this.D           = new ArrayList<Integer>();
        this.elements    = new ArrayList<RegionElement>();
        // TODO init all vars
        this.dfsInterval(0);
        System.out.print(this.elements.size() + "region elements explored");
    }

    private void enumerateSubsets(){
        int BOUND = 1 << (this.permutation.size() - 1);
//        int ALL   = BOUND - 1;

        for(int s=0; s<BOUND; s++){
            RegionElement e = new RegionElement(this,this.interval,this.permutation,s);
            this.elements.add(e);
            System.out.println("\n======\n");
            System.out.println(e);
        }
    }

    private void dfsPermutation(int step){ // step -> current permutation length
        if(step == this.openCount){
            this.enumerateSubsets();
        }
        else {

            for(int i=0; i<this.demension; i++)
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
        if (step == this.demension){
//            if (this.openCount >=1) {
                this.used        = new boolean[this.demension];
                this.dfsPermutation(0);
//            }
//            int e = new RegionElement(this,this.interval,P,D);
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
    @Override
    public String toString() {
        String res = "";
        for(int i=0; i < this.demension; i++){
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

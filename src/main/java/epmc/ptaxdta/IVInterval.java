package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */

public class IVInterval implements Cloneable {

    private final static String [] LBRACK = {"(","["};
    private final static String [] RBRACK = {")","]"};
    private final static String COMMA = ",";

    public int lowerClosed,upperClosed;
    public int lower;
    public int upper;
    private String info;
    static public int INF = -1;



    public IVInterval(int lClosed, int lower, int upper, int uClosed) {
        assert (upper == IVInterval.INF) | (lower <= upper);
        //TODO deal with +INF
        this.lower = lower;
        this.upper = upper;
        this.lowerClosed = lClosed;
        this.upperClosed = uClosed;
        info = LBRACK[lClosed] + lower + COMMA + upper + RBRACK[uClosed];
    }

    public boolean isLowerClosed() {
        return lowerClosed == 1;
    }
    public boolean isUpperClosed() {
        return upperClosed == 1;
    }
    @Override
    public IVInterval clone() {
        return new IVInterval(this.lowerClosed,this.lower,this.upper,this.upperClosed);
    }
    public String getInfo() {
        return info;
    }

    public String toString() {
        return getInfo();
    }

    public boolean isPoint() {
        return (this.lowerClosed + this.upperClosed == 2);
    }
    public Integer pointValue() {
        return (this.isPoint()) ? this.lower : null;
    }

    public int length() {
        if (this.upper == IVInterval.INF)
            return IVInterval.INF;
        return this.upper - this.lower;
    }
    public IVInterval successor(int bound){
        if(this.upper == IVInterval.INF){
            return this.clone();
        }

        if(this.isPoint()){
            if(this.upper == bound){
                return new  IVInterval(0,this.upper,IVInterval.INF,0);
            }
            return new IVInterval(0,this.upper,this.upper+1,0);
        }
        else {
            return new IVInterval(1,this.upper,this.upper,1);
        }
    }
}


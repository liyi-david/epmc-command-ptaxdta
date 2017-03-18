package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/18.
 */

public class IntegerValueInterval {

    private final static String [] LBRACK = {"(","["};
    private final static String [] RBRACK = {")","]"};
    private final static String COMMA = ",";

    private int lowerClosed,upperClosed;
    private int lower;
    private int upper;
    private String info;
    static public int INF = -1;

    public IntegerValueInterval(int lClosed,int lower, int upper,int uClosed) {
        assert lower <= upper;
        this.lower = lower;
        this.upper = upper;
        this.lowerClosed = lClosed;
        this.upperClosed = uClosed;
        info = LBRACK[lClosed] + lower + COMMA + upper + RBRACK[uClosed];
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

    public int length() {
        if (this.upper == IntegerValueInterval.INF)
            return IntegerValueInterval.INF;
        return this.upper - this.lower;
    }
}


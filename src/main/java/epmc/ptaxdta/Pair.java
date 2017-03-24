package epmc.ptaxdta;

/**
 * Created by lijianlin on 17/3/24.
 */

public class Pair<L,R> implements Cloneable {

    public  L left;
    public  R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    public static Pair of(Integer left, Integer right) {
        Pair res = new Pair<Integer,Integer>(left,right);
        return res;
    }
//    @Override
//    public Pair<L,R> clone(){
//        return new Pair<L, R>(this.left.clone(),this.right.clone());
//    }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }

}
package epmc.ptaxdta.pta.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lijianlin on 17/4/6.
 */
public class APSet {
    private ArrayList<String> content;

    public APSet(String ...args) {
        this.content = new ArrayList<>(Arrays.asList(args));
        this.content.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                for (int i=0; i < Math.min(o1.length(),o2.length()); i++)
                    if (!(o1.charAt(i) == o2.charAt(i)))
                        return o1.charAt(i) - o2.charAt(i);
                return o1.length() - o2.length();
            }
        });
    }

    public APSet(ArrayList<String> content) {
        this.content = content;
        this.content.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                for (int i=0; i < Math.min(o1.length(),o2.length()); i++)
                    if (!(o1.charAt(i) == o2.charAt(i)))
                        return o1.charAt(i) - o2.charAt(i);
                return o1.length() - o2.length();
            }
        });
    }

    public boolean equals(Object a){
        APSet rhs = (APSet)a;
        if(this.content.size()!=rhs.content.size()){
            return false;
        }

        for (int i = 0; i < this.content.size(); i++) {
            if (!this.content.get(i).equals(rhs.content.get(i))) {
                return false;
            }
        }
        return true;
    }
    public String contentString(){
        return this.LabelWithSet((1<<this.content.size())-1).toString();
    }
    public LabelPTA LabelWithSet(int S){
        List<String> LabelList =
                IntStream.range(0,this.content.size())
                        .filter(i -> ((S >> i) & 1) > 0 )
                        .mapToObj(i -> this.content.get(i))
                        .collect(Collectors.toList());

//        String [] LabelArray = (String[]) LabelList.toArray();
        ArrayList<String> L = new ArrayList<String>(LabelList);
        return new LabelPTA(L);

    }
    public int size(){
        return this.content.size();
    }
    public int bound() {
        return 1 << this.size();
    }
    public int full() {
        return this.bound() - 1;
    }
    public int empty() {
        return 0; // TODO maybe useless, maybe static
    }
}

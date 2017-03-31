package epmc.ptaxdta.pta.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by lijianlin on 17/3/31.
 */
public class LabelPTA  implements ActionPTA {
    private ArrayList<String> content;

    public LabelPTA(String ...args) {
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
    public LabelPTA(ArrayList<String> content) {
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

    public boolean equals(ActionPTA a){
        LabelPTA rhs = (LabelPTA)a;
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
        String res = "{";
        res += this.content.get(0);
        for (int i = 1; i <this.content.size(); i++) {
            res += ", " + this.content.get(i);
        }
        res += "}";
        return res;
    }

}

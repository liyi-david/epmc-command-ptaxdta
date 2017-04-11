package epmc.ptaxdta.pta.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

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
    //TODO override hashCode to use as index
//    @Override
//    public int hashCode() {
//        return this.content.hashCode();
//    }
    @Override
    public boolean equals(ActionPTA a){
    	assert (a instanceof LabelPTA);
    	
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
    public String contentString() {
        Optional<String> res = this.content.stream()
                .reduce((r, e) -> r + ", " + e);
        if (!res.isPresent()) {
            return "{}";
        }
        return "{" + res.get() + "}";
    }
    @Override
    public String toString(){
        return this.contentString();
    }
}

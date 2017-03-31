package epmc.ptaxdta.pta.model;

/**
 * Created by lijianlin on 17/3/31.
 */
public class ActionStandardPTA implements ActionPTA {
    private String content;

    public ActionStandardPTA(String content) {
        this.content = content;
    }
    public String contentString(){
        return this.content;
    }

    public String getContent() {
        return content;
    }
    public boolean equals(ActionPTA a){
        ActionStandardPTA rhs = (ActionStandardPTA) a;
        return this.content.equals(rhs.content);
    }

}

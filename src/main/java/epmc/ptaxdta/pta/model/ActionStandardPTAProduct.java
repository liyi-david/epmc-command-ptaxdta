package epmc.ptaxdta.pta.model;

import epmc.ptaxdta.Region;

/**
 * Created by lijianlin on 17/4/5.
 */
public class ActionStandardPTAProduct implements ActionPTA {
    private String action;// ActionStandardPTA action
    private Region region;

    public ActionStandardPTAProduct(String action, Region region) {
        this.action = action;
        this.region = region;
    }

    public String contentString(){
            return this.action + this.region.toUDBMString();
        }

    public String getAction() {
        return action;
    }

    public Region getRegion() {
        return region;
    }

    public boolean equals(ActionPTA a){
        ActionStandardPTAProduct rhs = (ActionStandardPTAProduct) a;
        return this.action.equals(rhs.getAction()) &&
               this.region.equals(rhs.getRegion()) ;
    }

}

package epmc.command.util;

import com.sun.org.apache.xpath.internal.operations.Mod;
import epmc.error.EPMCException;
import epmc.jani.model.UtilModelParser;
import epmc.modelchecker.Model;
import epmc.modelchecker.UtilModelChecker;
import epmc.prism.model.convert.UtilPrismConverter;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.UtilDBM;
import epmc.ptaxdta.pta.model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lijianlin on 17/4/17.
 */
public class UtilTest {
    public static class TaskComplete {
        static public ArrayList<ModelPTA> generatePTA(Model model, int n) {

            ModelPTA pta = new ModelPTA("task_complete_" + n);

            pta.setContextValue(model.getContextValue());
            pta.actions.add(new ActionStandardPTA("i"));
            pta.clocks.clocknames.add("x");


            LocationPTA l[] = new LocationPTA[n + 1];
            l[0] = pta.initialLocations.addLocation(
                    pta.locations.addLocation(new LocationPTABasic("l0"))
            );
            for (int i = 1; i <= n; i++){ // !!!
                l[i] = pta.locations.addLocation(new LocationPTABasic("l" + i));
            }

            ClockSpace ptaspace = new ClockSpace(pta.clocks);
            ptaspace.setModel(model);
            pta.setSpace(ptaspace);

            ClockConstraint ptatop = ClockConstraint.TOP(ptaspace);

            Random r = new Random();
            int use[] = new int[30];
            int MIN = 5;
            int MAX = 20;
            int DIFF = MAX - MIN;

            for (int cnt = 0; cnt < n; cnt++){
                int  t = 0;
                do {
                    t = r.nextInt(DIFF);
                } while (use[t + MIN] > 2);
                use[t + MIN]++;
//                System.out.println(t + MIN);
            }
            int sum = 0;
            int start[] = new int[n];
            int end[] = new int[n];
            int idx = MIN;
            for (int i = 0; i <n ; i++) {
                while(use[idx]<=0) idx++;
                int len = r.nextInt(3) + 2;
                end[i] = idx;
                start[i] = idx - len;
                sum += end[i];
                System.out.println(start[i] + " " + end[i] + " " + len);
                pta.invariants.put(l[i],UtilDBM.UDBMString2CC("(x <= " + end[i] + ")", ptaspace));
                pta.label.put(l[i], new LabelPTA( i % 2 == 0 ? "alpha" : "beta"));
                ClockConstraint g = UtilDBM.UDBMString2CC("(" + start[i] +" <= x) && (x <= " + end[i] + ")", ptaspace);

                int lose_prob = r.nextInt(15) + 5;
                int win_prob  = 100 - lose_prob - 5 ;

                pta.addConnectionFrom(l[i], new ActionStandardPTA("i"), g)
                        .addTarget((double)lose_prob / 100, new ClocksPTA("x"), l[i])
                        .addTarget((double) win_prob / 100, new ClocksPTA("x"), l[i+1]);

                use[idx] --;
                idx += (use[idx] == 0) ? 1 : 0;
            }

            pta.invariants.put(l[n],ptatop);
            pta.label.put(l[n], new LabelPTA());
            ModelPTA dta = new ModelPTA("task_complete_prop_" + n);
            dta.setContextValue(model.getContextValue());
            dta.addLabels("alpha", "beta");
            dta.clocks.clocknames.add("y");
            dta.clocks.clocknames.add("z");
            ClockSpace dtaspace = new ClockSpace(dta.clocks);
            dtaspace.setModel(model);

            dta.setSpace(dtaspace);
            ClockConstraint dtatop = ClockConstraint.TOP(dtaspace);

            LocationPTA q[] = new LocationPTA[n + 2];
            q[0] = dta.initialLocations.addLocation(
                    dta.locations.addLocation(new LocationPTABasic("q0"))
            );
            dta.invariants.put(q[0],dtatop);
            for (int i = 1; i <= n + 1; i++){
                q[i] = dta.locations.addLocation(new LocationPTABasic("q" + i));
                dta.invariants.put(q[i],dtatop);
            }


            for (int i = 0; i < n ; i++) {
                ClockConstraint g;
                if(i ==0){
                    g = ClockConstraint.TOP(dtaspace);
                }
                else {
                    g = UtilDBM.UDBMString2CC("(y <= " + 3 * end[i-1] + ")", dtaspace);
                }
                String a = i % 2 == 0 ? "alpha" : "beta";
                dta.addConnectionFrom(q[i], new LabelPTA(a), g)
                        .addTarget(1, new ClocksPTA("y"), q[i+1]);

                dta.addConnectionFrom(q[i+1], new LabelPTA(a), dtatop)
                        .addTarget(1, new ClocksPTA(), q[i+1]);
            }
            ClockConstraint g = UtilDBM.UDBMString2CC("(y <= " + 3 * end[ n - 1 ] + ") && (z <= " + 3 * sum + ")", dtaspace);

            dta.addConnectionFrom(q[n], new LabelPTA(), g)
                .addTarget(1, new ClocksPTA("y"), q[n+1]);


//            pta.setAP(new APSet("alpha", "beta"));
//            dta.setAP(new APSet("alpha","beta","gamma"));
//            dta.setFinalLocation(q[n+1]);
            dta.addTrapLocation();
            dta.dtaflag = 1;
            ArrayList<ModelPTA> res = new ArrayList<>();
            res.add(pta);
            res.add(dta);
            return res;
        }
    }
    public static void main(Model model) throws EPMCException {
        for (int i = 5; i <= 20; i+= (i<19) ? 2 : 1) {
            System.out.println("==========  " + i + "  ==========");
            ArrayList<ModelPTA> res = TaskComplete.generatePTA(model,i);
            ModelPTA pta = res.get(0);
            ModelPTA dta = res.get(1);
            System.out.println(pta.toJani(null));
            System.out.println(dta.toJani(null));

            //            System.out.println(pta.toPrism());
//            System.out.println(UtilModelParser.prettyString(dta.toJani(null)));
//            System.out.println(dta.toPrism());
            UtilProductV2 util = new UtilProductV2();
            ModelPTA result = util.prod(pta,dta);

//FIXME            System.out.println(dta.isDTA());
            System.out.println(result.toJani(null));
            System.out.println(result.toPrism());
        }
    }
}

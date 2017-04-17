package epmc.command.util;

import epmc.error.EPMCException;
import epmc.modelchecker.Model;
import epmc.ptaxdta.ClockConstraint;
import epmc.ptaxdta.ClockSpace;
import epmc.ptaxdta.UtilDBM;
import epmc.ptaxdta.pta.model.*;

import java.util.Random;

/**
 * Created by lijianlin on 17/4/17.
 */
public class UtilTest {
    public static class TaskComplete {
        static public ModelPTA generatePTA(Model model, int n) {

            ModelPTA pta = new ModelPTA("task_complete_" + n);

            pta.setContextValue(model.getContextValue());
            pta.actions.add(new ActionStandardPTA("i"));
            pta.clocks.clocknames.add("x");


            LocationPTA l[] = new LocationPTA[n + 1];
            l[0] = pta.initialLocations.addLocation(
                    pta.locations.addLocation(new LocationPTABasic("l0"))
            );
            for (int i = 0; i <= n; i++){
                l[i] = pta.locations.addLocation(new LocationPTABasic("l" + i));
            }

            ClockSpace space = new ClockSpace(pta.clocks);
            space.setModel(model);
            pta.setSpace(space);

            ClockConstraint top = ClockConstraint.TOP(space);

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

            int start[] = new int[n];
            int end[] = new int[n];
            int idx = MIN;
            for (int i = 0; i <n ; i++) {
                while(use[idx]<=0) idx++;
                end[i] = idx;
                start[i] = idx - r.nextInt(3) - 1;
                pta.invariants.put(l[i],UtilDBM.UDBMString2CC("(x <= " + end[i] + ")", space));
                pta.label.put(l[i], new LabelPTA( i % 2 == 0 ? "alpha" : "beta"));
                ClockConstraint g = UtilDBM.UDBMString2CC("(" + start[i] +" <= x) && (x <= " + end[i] + ")", space);

                int lose_prob = r.nextInt(15) + 5;
                int win_prob  = 100 - lose_prob - 5 ;

                pta.addConnectionFrom(l[i], new ActionStandardPTA("i"), g)
                        .addTarget((double)lose_prob / 100, new ClocksPTA("x"), l[i])
                        .addTarget((double) win_prob / 100, new ClocksPTA("x"), l[i+1]);

                use[idx] --;
                idx += (use[idx] == 0) ? 1 : 0;
            }

            pta.invariants.put(l[n], top);
            pta.label.put(l[n], new LabelPTA());

//            pta.setAP(new APSet("alpha", "beta"));

            return pta;
        }
    }
    public static void main(Model model) throws EPMCException {
        for (int i = 6; i <= 20; i+= 2) {
            ModelPTA pta = TaskComplete.generatePTA(model,i);
            System.out.println(pta.toPrism());
        }
    }
}

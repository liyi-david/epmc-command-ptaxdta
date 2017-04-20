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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lijianlin on 17/4/17.
 */
public class UtilTest {
    public static class TaskComplete {
        static public ArrayList<ModelPTA> generate(Model model, int n) {

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
            int MIN = 3;
            int MAX = 10;
            int DIFF = MAX - MIN;

            for (int cnt = 0; cnt < n; cnt++){
                int  t = 0;
                do {
                    t = r.nextInt(DIFF + 1);
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
                int len = r.nextInt(3) + 1;
                end[i] = idx;
                start[i] = idx - len;
                sum += end[i];
//                System.out.println(start[i] + " " + end[i] + " " + len);
                pta.invariants.put(l[i],UtilDBM.UDBMString2CC("(x <= 3)", ptaspace));
                pta.label.put(l[i], new LabelPTA( i % 2 == 0 ? "alpha" : "beta"));
                ClockConstraint g = UtilDBM.UDBMString2CC("(2 <= x) && (x <= 3)", ptaspace);

                int lose_prob = r.nextInt(15) + 5;
                int win_prob  = 100 - lose_prob;

                pta.addConnectionFrom(l[i], new ActionStandardPTA("i"), g)
                        .addTarget((double)lose_prob / 100, new ClocksPTA("x"), l[i])
                        .addTarget((double) win_prob / 100, new ClocksPTA("x"), l[i+1]);

                use[idx] --;
                idx += (use[idx] == 0) ? 1 : 0;
            }

            pta.invariants.put(l[n],ptatop);
            pta.label.put(l[n], new LabelPTA());

//            ClockConstraint gg = UtilDBM.UDBMString2CC("(" + start[0] +" <= x) && (x <= " + end[0] + ")", ptaspace);
//            pta.addConnectionFrom(l[n],new ActionStandardPTA("i"),gg)
//                    .addTarget(1,new ClocksPTA("x"),l[n]);


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
                    g = UtilDBM.UDBMString2CC("(y <= 9)", dtaspace);
                }
                String a = i % 2 == 0 ? "alpha" : "beta";
                dta.addConnectionFrom(q[i], new LabelPTA(a), g)
                        .addTarget(1, new ClocksPTA("y"), q[i+1]);

                dta.addConnectionFrom(q[i+1], new LabelPTA(a), dtatop)
                        .addTarget(1, new ClocksPTA(), q[i+1]);
            }
            ClockConstraint g = UtilDBM.UDBMString2CC("(y <= 9) && (z <= " + (9 * n) +  ")", dtaspace);

            dta.addConnectionFrom(q[n], new LabelPTA(), g)
                .addTarget(1, new ClocksPTA("y"), q[n+1]);


//            pta.setAP(new APSet("alpha", "beta"));
//            dta.setAP(new APSet("alpha","beta","gamma"));
//            dta.setFinalLocation(q[n+1]); WRONG
            dta.addTrapLocation();
            dta.dtaflag = 1;
            ArrayList<ModelPTA> res = new ArrayList<>();
            res.add(pta);
            res.add(dta);
            return res;
        }
        public static void main(Model model) throws EPMCException {
            for (int i = 5; i <= 20; i+= (i<19) ? 2 : 1) {
                System.out.println("==========  " + i + "  ==========");
                ArrayList<ModelPTA> res = TaskComplete.generate(model,i);
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
                System.out.println(result.toSingleJani(null));
                System.out.println(result.toPrism());
            }
        }
    }
    public static class RobotNavigate {
        static void dfs(int x,int y,int n,boolean vis[][],int [][] map){
            int [] dx = new int[] {-1,0,1,0};
            int [] dy = new int[] {0,-1,0,1};
            vis[x][y] = true;
//            if ((x == n -1 ) && (y == n -1 )) return ;
            for (int i = 0; (i < 4); i++) {
//                System.out.println(s);
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (( 0 <= nx) && (nx < n) &&
                    ( 0 <= ny) && (ny < n) &&
                    ( map[nx][ny] != 3)    &&
                    (!vis[nx][ny])){
                    dfs(nx,ny,n,vis,map);
                }
            }
        }

        static public int[][] maze(int n){
            int [][] map = new int[n][n];
            // 0 for {}
            // 1 for {alpha}
            // 2 for {beta}
            // 3 for block

            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++) {
                    map[i][j] = ( i + j ) % 2 == 0 ? 1 : 2;
                }
            }
            map[ n - 1 ][ n - 1 ] = 0;
            Random r = new Random();
            for (int i = 0; i < n; i++){ // TODO n or n-1
                int x, y;
                boolean connected = true;
                do {
                    x = r.nextInt(n);
                    y = r.nextInt(n);
//                    System.out.println(x + "," + y);
                    if(((map[x][y] == 1) || (map[x][y] == 2)) &&
                        (!((x == 0) && (y == 0)) )){
                        boolean[][] vis = new boolean[n][n];
                        int old = map[x][y];
                        map[x][y] = 3;
                        int cnt = 0;
                        for (int nx = 0; nx < n; nx++) {
                            for (int ny = 0; ny < n; ny++) {
                                if ((map[nx][ny] != 3) && (!vis[nx][ny])) {
                                    dfs(nx,ny,n,vis,map);
                                    cnt ++;
                                }
                            }
                        }
                        connected = cnt == 1;
                        map[x][y] = old;
//                        System.out.println("cnt = " + cnt);
                    }

                } while ((map[x][y] == 0)       ||
                         (map[x][y] == 3)       ||
                         ((x == 0) && (y == 0)) ||
                         (!connected)           ||
//                         (x - y == 1)         ||
                         ((n >= 5) && (x * y == 0) || (x == n-1) || (y == n-1)) );// TODO not in edge
                map[x][y] = 3;
            }
            //TODO check reachability
            return map;
        }
        static public ModelPTA DTA(Model model,int n) {
            ModelPTA dta = new ModelPTA("Navigation_prop");

            dta.setContextValue(model.getContextValue());
            dta.addLabels("alpha", "beta");
            dta.clocks.clocknames.add("y");
            dta.clocks.clocknames.add("z");

            LocationPTA q0 = dta.initialLocations.addLocation(
                    dta.locations.addLocation(new LocationPTABasic("q0"))
            );
            LocationPTA q1 = dta.locations.addLocation(new LocationPTABasic("q1"));
            LocationPTA q2 = dta.locations.addLocation(new LocationPTABasic("q2"));
            LocationPTA q3 = dta.locations.addLocation(new LocationPTABasic("q3"));

            ClockSpace space = new ClockSpace(dta.clocks);
            space.setModel(model);

            dta.setSpace(space);
            ClockConstraint top = ClockConstraint.TOP(space);

            dta.invariants.put(q0,top);
            dta.invariants.put(q1,top);
            dta.invariants.put(q2,top);
            dta.invariants.put(q3,top);

            ClockConstraint g_y = UtilDBM.UDBMString2CC("(y <= " + 24 + ")", space);
//            ClockConstraint g_z = UtilDBM.UDBMString2CC("(z <= " + (15 * n) + ")", space);

            ClockConstraint g = UtilDBM.UDBMString2CC("(y <= " + 24 + ") && ( z <=" + (15 * n) + ")", space);


            dta.addConnectionFrom(q0, new LabelPTA("alpha"), top)
                    .addTarget(1, new ClocksPTA("y"), q1);
            dta.addConnectionFrom(q0, new LabelPTA("beta"), top)
                    .addTarget(1, new ClocksPTA("y"), q1);

            dta.addConnectionFrom(q1, new LabelPTA("alpha"), g_y)
                    .addTarget(1, new ClocksPTA("y"), q1);
            dta.addConnectionFrom(q1, new LabelPTA("beta"), g_y)
                    .addTarget(1, new ClocksPTA("y"), q2);
            dta.addConnectionFrom(q1, new LabelPTA(), g)
                    .addTarget(1, new ClocksPTA(), q3);

            dta.addConnectionFrom(q2, new LabelPTA("beta"), g_y)
                    .addTarget(1, new ClocksPTA("y"), q1);
            dta.addConnectionFrom(q2, new LabelPTA("alpha"), g_y)
                    .addTarget(1, new ClocksPTA("y"), q2);
            dta.addConnectionFrom(q2, new LabelPTA(), g)
                    .addTarget(1, new ClocksPTA(), q3);

//            dta.setFinalLocation(q3); WRONG
            dta.addTrapLocation();
            dta.dtaflag = 1;
//            dta.setAP(new APSet("alpha","beta","gamma"));


            return dta;
        }
        static public ArrayList<ModelPTA> generate(int[][] map,Model model, int n,int lbound, int ubound) {

            ModelPTA pta = new ModelPTA("Navigation");

            pta.setContextValue(model.getContextValue());
            pta.actions.add(new ActionStandardPTA("i"));
            pta.clocks.clocknames.add("x");

            ClockSpace ptaspace = new ClockSpace(pta.clocks);
            ptaspace.setModel(model);

            pta.setSpace(ptaspace);

            ClockConstraint top = ClockConstraint.TOP(ptaspace);


            ClockConstraint g = UtilDBM.UDBMString2CC("(" + lbound +" <= x) && (x <= " + ubound + ")", ptaspace);
            ClockConstraint inv = UtilDBM.UDBMString2CC("(x <= " + ubound + ")", ptaspace);

            LocationPTA[][] l = new LocationPTA[n][n];

            int [] dx = new int[] {-1,0,1,0};
            int [] dy = new int[] {0,-1,0,1};
            int [][] cnt = new int[n][n];
            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++) {
                    if (map[i][j] != 3){
                        l[i][j] = pta.locations.addLocation(new LocationPTABasic("l-" + i + "-" + j));
                        pta.invariants.put(l[i][j],inv);
                        pta.label.put(l[i][j], map[i][j] == 0 ? new LabelPTA() :
                                               map[i][j] == 1 ? new LabelPTA("alpha") :
                                                                new LabelPTA(("beta")));

                        cnt[i][j] = 0;
                        for (int k = 0; k < 4; k++) {
                            int x = i + dx[k];
                            int y = j + dy[k];
                            if ((0 <= x) && (x < n) &&
                                    (0 <= y) && (y < n) &&
                                    map[x][y] != 3) {
                                cnt[i][j]++;
                            }

                        }
                    }
                }
            }
            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++) {
                    if (map[i][j] != 3){
                        if (cnt[i][j] > 0){

                            TransitionPTA tran = pta.addConnectionFrom(l[i][j], new ActionStandardPTA("i"), g);
                            tran.addTarget(0.1,new ClocksPTA("x"),l[i][j]);

                            int sum = 90;
                            int prob = 90 / cnt[i][j];
                            int rest = 90 % cnt[i][j];

                            for (int k = 0; k < 4; k++) {
                                int x = i + dx[k];
                                int y = j + dy[k];
                                if (( 0 <= x) && (x < n) &&
                                        ( 0 <= y) && (y < n) &&
                                        map[x][y] != 3) {
                                    cnt[i][j] --;
                                    int t = cnt[i][j] == 0 ? prob + rest : prob;
                                    tran.addTarget(t / 100.00,new ClocksPTA("x"),l[x][y]); //TODO prob
                                }
                            }
                        }

                    }
                }
            }

            pta.initialLocations.addLocation(l[0][0]);

//            System.out.println(pta.toJani(null));
            ArrayList<ModelPTA> res = new ArrayList<>();
            res.add(pta);
            ModelPTA dta = DTA(model,n);
            res.add(dta);
            return res;
        }
        public static void main(Model model) throws EPMCException {
            try {
//                File file = new File("");
//                file.createNewFile();
                FileWriter writer = new FileWriter("/Users/lijianlin/RobotNaviLog.txt");
//                writer.write("This\n is\n an\n example\n");
                for (int n = 5; n < 20; n += 2) {
                    System.out.println("==========" + n + "==========");
                    writer.write("==========" + n + "==========");
                    writer.write("\n");

                    int [][] map = RobotNavigate.maze(n);

                    for (int i = 0; i < n; i++){
                        for (int j = 0; j < n; j++) {
                            System.out.print(map[i][j] + " ");
                            writer.write(map[i][j] + " ");
                        }
                        System.out.println();
                        writer.write("\n");
                    }
                    System.out.println();
                    writer.write("\n");

                    ArrayList<ModelPTA> res = RobotNavigate.generate(map,model,n,2,3);
                    ModelPTA pta = res.get(0);
                    ModelPTA dta = res.get(1);
//                System.out.println(pta.toPrism());
//                    System.out.println(pta.toJani(null));
//                    System.out.println(dta.toJani(null));
                    writer.write(pta.toJani(null).toString() + "\n");
                    writer.write(dta.toJani(null).toString() + "\n");
                    //            System.out.println(pta.toPrism());
//            System.out.println(UtilModelParser.prettyString(dta.toJani(null)));
//            System.out.println(dta.toPrism());
                    UtilProductV2 util = new UtilProductV2();
                    ModelPTA result = util.prod(pta,dta);
                    int num = result.locations.getLocations().size();
                    System.out.println(num + " locations");
                    writer.write(num + "locations\n");
//FIXME            System.out.println(dta.isDTA());
//                    System.out.println(result.toJani(null));
//                    System.out.println(result.toSingleJani(null));
//                    System.out.println(result.toPrism());
                    writer.write(result.toJani(null).toString() + "\n");
//                    writer.write(result.toSingleJani(null).toString() + "\n");
                    writer.write(result.toPrism().toString() + "\n");

                }
                writer.flush();
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

    }
    public static void main(Model model) throws EPMCException {
        RobotNavigate.main(model);
//        TaskComplete.main(model);
    }
}

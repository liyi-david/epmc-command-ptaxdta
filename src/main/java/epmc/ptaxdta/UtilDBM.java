package epmc.ptaxdta;

import epmc.command.util.UtilNative;
import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.expression.standard.ExpressionIdentifierStandard;
import epmc.expression.standard.ExpressionLiteral;
import epmc.expression.standard.ExpressionOperator;
import epmc.jani.model.ModelJANI;
import epmc.modelchecker.Model;
import epmc.udbm.AtomConstraint;
import epmc.udbm.Federation;
import epmc.value.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lijianlin on 17/3/30.
 */
public class UtilDBM {

    public static void LoadUDBM() {
    	try {
			UtilNative.loadLibraryFromJar("/epmc/udbm/udbm_int.so");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    //TODO use Javacc when refactoring
    public static Federation UDBMString2Federation(String f, ClockSpace space) {

//        System.out.println("\n\nbefore : " + f);
        String conjunctions[] = f.split("\\|\\||$");
        Federation res = new Federation(space.getDimension());
//        res.setInit();

        for (String conj : conjunctions) {
            String inequalities[] = conj.replace("(","").replace(")","").replace(" ","").split("&&");
            Federation inequs = new Federation(space.getDimension());
            inequs.setInit();
            for (String inequ : inequalities) {
                if(inequ.equals("true")){
                    Federation top = new Federation(space.getDimension());
                    top.setInit();
                    inequs = inequs.andOp(top);//TODO do not need & True
                    continue;
                }

                if(inequ.equals("false")){
                    Federation bot = new Federation(space.getDimension());
                    inequs = inequs.andOp(bot);
                    continue;
                }
                String op_sym = null;
                String identifier = null;
                boolean isStric = false;
                if(inequ.contains("==")){
//                    System.out.println("equ");
                    op_sym = "==";
                    isStric = false;
                    identifier = OperatorEq.IDENTIFIER;
                }
                else if(inequ.contains("<=")){
//                    System.out.println("le");
                    isStric = false;
                    op_sym = "<=";
                    identifier = OperatorLe.IDENTIFIER;
                }
                else if(inequ.contains("<")){
//                    System.out.println("lt");
                    isStric = true;
                    op_sym = "<";
                    identifier = OperatorLt.IDENTIFIER;
                }

                Federation cur = new Federation(space.getDimension());
                cur.setInit();

                String oprands[] = inequ.split(op_sym);
                int d_idx = -1,d = -1;
                boolean isVar[] = {false,false};
                for (int i = 0; i < 2; i++) {
                    try {
//                        isVar[i] = false;
                        d = Integer.parseInt(oprands[i]);
                        d_idx = i;
                        break;
                    } catch (NumberFormatException e) {
                        isVar[i] = true;
                    }
                }
                int x_idx = 1 - d_idx;
                if(isVar[0] && isVar[1]){
                    int x0 = space.findClockbyName(oprands[0]);
                    int x1 = space.findClockbyName(oprands[1]);

                    if(op_sym.equals("==")){
                        // x ==  y
                        AtomConstraint g1 = new AtomConstraint(x0,x1,0,false);
                        AtomConstraint g2 = new AtomConstraint(x1,x0,0,false);
                        Federation temp = new Federation(space.getDimension());
                        temp.setInit();
                        temp = temp.andOp(g1).andOp(g2);
//                        System.out.println(" == : " + temp.toStr(space.getVarNamesAccessor()));
                        cur = cur.andOp(g1).andOp(g2);
                    }
                    else {
                        // x < / <= y
                        cur = cur.andOp(new AtomConstraint(x0,x1,0,isStric));
                    }
                    inequs = inequs.andOp(cur);
                }
                else {
                    int x0 = 0, x1 = 0;

                    if(oprands[x_idx].contains("-")){
                        String vars[] = oprands[x_idx].split("-");
                        x0 = space.getClockbyName(vars[0]);
                        x1 = space.getClockbyName(vars[1]);
                    }
                    else {
                        x0 = space.getClockbyName(oprands[x_idx]);
                        x1 = 0;
                    }
                    if(d_idx == 1){
                        // x ( - y | 0) < / <= d
                        cur = cur.andOp(new AtomConstraint(x0,x1,d,isStric));
                        if (op_sym == "=="){
                            // x (- y | 0) == d
                            cur = cur.andOp(new AtomConstraint(x1,x0,-d,isStric));
                        }
                    }
                    else { // d_idx = 0
                        // d < / <= x ( - y | 0)
                        cur = cur.andOp(new AtomConstraint(x1,x0,-d,isStric));
                        if (op_sym == "=="){
                            // d == x ( - y | 0)
                            cur = cur.andOp(new AtomConstraint(x0,x1,d,isStric));
                        }
                    }
                    inequs = inequs.andOp(cur);


                }
            }
            res = res.orOp(inequs);
//            System.out.println("res.orOp(inequs) : " + res.toStr(space.getVarNamesAccessor()));

        }
//        System.out.println("after : " + res.toStr(space.getVarNamesAccessor()));
//        assert f.equals(res.toStr(space.getVarNamesAccessor()));
        return res;
    }

    public static Expression UDBMString2Expression(String f, Model model) throws EPMCException {
//        System.out.println(f);
        String conjunctions[] = f.split("\\|\\||$");
        ArrayList<Expression> conjExps = new ArrayList<>();
        for (String conj : conjunctions) {
            String inequalities[] = conj.replace("(","").replace(")","").replace(" ","").split("&&");
            ArrayList<Expression> inequExps = new ArrayList<>();
            for (String inequ : inequalities) {
                if(inequ.equals("true")){
                    Expression top = new ExpressionLiteral.Builder()
				        .setValue(
						UtilValue.newValue(
								TypeBoolean.get(model.getContextValue()),
								"true")
				        )
				        .build();
                    inequExps.add(top);
                    continue;
                }

                if(inequ.equals("false")){
                    Expression bot = new ExpressionLiteral.Builder()
                            .setValue(
                                    UtilValue.newValue(
                                            TypeBoolean.get(model.getContextValue()),
                                            "false")
                            )
                            .build();
                    inequExps.add(bot);
                    continue;
                }
                String op_sym = null;
                String identifier = null;
                if(inequ.contains("==")){
//                    System.out.println("equ");
                    op_sym = "==";
                    identifier = OperatorEq.IDENTIFIER;
                }
                else if(inequ.contains("<=")){
//                    System.out.println("le");
                    op_sym = "<=";
                    identifier = OperatorLe.IDENTIFIER;
                }
                else if(inequ.contains("<")){
//                    System.out.println("lt");
                    op_sym = "<";
                    identifier = OperatorLt.IDENTIFIER;
                }


                String oprands[] = inequ.split(op_sym);
                int d_idx = -1,d = -1;
                boolean isVar[] = {false,false};
                for (int i = 0; i < 2; i++) {
                    try {
//                        isVar[i] = false;
                        d = Integer.parseInt(oprands[i]);
                        d_idx = i;
                        break;
                    } catch (NumberFormatException e) {
                        isVar[i] = true;
                    }
                }
                int x_idx = 1 - d_idx;
                if(isVar[0] && isVar[1]){
                    Expression x0 = new ExpressionIdentifierStandard.Builder()
                            .setName(oprands[0])
                            .build();
                    Expression x1 = new ExpressionIdentifierStandard.Builder()
                            .setName(oprands[1])
                            .build();
                    Expression equ = new ExpressionOperator.Builder()
                            .setOperator(model.getContextValue().getOperator(identifier))
                            .setOperands(x0,x1)
                            .build();
                    // x0 ~ x1
                    inequExps.add(equ);
                }
                else {
                    Expression c = new ExpressionLiteral.Builder()
                            .setValue(UtilValue.newValue(TypeInteger.get(model.getContextValue()),
                                    Integer.toString(d)))
                            .build();
                    Expression x = null;

                    if(oprands[x_idx].contains("-")){
                        String vars[] = oprands[x_idx].split("-");
                        Expression x0 = new ExpressionIdentifierStandard.Builder()
                                .setName(vars[0])
                                .build();
                        Expression x1 = new ExpressionIdentifierStandard.Builder()
                                .setName(vars[1])
                                .build();
                        x = new ExpressionOperator.Builder()
                                .setOperator(model.getContextValue().getOperator(OperatorSubtract.IDENTIFIER))
                                .setOperands(x0,x1)
                                .build();
                        // x ::= x1 - x2
                    }
                    else {
                        x = new ExpressionIdentifierStandard.Builder()
                                .setName(oprands[x_idx])
                                .build();
                        // x ::= x
                    }
                    Expression operandExp[] = new Expression[2];
                    operandExp[x_idx] = x;
                    operandExp[d_idx] = c;

                    Expression ine = new ExpressionOperator.Builder()
                            .setOperator(model.getContextValue().getOperator(identifier))
                            .setOperands(operandExp)
                            .build();
                    // x ~ c or c ~ x (x ::= x | x1 - x2)
                    inequExps.add(ine);
                }
            }
//            Expression conjExp = null;
//            if(inequExps.size() > 1) {
//                conjExp = new ExpressionOperator.Builder()
//                        .setOperator(model.getContextValue().getOperator(OperatorAnd.IDENTIFIER)) //TODO linearized
//                        .setOperands(inequExps)
//                        .build();
//            }
//            else {
//                conjExp = inequExps.get(0);
//            }
            Expression conjExp = UtilDBM.List2Tree(model,inequExps,OperatorAnd.IDENTIFIER);
//            System.out.println(conjExp);
            conjExps.add(conjExp);

        }
//        Expression res = null;
//        if(conjExps.size() > 1){
//            res = new ExpressionOperator.Builder()
//                .setOperator(model.getContextValue().getOperator(OperatorOr.IDENTIFIER)) //TODO linearized
//                .setOperands(conjExps)
//                .build();
//
//        }
//        else {
//            res = conjExps.get(0);
//        }
        Expression res = UtilDBM.List2Tree(model,conjExps,OperatorOr.IDENTIFIER);
        return res;
    }
    static public Expression List2Tree(Model model,ArrayList<Expression> list, String identifier){
        if(list.size() == 0) {
            return  null;
        }

        ArrayList<Expression> rest = (ArrayList<Expression>) list.clone();
        Expression cur = rest.get(0);
        rest.remove(0);

        if(list.size() == 1 ) {
            return cur;
        }
        else {
            Expression next = UtilDBM.List2Tree(model,rest,identifier);
            Expression res = new ExpressionOperator.Builder()
                    .setOperator(model.getContextValue().getOperator(identifier))
                    .setOperands(cur,next)
                    .build();
            return res;
        }
    }
    public static void main(String[] args) {


    }
}


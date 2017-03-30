package epmc.ptaxdta;

import epmc.error.EPMCException;
import epmc.expression.Expression;
import epmc.expression.standard.ExpressionIdentifierStandard;
import epmc.expression.standard.ExpressionLiteral;
import epmc.expression.standard.ExpressionOperator;
import epmc.jani.model.ModelJANI;
import epmc.modelchecker.Model;
import epmc.value.*;

import java.util.ArrayList;

/**
 * Created by lijianlin on 17/3/30.
 */
public class UtilDBM {

    public static void LoadUDBM() {
        System.load("/Users/lijianlin/Projects/epmc/plugins/command-ptaxdta/src/main/java/epmc/udbm/udbm_int.so");
    }

    public static Expression UDBMString2Expression(String f, Model model) throws EPMCException {
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
                    }
                    else {
                        x = new ExpressionIdentifierStandard.Builder()
                                .setName(oprands[x_idx])
                                .build();
                    }
                    Expression operandExp[] = new Expression[2];
                    operandExp[x_idx] = x;
                    operandExp[d_idx] = c;

                    Expression ine = new ExpressionOperator.Builder()
                            .setOperator(model.getContextValue().getOperator(identifier))
                            .setOperands(operandExp)
                            .build();
                    inequExps.add(ine);
                }
            }
            Expression conjExp = null;
            if(inequExps.size() > 1) {
                conjExp = new ExpressionOperator.Builder()
                        .setOperator(model.getContextValue().getOperator(OperatorAnd.IDENTIFIER)) //TODO linearized
                        .setOperands(inequExps)
                        .build();
            }
            else {
                conjExp = inequExps.get(0);
            }

//            System.out.println(conjExp);
            conjExps.add(conjExp);

        }
        Expression res = null;
        if(conjExps.size() > 1){
            res = new ExpressionOperator.Builder()
                .setOperator(model.getContextValue().getOperator(OperatorOr.IDENTIFIER)) //TODO linearized
                .setOperands(conjExps)
                .build();

        }
        else {
            res = conjExps.get(0);
        }
        return res;
    }
    public static void main(String[] args) {


    }
}


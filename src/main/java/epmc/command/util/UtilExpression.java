package epmc.command.util;

import epmc.expression.Expression;
import epmc.expression.standard.ExpressionIdentifier;
import epmc.expression.standard.ExpressionLiteral;
import epmc.expression.standard.ExpressionOperator;
import epmc.value.OperatorAnd;
import epmc.value.OperatorNot;
import epmc.value.OperatorOr;
import epmc.value.Value;
import epmc.value.ValueBoolean;

public class UtilExpression {
	
	public static boolean isFalse(Expression expression) {
        assert expression != null;
        if (!ExpressionLiteral.isLiteral(expression)) {
            return false;
        }
        ExpressionLiteral expressionLiteral = ExpressionLiteral.asLiteral(expression);
        return ValueBoolean.isFalse(getValue(expressionLiteral));
    }

    public static boolean isNot(Expression expression) {
        if (!(expression instanceof ExpressionOperator)) {
            return false;
        }
        ExpressionOperator expressionOperator = (ExpressionOperator) expression;
        return expressionOperator.getOperator()
                .getIdentifier()
                .equals(OperatorNot.IDENTIFIER);
    }

    public static boolean isOr(Expression expression) {
        return isOperator(expression, OperatorOr.IDENTIFIER);
    }
    
    private static boolean isOperator(Expression expression, String name) {
        if (!(expression instanceof ExpressionOperator)) {
            return false;
        }
        ExpressionOperator expressionOperator = (ExpressionOperator) expression;
        return expressionOperator.getOperator()
                .getIdentifier()
                .equals(name);
    }
    
    public static boolean isAnd(Expression expression) {
        return isOperator(expression, OperatorAnd.IDENTIFIER);
    }
    
    public static boolean isTrue(Expression expression) {
        assert expression != null;
        if (!ExpressionLiteral.isLiteral(expression)) {
            return false;
        }
        ExpressionLiteral expressionLiteral = ExpressionLiteral.asLiteral(expression);
        return ValueBoolean.isTrue(getValue(expressionLiteral));
    }
    
    private static Value getValue(Expression expression) {
        assert expression != null;
        assert ExpressionLiteral.isLiteral(expression);
        ExpressionLiteral expressionLiteral = ExpressionLiteral.asLiteral(expression);
        return expressionLiteral.getValue();
    }
    
    public static boolean isAction(Expression expression) {
    	return ExpressionIdentifier.isIdentifier(expression);
    }
}

package co.mgentertainment.common.indicator.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author larry
 * @createTime 2023/9/30
 * @description SpelExpressionResolver
 */
public class SpelExpressionResolver {

    public static String parseSpel(String spelExpression, String[] argNames, Object[] argVals) {
        if (!StringUtils.contains(spelExpression, "#")) {
            return spelExpression;
        }
        ExpressionParser parser = new SpelExpressionParser();
        // 解析后的SPEL
        Expression expression = parser.parseExpression(spelExpression);
        // Spring表达式上下文
        EvaluationContext context = new StandardEvaluationContext();
        // 添加方法入参到上下文
        for (int i = 0; i < argNames.length; i++) {
            context.setVariable(argNames[i], argVals[i]);
        }
        return expression.getValue(context, String.class);
    }
}

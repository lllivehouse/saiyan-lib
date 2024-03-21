package co.saiyan.common.indicator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author larry
 * @createTime 2023/9/30
 * @description SpelExpressionResolver
 */
@Slf4j
public class SpelExpressionResolver {

    public static String parseSpel(String spelExpression, String[] argNames, Object[] argVals, ApplicationContext applicationContext) {
//        if (!StringUtils.contains(spelExpression, "#")) {
//            return spelExpression;
//        }
//        if (argNames == null || argNames.length == 0 || argVals == null || argVals.length == 0) {
//            return null;
//        }
        ExpressionParser parser = new SpelExpressionParser();
        try {
            // 解析后的SPEL
            Expression expression = parser.parseExpression(spelExpression);
            // Spring表达式上下文
            StandardEvaluationContext context = new StandardEvaluationContext();
            // 设置BeanResolver
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
            // 添加方法入参到上下文
            for (int i = 0; i < argNames.length; i++) {
                context.setVariable(argNames[i], argVals[i]);
            }
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            log.error("解析SPEL表达式:{}异常", spelExpression, e);
            return null;
        }
    }
}

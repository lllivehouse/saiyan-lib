package co.saiyan.common.schedulerplus.support;

import co.saiyan.common.schedulerplus.strategy.RunStrategyEnum;
import co.saiyan.common.utils.CronExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description CronUtils
 */
public class StrategyValueUtils {

    public static boolean isStrategyValueInvalidOrExpired(RunStrategyEnum runStrategy, String strategyValue) {
        if (runStrategy == null || StringUtils.isBlank(strategyValue)) {
            return true;
        }
        switch (runStrategy) {
            case ONE_TIME:
                return !StringUtils.isNumeric(strategyValue) || new Date(Long.valueOf(strategyValue)).before(new Date());
            case CRON_EXPRESSION:
                return !CronExpression.isValidExpression(strategyValue)
                        || CronExpression.getNextExecution(strategyValue) == null
                        || CronExpression.getNextExecution(strategyValue).before(new Date());
            default:
                return true;
        }
    }
}

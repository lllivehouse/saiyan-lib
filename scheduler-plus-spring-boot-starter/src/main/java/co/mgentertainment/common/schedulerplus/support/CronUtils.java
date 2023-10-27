package co.mgentertainment.common.schedulerplus.support;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description CronUtils
 */
public class CronUtils {

    public static boolean isCronInvalidOrExpired(String cron) {
        boolean isValid = CronSequenceGenerator.isValidExpression(cron);
        if (!isValid) {
            return true;
        }
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        return cronSequenceGenerator.next(new Date()).before(new Date());
    }
}

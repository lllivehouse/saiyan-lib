package co.saiyan.common.schedulerplus.strengthen;

import co.saiyan.common.model.R;

import java.lang.reflect.Method;

public interface SchedulerPlusStrength {

    /**
     * 前置强化方法
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     * @return 返回值 日志表主键id
     */
    R<Long> before(Object bean, Method method, Object[] args);

    /**
     * 异常强化方法
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     * @param id     日志表主键id
     */
    void exception(Object bean, Method method, Object[] args, Long id);

    /**
     * Finally强化方法，出现异常也会执行
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     * @param result 任务执行结果
     * @param id     日志表主键id
     */
    void afterFinally(Object bean, Method method, Object[] args, Object result, Long id);

    /**
     * 后置强化方法
     * 出现异常不会执行
     * 如果未出现异常，在afterFinally方法之后执行
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     * @param id     日志表主键id
     */
    void after(Object bean, Method method, Object[] args, Long id);
}

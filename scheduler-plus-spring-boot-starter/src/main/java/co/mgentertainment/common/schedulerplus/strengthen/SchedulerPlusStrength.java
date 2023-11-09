package co.mgentertainment.common.schedulerplus.strengthen;

import java.lang.reflect.Method;

public interface SchedulerPlusStrength {

    /**
     * 前置强化方法
     *
     * @param bean     bean实例（或者是被代理的bean）
     * @param method   执行的方法对象
     * @param args     方法参数
     */
    void before(Object bean, Method method, Object[] args);

    /**
     * 异常强化方法
     *
     * @param bean     bean实例（或者是被代理的bean）
     * @param method   执行的方法对象
     * @param args     方法参数
     */
    void exception(Object bean, Method method, Object[] args);

    /**
     * Finally强化方法，出现异常也会执行
     *
     * @param bean     bean实例（或者是被代理的bean）
     * @param method   执行的方法对象
     * @param args     方法参数
     * @param result   任务执行结果
     */
    void afterFinally(Object bean, Method method, Object[] args, Object result);

    /**
     * 后置强化方法
     * 出现异常不会执行
     * 如果未出现异常，在afterFinally方法之后执行
     *
     * @param bean     bean实例（或者是被代理的bean）
     * @param method   执行的方法对象
     * @param args     方法参数
     */
    void after(Object bean, Method method, Object[] args);
}

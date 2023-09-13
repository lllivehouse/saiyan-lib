package co.mgentertainment.common.syslog.event;

import co.mgentertainment.common.syslog.dal.po.SysLogDO;
import org.springframework.context.ApplicationEvent;

/**
 * @description 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {

	public SysLogEvent(SysLogDO source) {
		super(source);
	}

}

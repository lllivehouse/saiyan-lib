package co.mgentertainment.common.syslog.event;

import co.mgentertainment.common.syslog.dal.po.SysLogDO;
import co.mgentertainment.common.syslog.dal.repository.SyslogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author lengleng 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener {

	private final SyslogRepository syslogRepository;

	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLogDO sysLog = (SysLogDO) event.getSource();
		syslogRepository.saveLog(sysLog);
	}

}

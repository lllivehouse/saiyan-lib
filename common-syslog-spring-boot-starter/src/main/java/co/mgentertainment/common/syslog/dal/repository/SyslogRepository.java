package co.mgentertainment.common.syslog.dal.repository;

import co.mgentertainment.common.syslog.dal.po.SysLogDO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description SyslogRepositoryImpl
 */
public class SyslogRepository implements InitializingBean {

    private static final String INSERT_SQL = "insert into sys_log (type, title, service_id, remote_addr, user_agent, request_uri, method, time, del_flag, create_time, update_time, create_by, update_by, opration_module, opration_text, opration_type, params, exception)  values (?, ?, ?, ?, ?, ?, ?, ?, 0, now(), now(), ?, ?, ?, ?, ?, ?, ?)";

    private final DataSource syslogDataSource;

    private JdbcTemplate jdbcTemplate;

    public SyslogRepository(DataSource syslogDataSource) {
        this.syslogDataSource = syslogDataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.jdbcTemplate = new JdbcTemplate(this.syslogDataSource);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveLog(SysLogDO sysLog) {
        jdbcTemplate.update(INSERT_SQL,
                sysLog.getType(), sysLog.getTitle(), sysLog.getServiceId(), sysLog.getRemoteAddr(), sysLog.getUserAgent(),
                sysLog.getRequestUri(), sysLog.getMethod(), sysLog.getTime(), sysLog.getCreateBy(), sysLog.getUpdateBy(),
                sysLog.getOprationModule(), sysLog.getOprationText(), sysLog.getOprationType(), sysLog.getParams(), sysLog.getException()
        );
    }
}

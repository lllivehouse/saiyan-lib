package co.mgentertainment.common.schedulerplus.support;

import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description SchedulerPlusObjectMapper
 */
@Mapper
public interface SchedulerPlusObjectMapper {

    SchedulerPlusObjectMapper INSTANCE = Mappers.getMapper(SchedulerPlusObjectMapper.class);

    SchedulerPlusTaskDO toSchedulerPlusTaskDO(SchedulerPlusTaskItem schedulerPlusTaskItem);

    @Mappings({
            @Mapping(source = "scheduledMode.code", target = "scheduledMode"),
            @Mapping(source = "jobBeanName", target = "jobName"),
            @Mapping(source = "schedulerDesc", target = "taskDesc"),
            @Mapping(source = "status.code", target = "taskStatus"),
            @Mapping(target = "taskArgs", expression = "java(SchedulerPlusMeta.parseContextArgs(schedulerPlusMeta.getContextArgs()))")
    })
    SchedulerPlusTaskItem toSchedulerPlusTaskItem(SchedulerPlusMeta schedulerPlusMeta);

    @Mappings({
            @Mapping(source = "scheduledMode.code", target = "scheduledMode"),
            @Mapping(source = "jobBeanName", target = "jobName"),
            @Mapping(source = "schedulerDesc", target = "taskDesc"),
            @Mapping(source = "status.code", target = "taskStatus"),
            @Mapping(target = "taskArgs", expression = "java(SchedulerPlusMeta.parseContextArgs(schedulerPlusMeta.getContextArgs()))")
    })
    SchedulerPlusTaskDO toSchedulerPlusTaskDO(SchedulerPlusMeta schedulerPlusMeta);

}

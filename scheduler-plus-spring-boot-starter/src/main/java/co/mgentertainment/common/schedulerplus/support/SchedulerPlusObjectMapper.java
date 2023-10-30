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

    @Mappings({
            @Mapping(target = "jobName", expression = "java(schedulerPlusTaskItem.getJobClass().getSimpleName())")
    })
    SchedulerPlusTaskDO toSchedulerPlusTaskDO(SchedulerPlusTaskItem schedulerPlusTaskItem);

    @Mappings({
            @Mapping(source = "scheduledMode.code", target = "scheduledMode"),
            @Mapping(source = "schedulerDesc", target = "taskDesc"),
            @Mapping(source = "status.code", target = "taskStatus"),
            @Mapping(target = "taskArgs", expression = "java(SchedulerPlusMeta.parseContextArgs(schedulerPlusMeta.getContextArgs()))"),
            @Mapping(target = "jobClass", expression = "java(schedulerPlusMeta.getJob().getClass())")
    })
    SchedulerPlusTaskItem toSchedulerPlusTaskItem(SchedulerPlusMeta schedulerPlusMeta);

    @Mappings({
            @Mapping(source = "scheduledMode.code", target = "scheduledMode"),
            @Mapping(source = "schedulerDesc", target = "taskDesc"),
            @Mapping(source = "status.code", target = "taskStatus"),
            @Mapping(target = "taskArgs", expression = "java(SchedulerPlusMeta.parseContextArgs(schedulerPlusMeta.getContextArgs()))"),
            @Mapping(target = "jobName", expression = "java(schedulerPlusMeta.getJob().getClass().getSimpleName())")
    })
    SchedulerPlusTaskDO toSchedulerPlusTaskDO(SchedulerPlusMeta schedulerPlusMeta);

}

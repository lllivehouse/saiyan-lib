/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.mgentertainment.common.uidgen.dal.mapper;

import co.mgentertainment.common.uidgen.dal.entity.WorkerNodeEntity;

import java.util.List;

/**
 * DAO for M_WORKER_NODE
 *
 * @author yutianbao
 */
public interface WorkerNodeMapper {

    /**
     * Get work node host
     * 
     * @return
     */
    List<String> selectWorkerNodeHost();

    /**
     * Add {@link WorkerNodeEntity}
     * 
     * @param workerNodeEntity
     */
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);

    /**
     * deleteByPrimaryKey
     * @param id
     */
    void deleteByPrimaryKey(Long id);

    /**
     * deleteByHostname
     * @param hostname
     */
    void deleteByHostname(String hostname);
}

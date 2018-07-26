package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;

/**
 * 应用库升级服务
 */
public interface DhProcessAppUpdateService {


    ServerResponse pullAllProcessActivityMeta(String proAppId, String snapshotId);
}
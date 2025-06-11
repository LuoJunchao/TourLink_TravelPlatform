package org.tourlink.attractionservice.service;

import java.io.ByteArrayOutputStream;

/**
 * 景点热度Excel模板服务接口
 */
public interface PopularityExcelTemplateService {
    
    /**
     * 生成景点热度导入模板
     * @return Excel文件字节数组
     */
    ByteArrayOutputStream generatePopularityImportTemplate();
}

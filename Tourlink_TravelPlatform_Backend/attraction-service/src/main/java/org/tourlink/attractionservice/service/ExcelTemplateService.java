package org.tourlink.attractionservice.service;

import java.io.ByteArrayOutputStream;

/**
 * Excel模板服务接口
 */
public interface ExcelTemplateService {
    
    /**
     * 生成景点导入模板
     * @return Excel文件字节数组
     */
    ByteArrayOutputStream generateAttractionImportTemplate();
}

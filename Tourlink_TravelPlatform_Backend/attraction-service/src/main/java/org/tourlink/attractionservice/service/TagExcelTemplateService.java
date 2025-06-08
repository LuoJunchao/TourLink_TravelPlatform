package org.tourlink.attractionservice.service;

import java.io.ByteArrayOutputStream;

public interface TagExcelTemplateService {
    
    /**
     * 生成标签导入模板
     * @return Excel文件字节数组
     */
    ByteArrayOutputStream generateTagImportTemplate();
}

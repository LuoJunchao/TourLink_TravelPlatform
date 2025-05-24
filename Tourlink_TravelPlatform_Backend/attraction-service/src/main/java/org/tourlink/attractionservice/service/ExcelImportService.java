package org.tourlink.attractionservice.service;

import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;

/**
 * Excel导入服务接口
 */
public interface ExcelImportService {
    
    /**
     * 从Excel文件导入景点数据
     * @param file Excel文件
     * @return 导入结果
     */
    ImportResultDTO importAttractionsFromExcel(MultipartFile file);
}

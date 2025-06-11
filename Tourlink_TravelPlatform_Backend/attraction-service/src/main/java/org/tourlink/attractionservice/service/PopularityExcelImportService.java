package org.tourlink.attractionservice.service;

import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;

/**
 * 景点热度Excel导入服务接口
 */
public interface PopularityExcelImportService {
    
    /**
     * 从Excel文件导入景点热度数据
     * @param file Excel文件
     * @return 导入结果
     */
    ImportResultDTO importPopularityFromExcel(MultipartFile file);
}

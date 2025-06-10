package org.tourlink.attractionservice.service;

import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;

public interface TagExcelImportService {
    /**
     * 从Excel文件导入标签数据
     * @param file Excel文件
     * @return 导入结果
     */
    ImportResultDTO importTagsFromExcel(MultipartFile file);
}

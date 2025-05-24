package org.tourlink.attractionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 导入结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDTO {
    
    /**
     * 总记录数
     */
    private int totalCount;
    
    /**
     * 成功导入数
     */
    private int successCount;
    
    /**
     * 失败数
     */
    private int failCount;
    
    /**
     * 错误信息列表
     */
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    /**
     * 添加错误信息
     * @param error 错误信息
     */
    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}

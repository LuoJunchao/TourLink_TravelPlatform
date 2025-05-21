package org.tourlink.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应对象
 * @param <T> 分页数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private boolean first;
    private boolean last;
    private boolean empty;
    
    /**
     * 从Spring Data Page对象创建分页响应
     * @param page Spring Data Page对象
     * @param <T> 数据类型
     * @return 分页响应对象
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
    
    /**
     * 从Spring Data Page对象创建分页响应，并转换数据类型
     * @param page Spring Data Page对象
     * @param content 转换后的内容
     * @param <T> 原始数据类型
     * @param <R> 转换后的数据类型
     * @return 分页响应对象
     */
    public static <T, R> PageResponse<R> from(Page<T> page, List<R> content) {
        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}

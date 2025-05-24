package org.tourlink.attractionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AttractionRequest {

    // 省份
    @NotBlank(message = "省份不能为空")
    @Size(max = 50, message = "省份名称长度不能超过50个字符")
    private String province;

    // 名称
    @NotBlank(message = "景点名称不能为空")
    @Size(max = 100, message = "景点名称长度不能超过100个字符")
    private String name;

    // 星级
    @Size(max = 20, message = "星级长度不能超过20个字符")
    private String starLevel;

    // 评分
    @DecimalMin(value = "0.0", message = "评分最小值为0")
    @DecimalMax(value = "5.0", message = "评分最大值为5")
    private BigDecimal rating;

    // 价格
    @DecimalMin(value = "0.0", message = "价格不能为负数")
    private BigDecimal price;

    // 销量
    @Min(value = 0, message = "销量不能为负数")
    private Integer salesVolume;

    // 省/市/区
    @Size(max = 100, message = "省/市/区长度不能超过100个字符")
    private String region;

    // 坐标
    @Size(max = 100, message = "坐标长度不能超过100个字符")
    private String coordinates;

    // 简介
    @Size(max = 5000, message = "简介长度不能超过5000个字符")
    private String description;

    // 是否免费
    private Boolean isFree;

    // 具体地址
    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    // 标签
    @Size(max = 20, message = "最多20个标签")
    private List<String> tags;

    // 推荐游玩时间（字符串格式，如"3-4小时"）
    @Size(max = 50, message = "推荐游玩时间长度不能超过50个字符")
    private String recommendedPlayTime;

    // 图片
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;
}

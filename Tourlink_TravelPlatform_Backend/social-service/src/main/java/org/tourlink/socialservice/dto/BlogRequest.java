package org.tourlink.socialservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class BlogRequest {

    private String userId;
    private String title;
    private String content;
    private List<String> images;
    private List<String> tags;

}

package org.tourlink.dataplatformservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "content_tag")
@Data
public class ContentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false, unique = true)
    private String tagName;

}

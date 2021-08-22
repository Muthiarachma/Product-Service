package com.MiniProjek.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DownloadDTO {
    private Long id;
    private String fileName;

    private String fileType;

    private Date upload_at;
}

package com.example.check.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnexDownloadNewDO {
    private String annexName;
    private String annexType;
    private String mimeType;
    private byte[] annexData;
}

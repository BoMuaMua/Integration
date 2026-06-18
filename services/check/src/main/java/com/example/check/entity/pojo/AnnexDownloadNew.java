package com.example.check.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnexDownloadNew {
    private String annexName;
    private String mimeType;
    private byte[] annexData;
}

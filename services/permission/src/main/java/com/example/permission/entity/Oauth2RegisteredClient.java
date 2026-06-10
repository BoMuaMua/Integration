package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Oauth2_registered_client")
public class Oauth2RegisteredClient {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String redirectUris;
    private String requireAuthorizationConsent;
    private String scope;
    private Integer status;
    private String url;
}

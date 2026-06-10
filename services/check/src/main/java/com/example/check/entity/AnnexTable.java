package com.example.check.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
//附件表
@Data
@Entity
@Table(name = "annex_table")
public class AnnexTable {

    @Id
    @Column(name = "requirement_id")
    private String requirementId;

    @Column(name = "annex_id")
    private int annexID;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private double size;

    @Column(name = "type")
    private String type;

    @Column(name = "last_modified")
    private String lastModified;

}

package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "project", schema = "mqct")
public class ProjectEntity {
    private int id;
    @ApiModelProperty(value = "项目名称")
    private String xmmc;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity that = (ProjectEntity) o;
        return id == that.id &&
                Objects.equals(xmmc, that.xmmc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, xmmc);
    }
}

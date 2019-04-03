package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "project", schema = "mqct")
public class ProjectEntity {
    private int id;
    @ApiModelProperty(value = "外在项目名称")
    private String wzxmmc;
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "项目名称")
    private String xmmc;
    @ApiModelProperty(value = "备注")
    private String bz;
    @ApiModelProperty(value = "报价单位")
    private String bjdw;
    @ApiModelProperty(value = "运输费")
    private BigDecimal ysf;
    @ApiModelProperty(value = "第三方检测费")
    private BigDecimal jcf;

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
    @Column(name = "uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "out_name")
    public String getWzxmmc() {
        return wzxmmc;
    }

    public void setWzxmmc(String wzxmmc) {
        this.wzxmmc = wzxmmc;
    }

    @Basic
    @Column(name = "name")
    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    @Basic
    @Column(name = "remark")
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Basic
    @Column(name = "quotation_unit")
    public String getBjdw() {
        return bjdw;
    }

    public void setBjdw(String bjdw) {
        this.bjdw = bjdw;
    }

    @Basic
    @Column(name = "transport_money")
    public BigDecimal getYsf() {
        return ysf;
    }

    public void setYsf(BigDecimal ysf) {
        this.ysf = ysf;
    }

    @Basic
    @Column(name = "test_fee")
    public BigDecimal getJcf() {
        return jcf;
    }

    public void setJcf(BigDecimal jcf) {
        this.jcf = jcf;
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

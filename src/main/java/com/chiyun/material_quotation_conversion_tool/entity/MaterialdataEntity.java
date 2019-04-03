package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by wazto on 2019/4/3.
 */
@Entity
@Table(name = "materialdata", schema = "mqct", catalog = "")
@ApiModel
public class MaterialdataEntity {
    @ApiModelProperty("材料id")
    private int clid;
    @ApiModelProperty("材料名称")
    private String clmc;
    @ApiModelProperty("材料规格")
    private String clgg;
    @ApiModelProperty("材料单位")
    private String cldw;
    @ApiModelProperty("用户id")
    private String uid;
    @ApiModelProperty("基价")
    private BigDecimal jj;
    @ApiModelProperty("成本价")
    private BigDecimal cbj;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getClid() {
        return clid;
    }

    public void setClid(int clid) {
        this.clid = clid;
    }

    @Basic
    @Column(name = "name")
    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }

    @Basic
    @Column(name = "typesize")
    public String getClgg() {
        return clgg;
    }

    public void setClgg(String clgg) {
        this.clgg = clgg;
    }

    @Basic
    @Column(name = "unit")
    public String getCldw() {
        return cldw;
    }

    public void setCldw(String cldw) {
        this.cldw = cldw;
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
    @Column(name = "baseprice")
    public BigDecimal getJj() {
        return jj;
    }

    public void setJj(BigDecimal jj) {
        this.jj = jj;
    }

    @Basic
    @Column(name = "costprice")
    public BigDecimal getCbj() {
        return cbj;
    }

    public void setCbj(BigDecimal cbj) {
        this.cbj = cbj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialdataEntity entity = (MaterialdataEntity) o;
        return clid == entity.clid &&
                Objects.equals(clmc, entity.clmc) &&
                Objects.equals(clgg, entity.clgg) &&
                Objects.equals(cldw, entity.cldw) &&
                Objects.equals(uid, entity.uid) &&
                Objects.equals(jj, entity.jj) &&
                Objects.equals(cbj, entity.cbj);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clid, clmc, clgg, cldw, uid, jj, cbj);
    }
}

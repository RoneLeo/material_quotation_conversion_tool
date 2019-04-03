package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by wazto on 2019/4/1.
 */
@Entity
@Table(name = "pro_mate_price", schema = "mqct", catalog = "")
@ApiModel
@IdClass(ProMatePriceEntityPK.class)
public class ProMatePriceEntity {
    @ApiModelProperty("省份id")
    private int sfid;
    @ApiModelProperty("材料id")
    private int clid;
    @ApiModelProperty("基价")
    private BigDecimal jj;
    @ApiModelProperty("成本价")
    private BigDecimal cbj;

    @Id
    @Column(name = "pid")
    public int getSfid() {
        return sfid;
    }

    public void setSfid(int sfid) {
        this.sfid = sfid;
    }

    @Id
    @Column(name = "mid")
    public int getClid() {
        return clid;
    }

    public void setClid(int clid) {
        this.clid = clid;
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
        ProMatePriceEntity that = (ProMatePriceEntity) o;
        return sfid == that.sfid &&
                clid == that.clid &&
                Objects.equals(jj, that.jj) &&
                Objects.equals(cbj, that.cbj);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sfid, clid, jj, cbj);
    }
}

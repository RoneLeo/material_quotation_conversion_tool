package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "excel_data", schema = "mqct")
public class ExcelDataEntity {
    private int id;
    @ApiModelProperty(value = "项目编号")
    private Integer xmbh;
    @ApiModelProperty(value = "货物名称")
    private String hwmc;
    @ApiModelProperty(value = "型号规格")
    private String xhgg;
    @ApiModelProperty(value = "货物单位")
    private String hwdw;
    @ApiModelProperty(value = "数量")
    private int sl;
    @ApiModelProperty(value = "单价")
    private BigDecimal dj;
    @ApiModelProperty(value = "总价")
    private BigDecimal zj;
    @ApiModelProperty(value = "创建时间")
    private Date cjsj;
    @ApiModelProperty(value = "备注")
    private String bz;


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
    @Column(name = "project_id")
    public Integer getXmbh() {
        return xmbh;
    }

    public void setXmbh(Integer xmbh) {
        this.xmbh = xmbh;
    }

    @Basic
    @Column(name = "goods_name")
    public String getHwmc() {
        return hwmc;
    }

    public void setHwmc(String hwmc) {
        this.hwmc = hwmc;
    }

    @Basic
    @Column(name = "goods_model")
    public String getXhgg() {
        return xhgg;
    }

    public void setXhgg(String xhgg) {
        this.xhgg = xhgg;
    }

    @Basic
    @Column(name = "goods_unit")
    public String getHwdw() {
        return hwdw;
    }

    public void setHwdw(String hwdw) {
        this.hwdw = hwdw;
    }

    @Basic
    @Column(name = "number")
    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    @Basic
    @Column(name = "price")
    public BigDecimal getDj() {
        return dj;
    }

    public void setDj(BigDecimal dj) {
        this.dj = dj;
    }

    @Basic
    @Column(name = "total")
    public BigDecimal getZj() {
        return zj;
    }

    public void setZj(BigDecimal zj) {
        this.zj = zj;
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
    @Column(name = "create_time")
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcelDataEntity that = (ExcelDataEntity) o;
        return id == that.id &&
                Objects.equals(xmbh, that.xmbh) &&
                Objects.equals(hwmc, that.hwmc) &&
                Objects.equals(xhgg, that.xhgg) &&
                Objects.equals(hwdw, that.hwdw) &&
                Objects.equals(sl, that.sl) &&
                Objects.equals(dj, that.dj) &&
                Objects.equals(zj, that.zj) &&
                Objects.equals(bz, that.bz) &&
                Objects.equals(cjsj, that.cjsj);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, xmbh, hwmc, xhgg, hwdw, sl, dj, zj, bz, cjsj);
    }
}

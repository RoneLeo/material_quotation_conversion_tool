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
@Table(name = "excel_data", schema = "mqct")
@ApiModel
public class ExcelDataEntity {
    @ApiModelProperty("材料id")
    private int id;
    @ApiModelProperty("项目id")
    private Integer xmbh;
    @ApiModelProperty("材料名称")
    private String clmc;
    @ApiModelProperty("材料规格")
    private String clgg;
    @ApiModelProperty("材料单位")
    private String cldw;
    @ApiModelProperty("材料数量")
    private int clsl;
    @ApiModelProperty("基价,添加与修改时不做处理")
    private BigDecimal jj;
    @ApiModelProperty("成本价,添加与修改时不做处理")
    private BigDecimal cbj;

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
    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }

    @Basic
    @Column(name = "goods_model")
    public String getClgg() {
        return clgg;
    }

    public void setClgg(String clgg) {
        this.clgg = clgg;
    }

    @Basic
    @Column(name = "goods_unit")
    public String getCldw() {
        return cldw;
    }

    public void setCldw(String cldw) {
        this.cldw = cldw;
    }

    @Basic
    @Column(name = "number")
    public int getClsl() {
        return clsl;
    }

    public void setClsl(int clsl) {
        this.clsl = clsl;
    }

    @Basic
    @Column(name = "price")
    public BigDecimal getJj() {
        return jj;
    }

    public void setJj(BigDecimal jj) {
        this.jj = jj;
    }

    @Basic
    @Column(name = "total")
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
        ExcelDataEntity that = (ExcelDataEntity) o;
        return id == that.id &&
                clsl == that.clsl &&
                Objects.equals(xmbh, that.xmbh) &&
                Objects.equals(clmc, that.clmc) &&
                Objects.equals(clgg, that.clgg) &&
                Objects.equals(cldw, that.cldw) &&
                Objects.equals(jj, that.jj) &&
                Objects.equals(cbj, that.cbj);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, xmbh, clmc, clgg, cldw, clsl, jj, cbj);
    }
}

package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by wazto on 2019/4/1.
 */
@Entity
@ApiModel
@Table(name = "materialdata", schema = "mqct", catalog = "")
public class MaterialdataEntity {
    private int id;
    @ApiModelProperty("材料名称")
    private String mc;
    @ApiModelProperty("规格型号")
    private String gg;
    @ApiModelProperty("单位")
    private String dw;

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
    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    @Basic
    @Column(name = "typesize")
    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    @Basic
    @Column(name = "unit")
    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialdataEntity that = (MaterialdataEntity) o;
        return id == that.id &&
                Objects.equals(mc, that.mc) &&
                Objects.equals(gg, that.gg) &&
                Objects.equals(dw, that.dw);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mc, gg, dw);
    }
}

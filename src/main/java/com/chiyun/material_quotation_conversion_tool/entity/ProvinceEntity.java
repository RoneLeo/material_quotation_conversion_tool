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
@Table(name = "province", schema = "mqct", catalog = "")
public class ProvinceEntity {
    private int id;
    @ApiModelProperty("省份")
    private String mc;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvinceEntity that = (ProvinceEntity) o;
        return id == that.id &&
                Objects.equals(mc, that.mc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mc);
    }
}

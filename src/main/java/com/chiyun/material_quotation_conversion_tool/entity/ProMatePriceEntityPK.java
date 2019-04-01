package com.chiyun.material_quotation_conversion_tool.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by wazto on 2019/4/1.
 */
public class ProMatePriceEntityPK implements Serializable {
    private int sfid;
    private int clid;

    @Column(name = "pid")
    @Id
    public int getSfid() {
        return sfid;
    }

    public void setSfid(int sfid) {
        this.sfid = sfid;
    }

    @Column(name = "mid")
    @Id
    public int getClid() {
        return clid;
    }

    public void setClid(int clid) {
        this.clid = clid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProMatePriceEntityPK that = (ProMatePriceEntityPK) o;
        return sfid == that.sfid &&
                clid == that.clid;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sfid, clid);
    }
}

package com.chiyun.material_quotation_conversion_tool.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by wazto on 2019/4/2.
 */
@Entity
@ApiModel
@Table(name = "user", schema = "mqct", catalog = "")
public class UserEntity {
    @ApiModelProperty("用户id")
    private String id;
    @ApiModelProperty("用户账号")
    private String zh;
    @ApiModelProperty("用户密码")
    private String mm;
    @ApiModelProperty("用户所属省份id")
    private Integer sfid;
    @ApiModelProperty("用户角色：0-普通用户，1-管理员")
    private int js;
    @ApiModelProperty("用户登录时限")
    private Date dlsx;

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "account")
    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    @Basic
    @Column(name = "password")
    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    @Basic
    @Column(name = "pid")
    public Integer getSfid() {
        return sfid;
    }

    public void setSfid(Integer sfid) {
        this.sfid = sfid;
    }

    @Basic
    @Column(name = "role")
    public int getJs() {
        return js;
    }

    public void setJs(int js) {
        this.js = js;
    }

    @Basic
    @Column(name = "timelimit")
    public Date getDlsx() {
        return dlsx;
    }

    public void setDlsx(Date dlsx) {
        this.dlsx = dlsx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return js == that.js &&
                Objects.equals(id, that.id) &&
                Objects.equals(zh, that.zh) &&
                Objects.equals(mm, that.mm) &&
                Objects.equals(sfid, that.sfid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, zh, mm, sfid, js);
    }
}

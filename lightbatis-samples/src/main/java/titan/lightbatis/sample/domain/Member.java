package titan.lightbatis.sample.domain;

import javax.persistence.*;
import javax.annotation.Generated;

import lombok.Data;
import titan.lightbatis.sample.annotations.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Member企业表
 */
@Entity(name="member")
@ApiModel(value="企业表", description="企业表")
@Table(name="member")
@Generated("titan.lightbatis.web.generate.LombokBeanSerializer")
@Data
public class Member {

    @Column(name="created_by", length=19)
    @ApiModelProperty(value="创建人", allowEmptyValue=true)
    private Long createdBy;

    @Column(name="created_time", length=29)
    @ApiModelProperty(value="创建时间", allowEmptyValue=true)
    private java.sql.Timestamp createdTime;

    @Column(name="id", length=19, nullable=false)
    @ApiModelProperty(value="ID", allowEmptyValue=true)
    @OrderBy("DESC")
    @Id
    private Long id;

    @Column(name="kind_id", length=5)
    @ApiModelProperty(value="企业类型", allowEmptyValue=true)
    private Short kindId;

    @Column(name="member_name", length=128)
    @ApiModelProperty(value="企业名称", allowEmptyValue=true)
    private String memberName;

    @Column(name="revision", length=5)
    @ApiModelProperty(value="乐观锁", allowEmptyValue=true)
    private Short revision;

    @Column(name="staff_id", length=19)
    @ApiModelProperty(value="企业联系人", allowEmptyValue=true)
    private Long staffId;

    @Column(name="staff_name", length=32)
    @ApiModelProperty(value="企业联系人名称", allowEmptyValue=true)
    private String staffName;

    @Column(name="status", length=32)
    @ApiModelProperty(value="企业状态", allowEmptyValue=true)
    private String status;

    @Column(name="updated_by", length=19)
    @ApiModelProperty(value="更新人", allowEmptyValue=true)
    private Long updatedBy;

    @Column(name="updated_time", length=29)
    @ApiModelProperty(value="更新时间", allowEmptyValue=true)
    private java.sql.Timestamp updatedTime;

}


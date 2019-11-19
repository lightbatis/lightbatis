package com.aeotrade.provider.model.entity;

import javax.persistence.Entity;
import titan.lightbatis.annotations.DalEntity;
import javax.persistence.Column;
import javax.annotation.Generated;
import javax.persistence.Table;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Id;

/**
 * AssignOrder派单表
 */
@Entity(name="assign_order")
@ApiModel(value="派单表", description="派单表")
@DalEntity(name="AssignOrder", table="assign_order")
@Table(name="assign_order")
@Generated("titan.lightbatis.web.generate.LombokBeanSerializer")
@Data
public class AssignOrder {

    @Column(name="assign_status", length=32)
    @ApiModelProperty(value="待报价企业的状态", allowEmptyValue=true)
    private String assignStatus;

    @Column(name="created_by", length=19)
    @ApiModelProperty(value="创建人", allowEmptyValue=true)
    private Long createdBy;

    @Column(name="created_time", length=29)
    @ApiModelProperty(value="创建时间", allowEmptyValue=true)
    private java.sql.Timestamp createdTime;

    @Column(name="id", length=19, nullable=false)
    @ApiModelProperty(value="ID", allowEmptyValue=true)
    @Id
    private Long id;

    @Column(name="inquiry_member_id", length=19)
    @ApiModelProperty(value="询价的企业", allowEmptyValue=true)
    private Long inquiryMemberId;

    @Column(name="member_id", length=19)
    @ApiModelProperty(value="派单的企业", allowEmptyValue=true)
    private Long memberId;

    @Column(name="quote_id", length=19)
    @ApiModelProperty(value="询价单ID", allowEmptyValue=true)
    private Long quoteId;

    @Column(name="quote_member_id", length=19)
    @ApiModelProperty(value="待报价的企业", allowEmptyValue=true)
    private Long quoteMemberId;

    @Column(name="revision", length=5)
    @ApiModelProperty(value="乐观锁", allowEmptyValue=true)
    private Short revision;

    @Column(name="updated_by", length=19)
    @ApiModelProperty(value="更新人", allowEmptyValue=true)
    private Long updatedBy;

    @Column(name="updated_time", length=29)
    @ApiModelProperty(value="更新时间", allowEmptyValue=true)
    private java.sql.Timestamp updatedTime;

}


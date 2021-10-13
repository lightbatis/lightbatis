package titan.lightbatis.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import titan.lightbatis.annotations.DalEntity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OsPropertyentry
 */
@Table(name="OS_PROPERTYENTRY")
@DalEntity(name="PropertyEntry", table="OS_PROPERTYENTRY")
@ApiModel
@Entity(name="OS_PROPERTYENTRY")
@Generated("titan.lightbatis.web.generate.LombokBeanSerializer")
@Data
public class PropertyEntry {

    @Column(name="DATA_VALUE", length=900)
    @ApiModelProperty(value="DATA_VALUE", allowEmptyValue=true)
    private String dataValue;

    @Column(name="DATE_VALUE", length=19)
    @ApiModelProperty(value="DATE_VALUE", allowEmptyValue=true)
    private java.sql.Timestamp dateValue;

    @Column(name="FLOAT_VALUE", length=24)
    @ApiModelProperty(value="FLOAT_VALUE", allowEmptyValue=true)
    private java.math.BigDecimal floatValue;

    @Column(name="GLOBAL_KEY", length=32, nullable=false)
    @ApiModelProperty(value="GLOBAL_KEY", allowEmptyValue=true)
    private String globalKey;

    @Id
    @Column(name="ID", length=19, nullable=false)
    @ApiModelProperty(value="ID", allowEmptyValue=true)
    private Long id;

    @Column(name="ITEM_KEY", length=32, nullable=false)
    @ApiModelProperty(value="ITEM_KEY", allowEmptyValue=true)
    private String itemKey;

    @Column(name="ITEM_TYPE", length=10)
    @ApiModelProperty(value="ITEM_TYPE", allowEmptyValue=true)
    private Integer itemType;

    @Column(name="NUMBER_VALUE", length=24)
    @ApiModelProperty(value="NUMBER_VALUE", allowEmptyValue=true)
    private java.math.BigDecimal numberValue;

    @Column(name="STRING_VALUE")
    @ApiModelProperty(value="STRING_VALUE", allowEmptyValue=true)
    private String stringValue;

}


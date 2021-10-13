package titan.lightbatis.web.entity.query;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import titan.lightbatis.web.entity.PropertyEntry;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QOsPropertyentry is a Querydsl query type for OsPropertyentry
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QPropertyEntry extends com.querydsl.sql.RelationalPathBase<PropertyEntry> {

    private static final long serialVersionUID = 1871433487;

    public static final QPropertyEntry propertyEntry = new QPropertyEntry("OS_PROPERTYENTRY");

    public final StringPath dataValue = createString("dataValue");

    public final DateTimePath<java.sql.Timestamp> dateValue = createDateTime("dateValue", java.sql.Timestamp.class);

    public final NumberPath<java.math.BigDecimal> floatValue = createNumber("floatValue", java.math.BigDecimal.class);

    public final StringPath globalKey = createString("globalKey");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemKey = createString("itemKey");

    public final NumberPath<Integer> itemType = createNumber("itemType", Integer.class);

    public final NumberPath<java.math.BigDecimal> numberValue = createNumber("numberValue", java.math.BigDecimal.class);

    public final StringPath stringValue = createString("stringValue");

    public QPropertyEntry(String variable) {
        super(PropertyEntry.class, forVariable(variable), "", "OS_PROPERTYENTRY");
        addMetadata();
    }

    public QPropertyEntry(String variable, String schema, String table) {
        super(PropertyEntry.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPropertyEntry(String variable, String schema) {
        super(PropertyEntry.class, forVariable(variable), schema, "OS_PROPERTYENTRY");
        addMetadata();
    }

    public QPropertyEntry(Path<? extends PropertyEntry> path) {
        super(path.getType(), path.getMetadata(), "", "OS_PROPERTYENTRY");
        addMetadata();
    }

    public QPropertyEntry(PathMetadata metadata) {
        super(PropertyEntry.class, metadata, "", "OS_PROPERTYENTRY");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dataValue, ColumnMetadata.named("DATA_VALUE").withIndex(0).ofType(Types.VARCHAR).withSize(900));
        addMetadata(dateValue, ColumnMetadata.named("DATE_VALUE").withIndex(0).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(floatValue, ColumnMetadata.named("FLOAT_VALUE").withIndex(0).ofType(Types.DECIMAL).withSize(24));
        addMetadata(globalKey, ColumnMetadata.named("GLOBAL_KEY").withIndex(0).ofType(Types.VARCHAR).withSize(32).notNull());
        addMetadata(id, ColumnMetadata.named("ID").withIndex(0).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(itemKey, ColumnMetadata.named("ITEM_KEY").withIndex(0).ofType(Types.VARCHAR).withSize(32).notNull());
        addMetadata(itemType, ColumnMetadata.named("ITEM_TYPE").withIndex(0).ofType(Types.INTEGER).withSize(10));
        addMetadata(numberValue, ColumnMetadata.named("NUMBER_VALUE").withIndex(0).ofType(Types.DECIMAL).withSize(24));
        addMetadata(stringValue, ColumnMetadata.named("STRING_VALUE").withIndex(0).ofType(Types.VARCHAR).withSize(255));
    }

}


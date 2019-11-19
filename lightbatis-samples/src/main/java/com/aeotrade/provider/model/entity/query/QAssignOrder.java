package com.aeotrade.provider.model.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.aeotrade.provider.model.entity.AssignOrder;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAssignOrder is a Querydsl query type for AssignOrder
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAssignOrder extends com.querydsl.sql.RelationalPathBase<AssignOrder> {

    private static final long serialVersionUID = 1401672590;

    public static final QAssignOrder assignOrder = new QAssignOrder("assign_order");

    public final StringPath assignStatus = createString("assignStatus");

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> inquiryMemberId = createNumber("inquiryMemberId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> quoteId = createNumber("quoteId", Long.class);

    public final NumberPath<Long> quoteMemberId = createNumber("quoteMemberId", Long.class);

    public final NumberPath<Short> revision = createNumber("revision", Short.class);

    public final NumberPath<Long> updatedBy = createNumber("updatedBy", Long.class);

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public QAssignOrder(String variable) {
        super(AssignOrder.class, forVariable(variable), "", "assign_order");
        addMetadata();
    }

    public QAssignOrder(String variable, String schema, String table) {
        super(AssignOrder.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAssignOrder(String variable, String schema) {
        super(AssignOrder.class, forVariable(variable), schema, "assign_order");
        addMetadata();
    }

    public QAssignOrder(Path<? extends AssignOrder> path) {
        super(path.getType(), path.getMetadata(), "", "assign_order");
        addMetadata();
    }

    public QAssignOrder(PathMetadata metadata) {
        super(AssignOrder.class, metadata, "", "assign_order");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assignStatus, ColumnMetadata.named("assign_status").withIndex(0).ofType(Types.NULL).withSize(32));
        addMetadata(createdBy, ColumnMetadata.named("created_by").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(createdTime, ColumnMetadata.named("created_time").withIndex(0).ofType(Types.NULL).withSize(29));
        addMetadata(id, ColumnMetadata.named("id").withIndex(0).ofType(Types.NULL).withSize(19).notNull());
        addMetadata(inquiryMemberId, ColumnMetadata.named("inquiry_member_id").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(memberId, ColumnMetadata.named("member_id").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(quoteId, ColumnMetadata.named("quote_id").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(quoteMemberId, ColumnMetadata.named("quote_member_id").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(revision, ColumnMetadata.named("revision").withIndex(0).ofType(Types.NULL).withSize(5));
        addMetadata(updatedBy, ColumnMetadata.named("updated_by").withIndex(0).ofType(Types.NULL).withSize(19));
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").withIndex(0).ofType(Types.NULL).withSize(29));
    }

}


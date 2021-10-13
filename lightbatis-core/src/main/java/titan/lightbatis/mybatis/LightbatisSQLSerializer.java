package titan.lightbatis.mybatis;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.SQLTemplates;

public class LightbatisSQLSerializer extends SQLSerializer {
    private String limitTemplate = "\nlimit {0}";
    private boolean limitRequired = false;
    private int maxLimit = Integer.MAX_VALUE;
    private String offsetTemplate = "\noffset {0}";

	public LightbatisSQLSerializer(Configuration conf) {
		super(conf);
	}

	public LightbatisSQLSerializer(Configuration conf, boolean dml) {
		super(conf, dml);
		MetaObject metaObject = SystemMetaObject.forObject(templates);
		this.limitTemplate = (String) metaObject.getValue("limitTemplate");
		this.offsetTemplate = (String) metaObject.getValue("offsetTemplate");
		this.limitRequired = (Boolean) metaObject.getValue("limitRequired");
		this.maxLimit = (Integer) metaObject.getValue("maxLimit");
	}

    @Override
    protected void serializeForQuery(QueryMetadata metadata, boolean forCountRow) {
        boolean oldInSubquery = inSubquery;
        inSubquery = inSubquery || getLength() > 0;
        boolean oldSkipParent = skipParent;
        skipParent = false;
        final Expression<?> select = metadata.getProjection();
        final List<JoinExpression> joins = metadata.getJoins();
        final Predicate where = metadata.getWhere();
        final List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        final Predicate having = metadata.getHaving();
        final List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        final Set<QueryFlag> flags = metadata.getFlags();
        final boolean hasFlags = !flags.isEmpty();
        String suffix = null;

        List<? extends Expression<?>> sqlSelect;
        if (select instanceof FactoryExpression) {
            sqlSelect = ((FactoryExpression<?>) select).getArgs();
        } else if (select != null) {
            sqlSelect = ImmutableList.of(select);
        } else {
            sqlSelect = ImmutableList.of();
        }

        // with
        if (hasFlags) {
            List<Expression<?>> withFlags = Lists.newArrayList();
            boolean recursive = false;
            for (QueryFlag flag : flags) {
                if (flag.getPosition() == Position.WITH) {
                    if (flag.getFlag() == SQLTemplates.RECURSIVE) {
                        recursive = true;
                        continue;
                    }
                    withFlags.add(flag.getFlag());
                }
            }
            if (!withFlags.isEmpty()) {
                if (recursive) {
                    append(templates.getWithRecursive());
                } else {
                    append(templates.getWith());
                }
                handle(", ", withFlags);
                append("\n");
            }
        }

        // start
        if (hasFlags) {
            serialize(Position.START, flags);
        }

        // select
        Stage oldStage = stage;
        stage = Stage.SELECT;
        if (forCountRow) {
            append(templates.getSelect());
            if (hasFlags) {
                serialize(Position.AFTER_SELECT, flags);
            }

            if (!metadata.isDistinct()) {
                append(templates.getCountStar());
                if (!groupBy.isEmpty()) {
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelect());
                    append("1 as one ");
                    suffix = ") internal";
                }

            } else {
                List<? extends Expression<?>> columns;
                if (sqlSelect.isEmpty()) {
                    columns = getIdentifierColumns(joins, !templates.isCountDistinctMultipleColumns());
                } else {
                    columns = sqlSelect;
                }
                if (!groupBy.isEmpty()) {
                    // select count(*) from (select distinct ...)
                    append(templates.getCountStar());
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelectDistinct());
                    handleSelect(COMMA, columns);
                    suffix = ") internal";
                } else if (columns.size() == 1) {
                    append(templates.getDistinctCountStart());
                    handle(columns.get(0));
                    append(templates.getDistinctCountEnd());
                } else if (templates.isCountDistinctMultipleColumns()) {
                    append(templates.getDistinctCountStart());
                    append("(").handleSelect(COMMA, columns).append(")");
                    append(templates.getDistinctCountEnd());
                } else {
                    // select count(*) from (select distinct ...)
                    append(templates.getCountStar());
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelectDistinct());
                    handleSelect(COMMA, columns);
                    suffix = ") internal";
                }
            }

        } else if (!sqlSelect.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(templates.getSelect());
            } else {
                append(templates.getSelectDistinct());
            }
            if (hasFlags) {
                serialize(Position.AFTER_SELECT, flags);
            }

            handleSelect(COMMA, sqlSelect);
            
        } else {
        	append(templates.getSelect());
        	 if (hasFlags) {
                 serialize(Position.AFTER_SELECT, flags);
             }

             handleSelect(COMMA, sqlSelect);
        	 append(" * ");
        }
        if (hasFlags) {
            serialize(Position.AFTER_PROJECTION, flags);
        }

        // from
        stage = Stage.FROM;
        serializeSources(joins);

        // where
        if (hasFlags) {
            serialize(Position.BEFORE_FILTERS, flags);
        }
        if (where != null) {
            stage = Stage.WHERE;
            append(templates.getWhere()).handle(where);
        }
        if (hasFlags) {
            serialize(Position.AFTER_FILTERS, flags);
        }

        // group by
        if (hasFlags) {
            serialize(Position.BEFORE_GROUP_BY, flags);
        }
        if (!groupBy.isEmpty()) {
            stage = Stage.GROUP_BY;
            append(templates.getGroupBy()).handle(COMMA, groupBy);
        }
        if (hasFlags) {
            serialize(Position.AFTER_GROUP_BY, flags);
        }

        // having
        if (hasFlags) {
            serialize(Position.BEFORE_HAVING, flags);
        }
        if (having != null) {
            stage = Stage.HAVING;
            append(templates.getHaving()).handle(having);
        }
        if (hasFlags) {
            serialize(Position.AFTER_HAVING, flags);
        }

        // order by
        if (hasFlags) {
            serialize(Position.BEFORE_ORDER, flags);
        }
        if (!orderBy.isEmpty() && !forCountRow) {
            stage = Stage.ORDER_BY;
            append(templates.getOrderBy());
            handleOrderBy(orderBy);
        }
        if (hasFlags) {
            serialize(Position.AFTER_ORDER, flags);
        }

        // modifiers
        if (!forCountRow && metadata.getModifiers().isRestricting() && !joins.isEmpty()) {
            stage = Stage.MODIFIERS;
            serializeModifiers(metadata, this);
            
        }

        if (suffix != null) {
            append(suffix);
        }

        // reset stage
        stage = oldStage;
        skipParent = oldSkipParent;
        inSubquery = oldInSubquery;
    }
    
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() != null) {
            context.handle(limitTemplate, mod.getLimit());
        } else if (limitRequired) {
            context.handle(limitTemplate, maxLimit);
        }
        if (mod.getOffset() != null) {
            context.handle(offsetTemplate, mod.getOffset());
        }
    }
    

}

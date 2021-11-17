package titan.lightbatis.pagehelper;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableSet;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;
import titan.lightbatis.result.PageList;

import java.util.List;

public class PageListHelper extends PageHelper {
    @Override
    public boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return super.skip(ms, parameterObject, rowBounds);
    }

    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        if (pageList instanceof PageList){
            return pageList;
        }
//        ImmutableSet.of()
        return super.afterPage(pageList, parameterObject, rowBounds);
    }
}

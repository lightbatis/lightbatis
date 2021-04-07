package titan.lightbatis.jsv.engine;

import com.eclipsesource.v8.JavaCallback;

public interface IRangeExpression extends JavaCallback {

    String toExpressionStr(String varName);

    DataRangeFunctions.RangeType getOperation();

    String toExpressionStr(String varName, String argA, String argB);

    String toExpression(String varName, String argA, String argB);

    String getTypeCode();
}

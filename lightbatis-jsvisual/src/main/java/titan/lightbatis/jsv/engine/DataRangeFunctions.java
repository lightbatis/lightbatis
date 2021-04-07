package titan.lightbatis.jsv.engine;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.google.common.collect.Range;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class DataRangeFunctions {
    public static enum RangeType{
        CloseOpen, Close, Open, OpenClose, GreaterThan, AtLeast, LessThan, AtMost, EQ, NEQ
    }

    private static List<IRangeExpression> ranges = new ArrayList<>();
    /**
     * x = a
     */
    public static final IRangeExpression _eq = new RangeFunction(RangeType.EQ, 2, "A");

    /**
     * x != a
     */
    public static final IRangeExpression _neq = new RangeFunction(RangeType.NEQ, 2, "B");

    /**
     * [a..b)  {x | a <= x < b}    closedOpen
     */
    public static final IRangeExpression _closeOpen = new RangeFunction(RangeType.CloseOpen, 3, "A1B0");
    /**
     * [a..b]  {x | a <= x <= b}   closed
     */
    public static final IRangeExpression _close = new RangeFunction(RangeType.Close, 3,"A1B1");
    /**
     * (a..b)  {x | a < x < b}     open
     */
    public static final IRangeExpression _open = new RangeFunction(RangeType.Open,3, "A0B0");
    /**
     * (a..b]  {x | a < x <= b}    openClosed
     */
    public static final IRangeExpression _openClose = new RangeFunction(RangeType.OpenClose, 3, "A0B1");
    /**
     * (a..+∞) {x | x > a}         greaterThan
     */
    public static final IRangeExpression _greaterThan = new RangeFunction(RangeType.GreaterThan, 2, "A0");
    /**
     * [a..+∞) {x | x >= a}        atLeast
     */
    public static final IRangeExpression _atLeast = new RangeFunction(RangeType.AtLeast, 2, "A1");
    /**
     * (-∞..b) {x | x < b}         lessThan
     */
    public static final IRangeExpression _lessThan = new RangeFunction(RangeType.LessThan, 2, "B0");
    /**
     * (-∞..b] {x | x <= b}        atMost
     */
    public static final IRangeExpression _atMost = new RangeFunction(RangeType.AtMost, 2, "B1");


    private static Double[] getDoubleValues(V8Array parameters) {
        Double[] values = new Double[parameters.length()];
        for (int i=0; i< parameters.length();i++){
            values[i] = parameters.getDouble(i);
        }

        return values;
    }

    private static Integer[] getIntegerValues(V8Array parameters) {
        Integer[] values = new Integer[parameters.length()];
        for (int i=0; i< parameters.length();i++){
            values[i] = parameters.getInteger(i);
        }
        return values;
    }

    private static int getParameterType(V8Array parameters) {
        int type = V8Value.INTEGER;
        for (int i=0; i< parameters.length();i++) {
            int typeId = parameters.getType(i);
            if (typeId == V8Value.STRING) {
            }
            if (typeId != type) {
                return typeId;
            }
        }
        return type;
    }

    private static Comparable[] getParameterValues(V8Array parameters) {
        Comparable[] values = new Comparable[parameters.length()];
        for (int i=0; i< parameters.length();i++){
            values[i] = (Comparable) parameters.get(i);
            values[i] = NumberUtils.createDouble(values[i].toString());
        }
        return values;
    }

    static class RangeFunction implements IRangeExpression {
        private RangeType type = null;
        private int parameterCount = 3;
        private String typeCode = null;
        RangeFunction(RangeType type, int parameterCount, String typeCode) {
            this.type = type;
            this.parameterCount = parameterCount;
            ranges.add(this);
            this.typeCode = typeCode;
        }
        @Override
        public Object invoke(V8Object receiver, V8Array parameters) {
            if (parameters.length() != parameterCount) {
                throw new RuntimeException("参数的个数必须是 3 个， 如：[1,2,3]");
            }
            Comparable[] values = getParameterValues(parameters);
            return call(getRangeType(), values);
        }
        protected boolean call(RangeType type, Comparable[] values)  {
            //如果检查数据类型。
            switch (type){
                case Open:
                    return Range.open(values[0], values[1]).contains(values[2]);
                case Close:
                    return Range.closed(values[0], values[1]).contains(values[2]);
                case OpenClose:
                    return Range.openClosed(values[0], values[1]).contains(values[2]);
                case CloseOpen:
                    return Range.closedOpen(values[0], values[1]).contains(values[2]);
                case GreaterThan:
                    return Range.greaterThan(values[0]).contains(values[1]);
                case LessThan:
                    return Range.lessThan(values[0]).contains(values[1]);
                case AtLeast:
                    return Range.atLeast(values[0]).contains(values[1]);
                case AtMost:
                    return Range.atMost(values[0]).contains(values[1]);
            }
            return false;
        }

        protected RangeType getRangeType(){
            return type;
        }

        public String getTypeCode() {
            return typeCode;
        }

        @Override
        public String toExpressionStr(String varName) {
            StringBuilder builder = new StringBuilder();
            switch (type){
                case EQ:
                    builder.append(String.format(" %s = a", varName));
                    break;
                case NEQ:
                    builder.append(String.format(" %s != a", varName));
                    break;
                case Open:
                    builder.append(String.format(" a < %s < b", varName));
                    break;
                case Close:
                    builder.append(String.format(" a <= %s <= b", varName));
                    break;
                case OpenClose:
                    builder.append(String.format(" a < %s <= b", varName));
                    break;
                case CloseOpen:
                    builder.append(String.format(" a <= %s < b", varName));
                    break;
                case GreaterThan:
                    builder.append(String.format(" %s > a", varName));
                    break;
                case LessThan:
                    builder.append(String.format(" %s < b", varName));
                    break;
                case AtLeast:
                    builder.append(String.format(" %s >= a", varName));
                    break;
                case AtMost:
                    builder.append(String.format(" %s <= b", varName));
                    break;
            }
            return builder.toString();
        }

        @Override
        public RangeType getOperation() {
            return type;
        }

        @Override
        public String toExpression(String varName, String argA, String argB) {
            String script = null;
            switch (type){
                case EQ:
                    script =String.format(" %s == %s", varName, argA);
                    break;
                case NEQ:
                    script = String.format(" %s != %s", varName, argA);
                    break;
                case Open:
                    script = String.format(" (%s < %s) && (%s < %s) ", argA, varName, varName, argB);
                    break;
                case Close:
                    script = String.format(" (%s <= %s) && (%s <= %s) ", argA, varName, varName, argB);
                    break;
                case OpenClose:
                    script = String.format(" (%s < %s) && (%s <= %s) ", argA, varName, varName, argB);
                    break;
                case CloseOpen:
                    script = String.format(" (%s <= %s) && (%s < %s) ", argA, varName, varName, argB);
                    break;
                case GreaterThan:
                    script = String.format(" %s > %s ", varName,argA);
                    break;
                case LessThan:
                    script = String.format(" %s < %s ", varName, argA);
                    break;
                case AtLeast:
                    script = String.format(" %s >= %s ", varName, argA);
                    break;
                case AtMost:
                    script = String.format(" %s <= %s ", varName, argA);
                    break;
            }
            return script;
        }

        @Override
        public String toExpressionStr(String varName, String argA, String argB) {
            String script = null;
            switch (type){
                case EQ:
                    script =String.format(" %s == %s", varName, argA);
                    break;
                case NEQ:
                    script = String.format(" %s != %s", varName, argA);
                    break;
                case Open:
                    script = String.format(" %s < %s < %s ", argA, varName, argB);
                    break;
                case Close:
                    script = String.format(" %s <= %s <= %s ", argA, varName, argB);
                    break;
                case OpenClose:
                    script = String.format(" %s < %s <= %s ", argA, varName, argB);
                    break;
                case CloseOpen:
                    script = String.format(" %s <= %s < %s ", argA, varName, argB);
                    break;
                case GreaterThan:
                    script = String.format(" %s > %s ", varName,argA);
                    break;
                case LessThan:
                    script = String.format(" %s < %s ", varName, argA);
                    break;
                case AtLeast:
                    script = String.format(" %s >= %s ", varName, argA);
                    break;
                case AtMost:
                    script = String.format(" %s <= %s ", varName, argA);
                    break;
            }
            return script;
        }

    }



    public static IRangeExpression findRangeExpression(String name) {
        IRangeExpression rangeExpression = null;
        for (IRangeExpression ire: ranges) {
            if (ire.getOperation().name().equals(name)) {
                rangeExpression = ire;
                break;
            }
        }
        return rangeExpression;
    }

    public static IRangeExpression getRangeExpression(String code) {
        IRangeExpression rangeExpression = null;
        for (IRangeExpression ire: ranges) {
            if (ire.getTypeCode().equals(code)) {
                rangeExpression = ire;
                break;
            }
        }
        return rangeExpression;
    }

}

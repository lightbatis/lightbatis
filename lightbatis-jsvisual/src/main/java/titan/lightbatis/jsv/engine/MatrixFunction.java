package titan.lightbatis.jsv.engine;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

public class MatrixFunction implements JavaCallback {
    @Override
    public Object invoke(V8Object receiver, V8Array parameters) {
        //参数列表
        V8Array paramNames = parameters.getArray(0);
        //参数值列表
        V8Array paramValues = parameters.getArray(1);
        //参数的 n-1 个是参数条件值， 最后一个是符合条件后确定的值。
        int paramValueIndex = paramNames.length() - 1;
        // 循环每一行的参数数据。
        for (int i=0;i<paramValues.length();i++){
            V8Array row = paramValues.getArray(i);
            Boolean matchAllTrue = null;
            // 循环参数数据并确定这一行的值。
            for (int j=0;j<paramValueIndex;j++) {
                String name = paramNames.getString(j);
                //取出环境里的变量的值。
                Object contextVal = receiver.get(name);
                //取出当前数组的值。
                Object currentValue = row.get(j);
                //从数组里返回的值，有两种类型，一种是 Boolean, 另一种是 Number
                Boolean cellValue = false;
                if (currentValue instanceof Boolean) {
                    cellValue = (Boolean)currentValue;
                } else {
                    cellValue = contextVal.equals(currentValue);
                }
                //System.out.println(name + " = " + contextVal + " , " + currentValue);
                //确定两个值是否相等。
                if (matchAllTrue == null) {
                    matchAllTrue = cellValue;
                } else {
                    matchAllTrue = matchAllTrue && cellValue;
                }
            }
            //如果当前行的 n-1 的值都是 true, 说明本行已经匹配成功，将最后一个参数返回。
            boolean result = matchAllTrue != null && matchAllTrue.booleanValue();
            if (result) {
                // 如果找到，给最后一个参数赋值。
                String localVarName = paramNames.getString(paramValueIndex);
                // 取最后一行的值。
                Object value = row.get(paramValueIndex);
                //System.out.println(String.format("MatrixFunction var = %s, value = %s", localVarName, value));
                V8EngineServer.addValue(receiver, localVarName, value);
                return value;
            }
        }
        return null;
    }
}

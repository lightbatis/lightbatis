package titan.lightbatis.dataset.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;
import titan.lightbatis.table.ColumnSchema;

import java.sql.Timestamp;

public class ConverterUtils {

    public static <T> T convert(Object value, Class<T> targetClz) {

        ConversionService conversionService = ApplicationConversionService.getSharedInstance();
        if (conversionService.canConvert(value.getClass(), targetClz)) {
            return conversionService.convert(value, targetClz);
        }else {
            Converter converter = ConvertUtils.lookup(value.getClass());
            T obj =  (T)converter.convert(targetClz, value);
            return obj;
        }
    }
    public static void main(String[] args) {
        String str = "2022-11-17 12:05:11"; //2022-11-17T12:05:11
        Timestamp timestamp = convert(str, Timestamp.class);
        System.out.println(timestamp);
    }
}

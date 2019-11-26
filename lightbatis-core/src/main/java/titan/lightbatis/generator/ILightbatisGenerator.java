package titan.lightbatis.generator;

import titan.lightbatis.annotations.GeneratedEvent;

/**
 * 自动生成器的接口,在执行 {@link GeneratedEvent} 指定的事件时，将调用此接口完成默认的一些操作。
 * @author lifei114@126.com
 */
public interface ILightbatisGenerator<T> {

    Object generated(String mappedStatementId, T entity, String property);

}

package titan.lightbatis.generator;

import titan.lightbatis.utils.RevisionException;

import java.sql.Timestamp;

/**
 * 联系作者扫描以下二维码：
 * <p>
 * █████████████████████████████████████
 * █████████████████████████████████████
 * ████ ▄▄▄▄▄ █▀█ █▄▄▀▀ ▀▄█ █ ▄▄▄▄▄ ████
 * ████ █   █ █▀▀▀█ ▀▀ ████▄█ █   █ ████
 * ████ █▄▄▄█ █▀ █▀▀▄▀▀▄ ▀█ █ █▄▄▄█ ████
 * ████▄▄▄▄▄▄▄█▄▀ ▀▄█▄▀▄█ ▀ █▄▄▄▄▄▄▄████
 * ████ ▄ ▄ ▀▄  ▄▀▄▀▄ █ █▀ █ ▀ ▀▄█▄▀████
 * ████▄ ▄   ▄▄██▄█▀▄  ▄▄▀█ ▄▀  ▀█▀█████
 * ████ ▀▄▄█ ▄▄▄ ▄█▄▄▀▄▄█▀ ▀▀▀▀▀▄▄█▀████
 * █████ ▀ ▄ ▄▄█▀  ▄██ █▄▄▀  ▄ ▀▄▄▀█████
 * ████▀▄  ▄▀▄▄█▄▀▄▀█▄▀▀ ▄ ▀▀▀ ▀▄ █▀████
 * ████ ██▄▄▄▄█▀▄▀█▀█▀▄▀█ ▀▄▄█▀██▄▀█████
 * ████▄███▄█▄█▀▄ █▄▀▄▄▀▄██ ▄▄▄ ▀   ████
 * ████ ▄▄▄▄▄ █▄█▄ ▄▄  ██▄  █▄█ ▄▄▀█████
 * ████ █   █ █ ▀█▄ ▀ ▄▄▀▀█ ▄▄▄▄▀ ▀ ████
 * ████ █▄▄▄█ █ ▄▀███▀▄▄▄▄▄ █▄▀  ▄ █████
 * ████▄▄▄▄▄▄▄█▄███▄█▄▄▄▄▄██▄█▄▄▄▄██████
 * █████████████████████████████████████
 * █████████████████████████████████████
 * <p>
 * Lightbatis 基于规范约定的快速数据操作层
 *
 * @Author lifei114@126.com
 */
public class DefaultRevisionGenerator implements IRevisionGenerator {
    @Override
    public Object next(Object current) throws RevisionException {
        if (current == null) {
            throw new RevisionException("当前版本号为空!");
        }
        if (current instanceof Integer) {
            return (Integer) current + 1;
        } else if (current instanceof Long) {
            return (Long) current + 1L;
        } else if (current instanceof Timestamp) {
            return new Timestamp(System.currentTimeMillis());
        } else {
            throw new RevisionException("只支持 Integer, Long 和 java.sql.Timestamp 类型的版本号");
        }
    }
}

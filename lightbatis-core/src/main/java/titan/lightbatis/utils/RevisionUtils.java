package titan.lightbatis.utils;

import titan.lightbatis.generator.DefaultRevisionGenerator;
import titan.lightbatis.generator.IRevisionGenerator;

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
public class RevisionUtils {

    private static IRevisionGenerator defaultRevision = new DefaultRevisionGenerator();

    public static Object next(Object current) throws RevisionException {
        return defaultRevision.next(current);
    }
}

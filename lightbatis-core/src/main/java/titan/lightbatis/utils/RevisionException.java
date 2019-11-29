package titan.lightbatis.utils;

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
public class RevisionException extends Exception {
    public RevisionException() {
        super();
    }

    public RevisionException(String message) {
        super(message);
    }

    public RevisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RevisionException(Throwable cause) {
        super(cause);
    }

    public RevisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

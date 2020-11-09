package titan.lightbatis.common;

import lombok.Data;

@Data
public class CommonPageResult<T> extends CommonResult<T> {
    private int totalSize = 0;
    protected CommonPageResult(long code, String message, T data) {
        super(code, message, data);
    }
}

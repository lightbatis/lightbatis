package titan.lightbatis.common;

public class BaseController {

    protected <T> CommonResult<T> success(T data) {
        return CommonResult.success(data);
    }
    protected <T> CommonResult<T> success(T data, String message) {
        return CommonResult.success(data, message);
    }


    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    protected <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     */
    protected <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    protected <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    protected <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

}

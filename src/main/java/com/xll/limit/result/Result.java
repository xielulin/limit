package com.xll.limit.result;

import com.xll.limit.constants.Constant;
import lombok.Data;

/**
 * @author xielulin
 * @create 2018-11-18 15:37
 * @desc
 **/
@Data
public class Result<T> {
    private T data;

    private String status;

    private String desc;

    public Result() {
    }

    public Result(String status) {
        this.status = status;
    }

    public Result(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public Result(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Result(String status, T data, String desc) {
        this.status = status;
        this.data = data;
        this.desc = desc;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(Constant.ResultConstant.SUCCESS, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(Constant.ResultConstant.SUCCESS, null, null);
    }

    public static <T> Result<T> ok(String message) {
        return new Result<>(Constant.ResultConstant.SUCCESS, null, message);
    }

    public static <T> Result<T> ok(T data, String desc) {
        return new Result<>(Constant.ResultConstant.SUCCESS, data, desc);
    }

    public static Result warn(String desc) {
        return new Result<>(Constant.ResultConstant.FAIL, desc);
    }

    public static <T> Result<T> warn(T data, String desc) {
        return new Result<>(Constant.ResultConstant.FAIL, data, desc);
    }
}

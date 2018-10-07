package com.vera.shared.model;

/**
 * Rest 交互过程中产生的一些结果类型定义
 * @author ychost
 * @date 2018-10-05
 */
public final class RestCode {
    // 成功
    public static int success=0;
    // 未知错误
    public static int unknownError=1000;
    // 请求参数错误
    public static int reqParamError=1001;
    // 请求结果为空
    public static int resIsNull=1002;
    // 处理过程中发生了异常
    public static int exeception=1003;
    // 权限禁止
    public static int permissionForbid=1004;
    // 操作失败
    public static int failed = 1005;
    // 某些批量操作失败了一部分
    public static int someFailed=1006;
}

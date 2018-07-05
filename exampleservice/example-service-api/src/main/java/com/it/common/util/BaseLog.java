package com.it.common.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaowq
 * @description 日志输出
 * @create 2017-07-26 下午 01:42
 **/
public class BaseLog {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 输出日志长度
     */
    private static final int LOG_LENGTH = 1000;

    /**
     * @param remark
     * @param params
     * @return
     * @author zhaowq
     * @description 方法执行前日志输出
     * @create 2017-08-18
     */
    protected StringBuilder beforeLog(String remark, Object... params) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(remark)
                    .append(", 执行开始, 参数: ");
            if (params == null || params.length == 0) {
                sb.append("为空");
                return sb;
            }
            sb.append("[");
            int length = params.length;
            for (int i = 0; i < length; i++) {
                sb.append(JSON.toJSONString(params[i]));
                if (i != (length - 1)) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        } catch (Exception e) {
            sb.append(", 出现异常, 信息[")
                    .append(e.getMessage())
                    .append("]");
        } finally {
            logger.info(sb.toString());
        }
        return sb;
    }

    /**
     * @param sb          入参信息,与beforeLog执行结果对应
     * @param returnParam
     * @param startTime
     * @author zhaowq
     * @description 方法执行后日志输出
     * @create 2017-08-18
     */
    protected void afterLog(StringBuilder sb, Object returnParam, long startTime) {
        if (sb == null) {
            sb = new StringBuilder("入参为空");
        }
        try {
            sb.append(", 执行结束")
                    .append(", 出参[");
            String returnParamStr = (returnParam == null ? "" : JSON.toJSONString(returnParam));
            if (returnParamStr.length() > LOG_LENGTH) {
                returnParamStr = returnParamStr.substring(0, LOG_LENGTH);
            }
            sb.append(returnParamStr)
                    .append("]");
        } catch (Exception e) {
            sb.append(", 出现异常, 信息[")
                    .append(e.getMessage())
                    .append("]");
        } finally {
            sb.append(", 耗时")
                    .append(System.currentTimeMillis() - startTime)
                    .append("毫秒");
            logger.info(sb.toString());
        }
    }

    /**
     * @param methodName
     * @param interParam
     * @param returnParam
     * @param startTime
     * @author zhaowq
     * @description 方法执行统一输出日志
     * @create 2017-07-26 下午 01:42
     */
    protected void printlnLog(String methodName, Object interParam, Object returnParam, long startTime) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("方法")
                    .append(methodName)
                    .append("执行开始")
                    .append(", 入参[");
            String interParamStr = (interParam == null ? "" : JSON.toJSONString(interParam));
            if (interParamStr.length() > LOG_LENGTH) {
                interParamStr = interParamStr.substring(0, LOG_LENGTH);
            }
            sb.append(interParamStr)
                    .append("]")
                    .append(", 执行结束")
                    .append(", 出参[");
            String returnParamStr = (returnParam == null ? "" : JSON.toJSONString(returnParam));
            if (returnParamStr.length() > LOG_LENGTH) {
                returnParamStr = returnParamStr.substring(0, LOG_LENGTH);
            }
            sb.append(returnParamStr)
                    .append("]");
        } catch (Exception e) {
            sb.append(", 出现异常, 信息[")
                    .append(e.getMessage())
                    .append("]");
        } finally {
            sb.append(", 耗时")
                    .append(System.currentTimeMillis() - startTime)
                    .append("毫秒");
            logger.info(sb.toString());
        }
    }
}

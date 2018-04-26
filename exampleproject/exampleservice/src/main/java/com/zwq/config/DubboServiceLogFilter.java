package com.zwq.config;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.zwq.common.util.BaseLog;
import org.assertj.core.util.Arrays;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaowq
 * @description dubbo filter
 * @create 2017-12-21 下午 06:13
 **/
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class DubboServiceLogFilter extends BaseLog implements Filter {
    private final static ExecutorService executorService = new ThreadPoolExecutor(5, 100, 0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy());


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long startTime = System.currentTimeMillis();
        try {
            URL url = invoker.getUrl();
            String methodName = invocation.getMethodName();
            logger.info("访问方法名:{}", methodName);
            String name = invocation.getInvoker().getInterface().getName();
            logger.info("访问接口名:{}", name);
            String clientIp = RpcContext.getContext().getRemoteHost();
            logger.info("访问ip为:{}", clientIp);
            StringBuilder param = new StringBuilder();
            Object[] arguments = invocation.getArguments();
            // StringUtils.toArgumentString(invocation.getArguments());
            if (!Arrays.isNullOrEmpty(arguments)) {
                for (int i = 0; i < arguments.length; i++) {
                    param.append(JSON.toJSONString(arguments[i]));
                    if (i != arguments.length - 1) {
                        param.append(" | ");
                    }
                }
            }
            logger.info("入参:{}", JSON.toJSONString(param));
        } catch (Exception e) {
            logger.error("dubbo filter error");
        }
        Result invoke = invoker.invoke(invocation);
        Object value = invoke.getValue();
        logger.info("出参:{},耗时{}毫秒", JSON.toJSONString(value), System.currentTimeMillis() - startTime);
        return invoke;
    }
}

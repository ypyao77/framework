/*
 * The MIT License (MIT)
 * Copyright © 2019 <sky>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sky.framework.threadpool;

import com.sky.framework.threadpool.core.CommonThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 异步线程池自动加载配置类
 * 1.异步前后处理器线程池,灵活配置参数,线程池创建线程名称配置
 * 2.内存限制检测
 * 3.轻量化线程池数量管理
 * 4.自定义线程池拒绝策略实现
 * * *检测是否因并发造成线程池执行拒绝策略
 * * *队列达到上限则打印详细日志
 * * *轻量级管理线程池数量
 * 5.自定义线程创建方法
 * * *扩展内存检测
 * * *更严格控制是否开启 最大线程数限定数线程
 * 6.由优先使用队列转变为优先使用最大线程值
 *
 * @author
 */
@Configuration
@EnableConfigurationProperties(value = AsyncThreadPoolProperties.class)
@ConditionalOnProperty(prefix = "asyncthreadpool", value = "enabled", matchIfMissing = true)
@Slf4j
public class AsyncThreadPoolAutoConfiguration {

    @Resource
    private AsyncThreadPoolProperties asyncThreadPoolProperties;

    @PostConstruct
    public void init() {
        if (CommonThreadPool.getThreadPool() == null) {
            log.info("sky framework CommonThreadPool initialized successfully ! :{}" + asyncThreadPoolProperties);
            CommonThreadPool.initThreadPool(asyncThreadPoolProperties);
        }
    }

    @PreDestroy
    public void destory() {
        log.info("sky framework CommonThreadPool shutdown begin ! ");
        boolean flag = CommonThreadPool.shutDown();
        log.info("sky framework CommonThreadPool shutdown status is:{} " + flag);
    }
}

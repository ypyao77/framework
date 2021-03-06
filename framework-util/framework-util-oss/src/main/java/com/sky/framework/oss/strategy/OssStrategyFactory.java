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
package com.sky.framework.oss.strategy;

import com.sky.framework.common.LogUtils;
import com.sky.framework.oss.property.AliyunOssProperties;
import com.sky.framework.oss.property.OssProperties;
import com.sky.framework.oss.strategy.aliyun.AliyunOssStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * simple factory
 *
 * @author
 */
@Slf4j
public class OssStrategyFactory {

    /**
     * All OssStrategyFactory Object
     */
    public static Set<OssStrategyAdapter> container = new HashSet<>();

    /**
     *
     */
    private static OssProperties ossProperties;

    /**
     * init factory required properties
     *
     * @param ossProperties
     */
    public static void init(OssProperties ossProperties) {
        OssStrategyFactory.ossProperties = ossProperties;
    }

    /**
     * @param strategy
     */
    public static OssStrategyAdapter createOssStrategy(OssStrategyEnum strategy) {
        OssStrategy ossStrategy;
        if (AliyunOssStrategy.NAME.equals(strategy.getKey())) {
            ossStrategy = new AliyunOssStrategy(ossProperties);
        } else {
            throw new IllegalArgumentException("file strategy name:" + strategy.getKey() + " not support");
        }
        OssStrategyAdapter ossStrategyAdapter = new OssStrategyAdapter(ossStrategy);
        container.add(ossStrategyAdapter);
        return ossStrategyAdapter;
    }

    /**
     * destroy all oss client
     */
    public static void destroyAll() {
        for (OssStrategyAdapter ossStrategyAdapter : container) {
            try {
                ossStrategyAdapter.getOssStrategy().close();
            } catch (Exception e) {
                LogUtils.error(log, "close oss client error !", e.getMessage());
            }
        }
    }
}

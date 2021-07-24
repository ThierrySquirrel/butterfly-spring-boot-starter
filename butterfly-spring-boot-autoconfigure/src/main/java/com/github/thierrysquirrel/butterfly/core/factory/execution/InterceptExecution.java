/**
 * Copyright 2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thierrysquirrel.butterfly.core.factory.execution;

import com.github.thierrysquirrel.butterfly.annotation.Butterfly;
import com.github.thierrysquirrel.butterfly.annotation.Flutter;
import com.github.thierrysquirrel.butterfly.core.factory.FilterFactory;
import com.github.thierrysquirrel.butterfly.error.ButterflyException;
import com.github.thierrysquirrel.pine.netty.core.client.factory.ClientFactory;
import com.github.thierrysquirrel.pine.netty.core.client.factory.PineRequestContextFactory;
import com.github.thierrysquirrel.pine.netty.domain.PineRequestContext;
import lombok.extern.slf4j.Slf4j;


/**
 * ClassName: InterceptExecution
 * Description:
 * date: 2019/10/19 19:06
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Slf4j
public class InterceptExecution {
    private InterceptExecution() {
    }

    public static Object execution(Butterfly butterfly, Flutter coordinate, Object[] args) throws ButterflyException {
        String serviceName = butterfly.value ();
        String serviceUrl = ProduceNameFactoryExecution.getServiceUrl (serviceName);
        String serviceCoordinate = coordinate.value ();
        PineRequestContext rpcRequest = PineRequestContextFactory.createRpcRequest (serviceCoordinate, args);
        FilterFactory.butterflyFilter(rpcRequest);
        try {
            PineRequestContext request = ClientFactory.request (serviceUrl, rpcRequest);
            return request.getPineResponse ().getData ();
        } catch (Exception e) {
            log.error ("InterceptExecution execution Error", e);
        }
        throw new ButterflyException ("InterceptExecution execution Error");
    }
}

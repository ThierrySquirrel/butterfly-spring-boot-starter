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

package com.github.thierrysquirrel.butterfly.netty.service.thread.execution;

import com.github.thierrysquirrel.butterfly.core.factory.PineServiceUrlFactory;
import com.github.thierrysquirrel.butterfly.core.factory.ProduceNameFactory;
import com.github.thierrysquirrel.butterfly.netty.service.thread.AbstractGetPineServiceProducerThread;
import com.github.thierrysquirrel.pine.netty.core.client.factory.ClientFactory;
import com.github.thierrysquirrel.pine.netty.core.client.factory.PineRequestContextFactory;
import com.github.thierrysquirrel.pine.netty.domain.PineRequestContext;
import com.github.thierrysquirrel.pine.netty.domain.PineResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ClassName: GetPineServiceProducerThreadExecution
 * Description:
 * date: 2019/10/19 17:02
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Slf4j
public class GetPineServiceProducerThreadExecution extends AbstractGetPineServiceProducerThread {

    public GetPineServiceProducerThreadExecution(String pineServiceUrl) {
        super (pineServiceUrl);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void getPineServiceProducerName(String pineServiceUrl) {
        String serviceUrl = PineServiceUrlFactory.getPineServiceUrl (pineServiceUrl);
        List<String> produceNameList = ProduceNameFactory.getProduceNameList ();
        for (String produceName : produceNameList) {
            PineRequestContext byProducersNameGetUrlsRequest = PineRequestContextFactory.createByProducersNameGetUrlsRequest (produceName);
            try {
                PineRequestContext request = ClientFactory.request (serviceUrl, byProducersNameGetUrlsRequest);
                PineResponse pineResponse = request.getPineResponse ();
                Object data = pineResponse.getData ();
                if (data != null) {
                    ProduceNameFactory.setProduceNameUrl (produceName, (List<String>) data);
                }
            } catch (NullPointerException nullPointerException) {
                log.warn (produceName + "Non-existent");
            } catch (Exception e) {
                log.error ("GetPineServiceProducerName Error", e);
                PineServiceUrlFactory.nextPineServiceUrl (pineServiceUrl);
            }
        }
    }
}

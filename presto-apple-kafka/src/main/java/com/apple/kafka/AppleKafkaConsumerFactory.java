/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apple.kafka;

import com.apple.pie.queue.kafka.client.KafkaClientUtils;
import io.prestosql.plugin.kafka.KafkaConsumerFactory;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.inject.Inject;

import java.util.Map;

public class AppleKafkaConsumerFactory
        extends KafkaConsumerFactory
{
    private final Map<String, Object> configs;

    @Inject
    public AppleKafkaConsumerFactory(AppleKafkaConfig kafkaConfig)
    {
        super(kafkaConfig);
        configs = kafkaConfig.getConfigs();
    }

    @Override
    public KafkaConsumer<byte[], byte[]> create()
    {
        return KafkaClientUtils.createConsumer(configs);
    }
}

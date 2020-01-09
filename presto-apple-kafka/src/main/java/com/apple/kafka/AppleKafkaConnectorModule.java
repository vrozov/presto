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

import com.google.inject.Binder;
import com.google.inject.Scopes;
import io.prestosql.decoder.DecoderModule;
import io.prestosql.plugin.kafka.KafkaConfig;
import io.prestosql.plugin.kafka.KafkaConnectorModule;
import io.prestosql.plugin.kafka.KafkaConsumerFactory;
import io.prestosql.plugin.kafka.KafkaMetadata;
import io.prestosql.plugin.kafka.KafkaRecordSetProvider;
import io.prestosql.plugin.kafka.KafkaSplitManager;
import io.prestosql.plugin.kafka.KafkaTopicDescription;
import io.prestosql.spi.type.Type;

import static io.airlift.configuration.ConfigBinder.configBinder;
import static io.airlift.json.JsonBinder.jsonBinder;
import static io.airlift.json.JsonCodecBinder.jsonCodecBinder;

public class AppleKafkaConnectorModule
        extends KafkaConnectorModule
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(KafkaMetadata.class).in(Scopes.SINGLETON);
        binder.bind(KafkaSplitManager.class).in(Scopes.SINGLETON);
        binder.bind(KafkaRecordSetProvider.class).in(Scopes.SINGLETON);

        binder.bind(KafkaConsumerFactory.class).to(AppleKafkaConsumerFactory.class).in(Scopes.SINGLETON);

        configBinder(binder).bindConfig(AppleKafkaConfig.class);
        binder.bind(KafkaConfig.class).to(AppleKafkaConfig.class);

        jsonBinder(binder).addDeserializerBinding(Type.class).to(TypeDeserializer.class);
        jsonCodecBinder(binder).bindJsonCodec(KafkaTopicDescription.class);

        binder.install(new DecoderModule());
    }
}

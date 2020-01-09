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

import io.airlift.configuration.Config;
import io.prestosql.plugin.kafka.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

public class AppleKafkaConfig
        extends KafkaConfig
{
    private static final String KAFFE_CONNECT = "pie.queue.kaffe.connect";

    private String kaffeConnect;
    private String namespace;
    private String customer;
    private String environment;
    private String group;
    private String client;
    private String groupId;

    private Map<String, Object> configs;

    public AppleKafkaConfig()
    {
        this.configs = new HashMap<>();
    }

    @NotNull
    public String getKaffeConnect()
    {
        return this.kaffeConnect;
    }

    @Config(KAFFE_CONNECT)
    public AppleKafkaConfig setKaffeConnect(String uri)
    {
        this.kaffeConnect = uri;
        this.configs.put(KAFFE_CONNECT, uri);
        return this;
    }

    @NotNull
    public String getNamespace()
    {
        return this.namespace;
    }

    @Config("namespace")
    public AppleKafkaConfig setNamespace(String ns)
    {
        this.namespace = ns;
        return this;
    }

    @NotNull
    public String getCustomer()
    {
        return this.customer;
    }

    @Config("customer")
    public AppleKafkaConfig setCustomer(String c)
    {
        this.customer = c;
        return this;
    }

    @NotNull
    public String getEnvironment()
    {
        return this.environment;
    }

    @Config("environment")
    public AppleKafkaConfig setEnvironment(String env)
    {
        this.environment = env;
        return this;
    }

    @NotNull
    public String getGroup()
    {
        return this.group;
    }

    @Config("group")
    public AppleKafkaConfig setGroup(String g)
    {
        this.group = g;
        return this;
    }

    @NotNull
    public String getClient()
    {
        return this.client;
    }

    @Config("client")
    public AppleKafkaConfig setClient(String c)
    {
        this.client = c;
        return this;
    }

    @NotNull
    public String getGroupId()
    {
        return this.groupId;
    }

    @Config("group.id")
    public AppleKafkaConfig setGroupId(String gid)
    {
        this.groupId = gid;
        return this;
    }

    public Map<String, Object> getConfigs()
    {
        /* https://docs.aci.apple.com/kafka/client/quick_start.html */

        String kaffeClientId = String.format("%s.%s.%s.%s",
                this.getCustomer(), this.getGroup(), this.getEnvironment(), this.getClient());
        String kaffeNamespaceId = String.format("%s.%s.%s.%s",
                this.getCustomer(), this.getGroup(), this.getEnvironment(), this.getNamespace());

        // If not using PIE secrets, provide the file path of your client's private key
        /*
        String pieSecretsKey = "your secret key if using PIE Secrets"
        String kaffePrivateKeyPath = String.format("%s/%s",
        System.getenv("PLATFORM_SECRETS_PATH"), pieSecretsKey);
         */
        String kaffePrivateKeyPath = String.format("./%s.private.key.pem", kaffeClientId);

        /*
        String topicName = "test";
        PieTopicName pieTopic = new PieTopicName(PieNamespaceName.of(kaffeNamespaceId), topicName);
         */

        // configs.put(KAFFE_CONNECT, this.kaffeConnect);
        configs.put("pie.queue.kaffe.client.id", kaffeClientId);
        // configs.put(ConsumerConfig.CLIENT_ID_CONFIG, kaffeClientId);
        configs.put("pie.queue.kaffe.namespace.id", kaffeNamespaceId);
        configs.put("pie.queue.kaffe.client.private.key.location", kaffePrivateKeyPath);

        configs.put("pie.queue.kaffe.config.injection", true);

        // For more information on group id naming convention,
        // see https://docs.pie.apple.com/kafka/client/consumer_settings.html#consumer-group-convention-and-enforcement
        String groupId = String.format("%s.%s", kaffeClientId, this.getGroupId());
        configs.put(KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return this.configs;
    }
}

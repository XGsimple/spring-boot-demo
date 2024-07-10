package com.xkcoding.elasticsearch.hotel;

import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.xkcoding.elasticsearch.constants.HotelIndexConstants.MAPPING_TEMPLATE;

@SpringBootTest
class HotelIndexTest {
    private static final String INDEX = "hotel";

    private RestHighLevelClient client;

    @Test
    @DisplayName("文本分析")
    void testAnalyzeIndex() throws IOException {
        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(INDEX, "ik_smart", "如家酒店(北京良乡西路店)");
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        DetailAnalyzeResponse detail = response.detail();
        tokens.forEach(theToken -> System.out.println(theToken.getPosition() + ":" + theToken.getTerm()));
    }

    @Test
    @DisplayName("创建索引")
    void testCreateIndex() throws IOException {
        // 1.准备Request      PUT /hotel
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
        // 2.准备请求参数
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发送请求
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
    }

    @Test
    void testCreateIndex2() throws IOException {
        // 1.准备Request      PUT /hotel
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        // 2.准备请求参数
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    @DisplayName("判断索引是否存在")
    void testExistsIndex() throws IOException {
        // 1.准备Request
        GetIndexRequest request = new GetIndexRequest(INDEX);
        // 3.发送请求
        boolean isExists = client.indices().exists(request, RequestOptions.DEFAULT);

        System.out.println(isExists ? "存在" : "不存在");
    }

    @Test
    @DisplayName("删除索引")
    void testDeleteIndex() throws IOException {
        // 1.准备Request
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX);
        // 3.发送请求
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    @DisplayName("更新索引Mapping")
    void testUpdateMapping() throws IOException {
        PutMappingRequest request = new PutMappingRequest(INDEX);
        request.source(
            "{\n" + "  \"properties\": {\n" + "    \"newMessage\": {\n" + "      \"type\": \"text\"\n" + "    }\n" +
            "  }\n" + "}", XContentType.JSON);
        AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        boolean acknowledged = putMappingResponse.isAcknowledged();
    }

    @Test
    @DisplayName("获取索引Mapping")
    void testGetMapping() throws IOException {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(INDEX);
        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        Map<String, MappingMetadata> allMappings = getMappingResponse.mappings();
        MappingMetadata indexMapping = allMappings.get(INDEX);
        Map<String, Object> mapping = indexMapping.sourceAsMap();
        System.out.println(JSONUtil.toJsonStr(mapping));
    }

    @Test
    @DisplayName("获取索引 Field Mapping")
    void testGetFieldMapping() throws IOException {
        GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        request.indices(INDEX);
        request.fields("isAD", "business");
        GetFieldMappingsResponse response = client.indices().getFieldMapping(request, RequestOptions.DEFAULT);
        final Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetadata>> mappings = response.mappings();
        final Map<String, GetFieldMappingsResponse.FieldMappingMetadata> fieldMappings = mappings.get(INDEX);
        final GetFieldMappingsResponse.FieldMappingMetadata metadata = fieldMappings.get("isAD");
        final String fullName = metadata.fullName();
        final Map<String, Object> source = metadata.sourceAsMap();
        System.out.println(fullName + ",source" + JSONUtil.toJsonStr(source));
    }

    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.3.137:31364"),
                                                            HttpHost.create("http://192.168.3.138:31364")));
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
    }

}

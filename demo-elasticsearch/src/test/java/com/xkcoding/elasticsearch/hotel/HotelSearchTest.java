package com.xkcoding.elasticsearch.hotel;

import com.alibaba.fastjson.JSON;
import com.xkcoding.elasticsearch.pojo.HotelDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class HotelSearchTest {

    private RestHighLevelClient client;

    /**
     * 查询所有，有limit限制
     *
     * @throws IOException
     */
    @Test
    void testMatchAll() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        request.source().query(QueryBuilders.matchAllQuery());
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 全文检索，利用分词器对用户输入内容分词，然后去倒排索引库中匹配。
     *
     * @throws IOException
     */
    @Test
    void testMultiMatch() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        // request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));
        request.source().query(QueryBuilders.multiMatchQuery("外滩如家", "name", "brand", "city"));
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 全文检索，利用分词器对用户输入内容分词，然后去倒排索引库中匹配。
     *
     * @throws IOException
     */
    @Test
    void testMatch() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 复合查询
     *
     * @throws IOException
     */
    @Test
    void testBool() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
       /*
         BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 2.1.must
        boolQuery.must(QueryBuilders.termQuery("city", "杭州"));
        // 2.2.filter
        boolQuery.filter(QueryBuilders.rangeQuery("price").lte(250));
        */

        request.source()
               .query(QueryBuilders.boolQuery()
                                   .must(QueryBuilders.termQuery("city", "杭州"))
                                   .filter(QueryBuilders.rangeQuery("price").lte(250)));
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 排序和分页
     *
     * @throws IOException
     */
    @Test
    void testSortAndPage() throws IOException {
        int page = 2, size = 5;

        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        // 2.1.query
        request.source().query(QueryBuilders.matchAllQuery());
        // 2.2.排序sort
        request.source().sort("price", SortOrder.ASC);
        // 2.3.分页 from\size
        request.source().from((page - 1) * size).size(size);

        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 结果高亮
     *
     * @throws IOException
     */
    @Test
    void testHighlight() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        // 2.1.query
        request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));
        // 2.2.高亮， 搜索字段， 和高亮字段不一致时，设定requireFieldMatch=false
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 查询所有、模糊查询、分页查询、排序、高亮显示
     *
     * @return
     * @throws IOException
     */
    @Test
    void composeQueryTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");//里面可以放多个索引
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//构造搜索条件
        //此处可以使用QueryBuilders工具类中的方法
        //1.查询所有
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        //2.查询name中含有Java的
        sourceBuilder.query(QueryBuilders.multiMatchQuery("7天连锁", "name"));
        //3.分页查询
        sourceBuilder.from(0).size(5);
        //4.按照score正序排列
        sourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.ASC));
        //5.按照id倒序排列（score会失效返回NaN）
        sourceBuilder.sort(SortBuilders.fieldSort("_id").order(SortOrder.DESC));
        //6.给指定字段加上指定高亮样式
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name").preTags("<span style='color:red;'>").postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //获取总条数
        System.out.println(searchResponse.getHits().getTotalHits().value);
        //输出结果数据（如果不设置返回条数，大于10条默认只返回10条）
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println("分数:" + hit.getScore());
            Map<String, Object> source = hit.getSourceAsMap();
            System.out.println("index->" + hit.getIndex());
            System.out.println("id->" + hit.getId());
            for (Map.Entry<String, Object> s : source.entrySet()) {
                System.out.println(s.getKey() + "--" + s.getValue());
            }
        }
    }

    private void handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 4.1.总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("总条数：" + total);
        // 4.2.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for (SearchHit hit : hits) {
            // 4.4.获取source
            String json = hit.getSourceAsString();
            // 4.5.反序列化，非高亮的
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 4.6.处理高亮结果
            // 1)获取高亮map
            Map<String, HighlightField> map = hit.getHighlightFields();

            // 2）根据字段名，获取高亮结果
            HighlightField highlightField = map.get("name");
            if (Objects.nonNull(highlightField)) {
                // 3）获取高亮结果字符串数组中的第1个元素
                Text[] fragments = highlightField.getFragments();
                if (fragments != null && fragments.length > 0) {
                    String hName = highlightField.getFragments()[0].toString();
                    // 4）把高亮结果放到HotelDoc中
                    hotelDoc.setName(hName);
                }
            }
            // 4.7.打印
            System.out.println(hotelDoc);
        }
    }

    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.3.137:31364")));
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
    }

}

package com.xkcoding.elasticsearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Data
@NoArgsConstructor
@Document(indexName = "hotel", shards = 1, replicas = 0)
public class HotelEs {
    @Id
    private Long id;

    /**
     * copyTo = "all"，联合索引
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "all")
    private String name;

    @Field(type = FieldType.Keyword, index = false)
    private String address;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Integer)
    private Integer score;

    @Field(type = FieldType.Keyword, index = false, copyTo = "all")
    private String brand;

    @Field(type = FieldType.Keyword, index = false)
    private String city;

    @Field(type = FieldType.Keyword, index = false)
    private String starName;

    @Field(type = FieldType.Keyword, index = false, copyTo = "all")
    private String business;

    @GeoPointField
    private GeoPointField location;

    @Field(type = FieldType.Keyword, index = false)
    private String pic;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;
}

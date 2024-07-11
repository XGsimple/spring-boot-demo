package com.xkcoding.cache.redis.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JsonConverters {

    final static DateTimeFormatter DTF_LOCADATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    final static DateTimeFormatter DTF_LOCADATE = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

    public static JsonDeserializer<LocalDate> localDateDeserializer() {
        return new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
                String result = StringDeserializer.instance.deserialize(p, ctxt);
                System.out.println("LocalDateTimeJsonDeserializer -> " + result);
                return LocalDate.parse(result, DTF_LOCADATE);
            }
        };
    }

    public static JsonSerializer<LocalDate> localDateSerializer() {
        return new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
                String format = DTF_LOCADATE.format(value);
                System.out.println("LocalDateTimeJsonSerializer -> " + format);
                gen.writeString(format);
                //			gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            }
        };
    }

    public static JsonDeserializer<LocalDateTime> localDateTimeDeserializer() {
        return new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
                String result = StringDeserializer.instance.deserialize(p, ctxt);
                System.out.println("LocalDateTimeJsonDeserializer -> " + result);
                return LocalDateTime.parse(result, DTF_LOCADATETIME);
            }
        };
    }

    public static JsonSerializer<LocalDateTime> localDateTimeSerializer() {
        return new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
                String format = DTF_LOCADATETIME.format(value);
                System.out.println("LocalDateTimeJsonSerializer -> " + format);
                gen.writeString(format);
                //			gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            }
        };
    }
}

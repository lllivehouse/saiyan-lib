package co.saiyan.common.utils;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author larry
 * @createTime 2022/12/27
 * @description GsonFactory
 */
public class GsonFactory {

    public static Gson getGson() {
        String dateFormat = DateUtils.DEFAULT_FORMAT;
        return new GsonBuilder()
                // 当Map的key为复杂对象时,需要开启该方法
                .enableComplexMapKeySerialization()
                // 当字段值为空或null时，依然对该字段进行转换
                .serializeNulls()
                // 防止特殊字符出现乱码
                .disableHtmlEscaping()
                .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setDateFormat(dateFormat)
                .registerTypeAdapter(Long.class, new LongSerializer())
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new MapTypeAdapter())
                .registerTypeAdapter(Date.class, new DateDeserializer(dateFormat))
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer(dateFormat))
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer(dateFormat))
                .registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter())
                .registerTypeAdapter(Class.class, new ClassCodec())
                .create();
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {
        private String dateFormat;

        public DateDeserializer(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("This is not a primitive value");
            }

            String jsonStr = json.getAsString();
            if (Objects.nonNull(dateFormat)) {
                DateFormat format = new SimpleDateFormat(dateFormat);
                try {
                    return format.parse(jsonStr);
                } catch (ParseException e) {
                    // ignore
                }
            }
            return new Date(Long.parseLong(jsonStr));
        }
    }

    public static class LongSerializer implements JsonSerializer<Long> {

        @Override
        public JsonElement serialize(Long value, Type type, JsonSerializationContext jsonSerializationContext) {
            return (value == null ? JsonNull.INSTANCE : value > Integer.MAX_VALUE ? new JsonPrimitive(value.toString()) : new JsonPrimitive(value));
        }
    }

    public static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        private String dateFormat;

        public LocalDateTimeSerializer(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            String dateTimeFormat = dateFormat != null ? dateFormat : DateUtils.DEFAULT_FORMAT;
            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)));
        }
    }

    public static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        private String dateFormat;

        public LocalDateTimeDeserializer(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("This is not a primitive value");
            }

            String str = json.getAsString();
            String dateTimeFormat = dateFormat != null ? dateFormat : DateUtils.DEFAULT_FORMAT;
            return DateUtils.stringToLocalDateTime(str, dateTimeFormat);
        }
    }

    public static class MapTypeAdapter extends TypeAdapter<Object> {
        private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    String str = in.nextString();
                    if (StringUtils.isNumeric(str)) {
                        return Long.valueOf(str);
                    }
                    return str;

                case NUMBER:
                    double dbNum = in.nextDouble();

                    // 数字超过int的最大值，返回字符串类型
                    if (dbNum > Integer.MAX_VALUE) {
                        return in.nextString();
                    }
                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        if (lngNum <= Integer.MAX_VALUE) {
                            return (int) lngNum;
                        }
                        return lngNum;
                    }
                    return dbNum;
                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            delegate.write(out, value);
        }
    }

    public static class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {
        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getSimpleName());
        }
    }

    private static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
            final JsonParser parser = new JsonParser();
            return parser.parse(json.value());
        }
    }
}
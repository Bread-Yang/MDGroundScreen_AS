package org.mdground.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.mdground.api.utils.json.NullStringToEmptyAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;

public class GsonUtils {

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(int.class, new StringToIntegerTypeAdapter())
                .registerTypeAdapter(Integer.class, new StringToIntegerTypeAdapter())

                .registerTypeAdapter(double.class, new StringToDoubleTypeAdapter())
                .registerTypeAdapter(Double.class, new StringToDoubleTypeAdapter())

                .registerTypeAdapter(float.class, new StringToFloatTypeAdapter())
                .registerTypeAdapter(Float.class, new StringToFloatTypeAdapter())

                .registerTypeAdapter(long.class, new StringToLongTypeAdapter())
                .registerTypeAdapter(Long.class, new StringToLongTypeAdapter())
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
//                .registerTypeAdapter(boolean.class, new NumericToBooleanAdapter())
//                .registerTypeAdapter(Boolean.class, new NumericToBooleanAdapter())
                .create();
    }

    private static class StringToIntegerTypeAdapter extends TypeAdapter<Integer> {
        @Override
        public void write(JsonWriter jsonWriter, Integer value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.value(value);
            } catch (Exception e){
                throw e;
            }
        }

        @Override
        public Integer read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return 0;
            }

            try {
                String value = jsonReader.nextString();

                if (StringUtil.isNumeric(value)) {
                    return NumberFormat.getInstance().parse(value).intValue();
                }
                return 0;
            } catch (NumberFormatException | ParseException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }

    private static class StringToDoubleTypeAdapter extends TypeAdapter<Double> {
        @Override
        public void write(JsonWriter jsonWriter, Double value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.value(value);
            } catch (Exception e){
                throw e;
            }
        }

        @Override
        public Double read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return 0d;
            }

            try {
                String value = jsonReader.nextString();

                if (StringUtil.isNumeric(value)) {
                    return NumberFormat.getInstance().parse(value).doubleValue();
                }
                return 0d;
            } catch (NumberFormatException | ParseException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }

    private static class StringToFloatTypeAdapter extends TypeAdapter<Float> {
        @Override
        public void write(JsonWriter jsonWriter, Float value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.value(value);
            } catch (Exception e){
                throw e;
            }
        }

        @Override
        public Float read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return 0f;
            }

            try {
                String value = jsonReader.nextString();

                if (StringUtil.isNumeric(value)) {
                    return NumberFormat.getInstance().parse(value).floatValue();
                }
                return 0f;
            } catch (NumberFormatException | ParseException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }

    private static class StringToLongTypeAdapter extends TypeAdapter<Long> {
        @Override
        public void write(JsonWriter jsonWriter, Long value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.value(value);
            } catch (Exception e){
                throw e;
            }
        }

        @Override
        public Long read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return 0l;
            }

            try {
                String value = jsonReader.nextString();

                if (StringUtil.isNumeric(value)) {
                    return NumberFormat.getInstance().parse(value).longValue();
                }
                return 0l;
            } catch (NumberFormatException | ParseException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }

    private static class NumericToBooleanAdapter extends TypeAdapter<Boolean> {
        @Override
        public void write(JsonWriter jsonWriter, Boolean value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.value(value);
            } catch (Exception e){
                throw e;
            }
        }

        @Override
        public Boolean read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return false;
            }

            try {
                String value = jsonReader.nextString();

                if ("1".equals(value)) {
                    return true;
                }
                return false;
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }

    private static class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            String value = json.getAsString();

            if (StringUtil.isNumeric(value)) {
                int code = json.getAsInt();
                return code == 0 ? false :
                        code == 1 ? true : false;
            }

            return json.getAsBoolean();
        }
    }
}

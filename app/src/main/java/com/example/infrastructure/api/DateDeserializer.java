package com.example.infrastructure.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class DateDeserializer là một custom deserializer dành cho Gson,
 * được sử dụng để chuyển đổi chuỗi ngày tháng từ JSON thành đối tượng Date.
 *
 * Tác giả: Trần Văn An
 */
public class DateDeserializer implements JsonDeserializer<Date> {

    /**
     * Phương thức deserialize chuyển đổi một JsonElement (chứa chuỗi ngày tháng từ JSON)
     * thành một đối tượng Date.
     *
     * @param json     JsonElement chứa chuỗi ngày tháng từ JSON
     * @param typeOfT  Loại của đối tượng cần chuyển đổi
     * @param context  Ngữ cảnh của việc chuyển đổi
     * @return đối tượng Date được chuyển đổi từ chuỗi ngày tháng
     * @throws JsonParseException nếu không thể chuyển đổi thành công
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String dateStr = json.getAsString();
        SimpleDateFormat sdf = new SimpleDateFormat(RestApiClient.DATE_FORMAT);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonParseException("Unable to parse date", e);
        }
    }
}

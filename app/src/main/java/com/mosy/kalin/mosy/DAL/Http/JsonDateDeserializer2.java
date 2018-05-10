package com.mosy.kalin.mosy.DAL.Http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonDateDeserializer2 implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            return new SimpleDateFormat("HH:mm:ss", Locale.US).parse(jsonElement.getAsString());
        } catch (ParseException e) {
        }

        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + "HH:mm:ss");
    }
}
//
//public class JsonDateDeserializer implements JsonDeserializer<Date> {
//
//    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        String stringDate = json.getAsJsonPrimitive().getAsString();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stringDate.length() == 8 ? "HH:mm:ss" : "yyyy-MM-dd'T'HH:mm:ss");
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(stringDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return date;
//    }
//
//}

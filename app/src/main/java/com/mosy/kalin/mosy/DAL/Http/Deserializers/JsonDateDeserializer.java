package com.mosy.kalin.mosy.DAL.Http.Deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.Settings.System.DATE_FORMAT;

public class JsonDateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(jsonElement.getAsString());
        } catch (ParseException e) {
        }

        throw new JsonParseException("Imparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + "yyyy-MM-dd'T'HH:mm:ss");
    }
}

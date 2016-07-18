package com.lis99.mobile.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;



public class ApiTypeAdapterFactory implements TypeAdapterFactory {
    private String dataElementName;

    protected final static Gson plainGson = new Gson();

    public ApiTypeAdapterFactory(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementTypeAdapter = gson.getAdapter(JsonElement.class);


        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementTypeAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("status")) {
                        String status = jsonObject.get("status").getAsString();
                        if (status.equals("OK")) {
                            //do nothing
                        } else {
                            String msg = null;
                            if (jsonObject.has(dataElementName)) {
                                JsonObject dataObject = jsonObject.get(dataElementName).getAsJsonObject();
                                if (dataObject.has("error")) {
                                    msg = dataObject.get("error").getAsString();
                                }
                            }
                            if (TextUtils.isEmpty(msg)) {
                                throw new ApiException(1);
                            } else {
                                throw new ApiException(1, msg);
                            }
                        }
                    }
                    if (jsonObject.has(dataElementName)) {
                        jsonElement = jsonObject.get(dataElementName);
                    }
                }

                T model = (T) plainGson.fromJson(jsonElement, type.getType());

                return model;
                //return delegate.fromJsonTree(jsonElement);
            }

        }.nullSafe();
    }
}

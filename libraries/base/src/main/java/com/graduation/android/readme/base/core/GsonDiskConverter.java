package com.graduation.android.readme.base.core;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.graduation.android.readme.base.utils.CloseUtils;
import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.base.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ConcurrentModificationException;

/**
 * GSON-数据转换器
 */
public class GsonDiskConverter implements IDiskConverter {
    private Gson gson = new Gson();

    public GsonDiskConverter() {
        this.gson = new Gson();
    }

    public GsonDiskConverter(Gson gson) {
        Utils.checkNotNull(gson, "gson ==null");
        this.gson = gson;
    }

    @Override
    public <T> T load(InputStream source, Type type) {
        T value = null;
        try {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(source));
            value = (T) adapter.read(jsonReader);
        } catch (JsonIOException | IOException | ConcurrentModificationException | JsonSyntaxException e) {
            L.e(e.getMessage());
        } catch (Exception e) {
            L.e(e.getMessage());
        } finally {
            CloseUtils.closeIO(source);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        try {
            String json = gson.toJson(data);
            byte[] bytes = json.getBytes();
            sink.write(bytes, 0, bytes.length);
            sink.flush();
            return true;
        } catch (JsonIOException | JsonSyntaxException | ConcurrentModificationException | IOException e) {
            L.e(e.getMessage());
        } catch (Exception e) {
            L.e(e.getMessage());
        } finally {
            CloseUtils.closeIO(sink);
        }
        return false;
    }

}

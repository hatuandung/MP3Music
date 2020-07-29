package com.t3h.mp3music.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.t3h.mp3music.model.BaseModel;
import com.t3h.mp3music.model.FieldInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SystemData {
    private ContentResolver resolver;

    public SystemData(Context context) {
        resolver = context.getContentResolver();
    }

    public <T extends BaseModel> ArrayList<T> getData(Class<T> clz) {
        ArrayList<T> data = new ArrayList<>();
        try {
            T t = clz.newInstance();
            Cursor cursor = resolver.query(
                    t.getContentUri(),
                    null, null, null, null, null
            );
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                T item = clz.newInstance();
                readInfo(cursor, item);
                data.add(item);
                cursor.moveToNext();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return data;
        }
    }

    private <T extends BaseModel> T readInfo(Cursor cursor, T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);//cho pheeps truy cap thuoc tih private
            FieldInfo info = f.getAnnotation(FieldInfo.class);
            if (info == null) continue;
            try {
                setValue(cursor, t, f, info);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private <T extends BaseModel> void setValue(Cursor cursor, T t, Field f, FieldInfo info) throws IllegalAccessException {
        int index = cursor.getColumnIndex(info.columnName());
        String value = cursor.getString(index);
        switch (f.getType().getSimpleName()){
            case "int" :
                f.setInt(t,Integer.parseInt(value));
                break;
            case "float":
                f.setFloat(t,Float.parseFloat(value));
                break;
            default:
                f.set(t,value);
                break;
        }
    }
}

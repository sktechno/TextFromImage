package com.sk.doubtnut;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sk on 20/07/17.
 */

public class DataBase extends SQLiteOpenHelper {

    private static DataBase instance;
    private SQLiteDatabase db;
    private static final int DB_VERSION = 2;
    Context context;

    public static DataBase getInstance(Context context) {

        if (instance == null) {
            String cacheName = "doubtnubDb";

            instance = new DataBase(context, DB_VERSION, cacheName);
        }
        return instance;
    }

    private DataBase(Context context, int version, String cacheName) {

        super(context, cacheName, null, version);
        this.context = context;

    }

    private SQLiteDatabase getDB(Context context) {
        if (db == null)
            db = getInstance(context).getWritableDatabase();
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE 'doubtnubList' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'tags' TEXT NOT NULL, " +
                "'image_text' TEXT NOT NULL, " +
                "'image_path' TEXT NOT NULL, " +
                "'create_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");   //
    }

    public void insertorUpdate(String tags, String imagePath, String imageText) {

        try {
            ContentValues val = new ContentValues();

            val.put("'tags'", tags);
            val.put("'image_text'", imageText);
            val.put("'image_path'", imagePath);

            long rowid = getDB(context).insert("doubtnubList", null, val);
            Log.d("insert success", "true" + rowid);


        } catch (Exception e) {
            Log.d("insert fail", "true");
        }


    }

    public ArrayList<DataModel> getdata() {
        Cursor c = null;
        ArrayList<DataModel> data = new ArrayList<>();
        try {

            String q = "select * from doubtnubList";
            c = getDB(context).rawQuery(q, null);


            if (c.moveToFirst()) {

                while (c.moveToNext()) {
                    DataModel model = new DataModel();
                    model.setTag(c.getString(c.getColumnIndex("tags")));
                    model.setImagePath(c.getString(c.getColumnIndex("image_path")));
                    model.setId(c.getString(c.getColumnIndex("id")));
                    model.setImageText(c.getString(c.getColumnIndex("image_text")));

                    data.add(model);
                }


            }

        } catch (Exception e) {
            Log.d("inser fail", "true " + e.getMessage());
        } finally {
            if (c != null)
                c.close();
        }


        return data;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'doubtnubList';");
        onCreate(db);

    }
}

package io.github.mthli.Tweetin.Database.Favorite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.github.mthli.Tweetin.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAction {
    private SQLiteDatabase database;
    private FavoriteHelper helper;
    private Context context;
    
    public FavoriteAction(Context context) {
        this.helper = new FavoriteHelper(context);
        this.context = context;
    }

    public void openDatabase(boolean rw) {
        if (rw) {
            database = helper.getWritableDatabase();
        } else {
            database = helper.getReadableDatabase();
        }
    }

    public void closeDatabase() {
        helper.close();
    }


    public void addRecord(FavoriteRecord record) {
        ContentValues values = new ContentValues();
        values.put(FavoriteRecord.STATUS_ID, record.getStatusId());
        values.put(FavoriteRecord.REPLY_TO_STATUS_ID, record.getReplyToStatusId());
        values.put(FavoriteRecord.USER_ID, record.getUserId());
        values.put(FavoriteRecord.RETWEETED_BY_USER_ID, record.getRetweetedByUserId());
        values.put(FavoriteRecord.AVATAR_URL, record.getAvatarURL());
        values.put(FavoriteRecord.CREATED_AT, record.getCreatedAt());
        values.put(FavoriteRecord.NAME, record.getName());
        values.put(FavoriteRecord.SCREEN_NAME, record.getScreenName());
        if (record.isProtect()) {
            values.put(FavoriteRecord.PROTECT, "true");
        } else {
            values.put(FavoriteRecord.PROTECT, "false");
        }
        values.put(FavoriteRecord.CHECK_IN, record.getCheckIn());
        values.put(FavoriteRecord.TEXT, record.getText());
        if (record.isRetweet()) {
            values.put(FavoriteRecord.RETWEET, "true");
        } else {
            values.put(FavoriteRecord.RETWEET, "false");
        }
        values.put(FavoriteRecord.RETWEETED_BY_USER_NAME, record.getRetweetedByUserName());
        if (record.isFavorite()) {
            values.put(FavoriteRecord.FAVORITE, "true");
        } else {
            values.put(FavoriteRecord.FAVORITE, "false");
        }
        database.insert(FavoriteRecord.TABLE, null, values);
    }

    public void updatedByRetweet(long statusId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.sp_name),
                Context.MODE_PRIVATE
        );
        long useId = preferences.getLong(
                context.getString(R.string.sp_use_id),
                -1
        );

        ContentValues values = new ContentValues();
        values.put(FavoriteRecord.RETWEET, "true");
        values.put(FavoriteRecord.RETWEETED_BY_USER_ID, useId);
        values.put(
                FavoriteRecord.RETWEETED_BY_USER_NAME,
                context.getString(R.string.tweet_retweet_by_me)
        );
        database.update(
                FavoriteRecord.TABLE,
                values,
                FavoriteRecord.STATUS_ID + "=?",
                new String[] {String.valueOf(statusId)}
        );
    }

    public void updatedByFavorite(long statusId, boolean favorite) {
        ContentValues values = new ContentValues();
        if (favorite) {
            values.put(FavoriteRecord.FAVORITE, "true");
        } else {
            values.put(FavoriteRecord.FAVORITE, "false");
        }
        database.update(
                FavoriteRecord.TABLE,
                values,
                FavoriteRecord.STATUS_ID + "=?",
                new String[] {String.valueOf(statusId)}
        );
    }

    public void deleteAll() {
        database.execSQL("DELETE FROM " + FavoriteRecord.TABLE);
    }

    private FavoriteRecord getFavoriteRecord(Cursor cursor) {
        FavoriteRecord record = new FavoriteRecord();
        record.setStatusId(cursor.getLong(0));
        record.setReplyToStatusId(cursor.getLong(1));
        record.setUserId(cursor.getLong(2));
        record.setRetweetedByUserId(cursor.getLong(3));
        record.setAvatarURL(cursor.getString(4));
        record.setCreatedAt(cursor.getString(5));
        record.setName(cursor.getString(6));
        record.setScreenName(cursor.getString(7));
        record.setProtect(
                cursor.getString(8).equals("true")
        );
        record.setCheckIn(cursor.getString(9));
        record.setText(cursor.getString(10));
        record.setRetweet(
                cursor.getString(11).equals("true")
        );
        record.setRetweetedByUserName(cursor.getString(12));
        record.setFavorite(
                cursor.getString(13).equals("true")
        );

        return record;
    }
    public List<FavoriteRecord> getFavoriteRecordList() {
        List<FavoriteRecord> timelineRecordList = new ArrayList<FavoriteRecord>();
        Cursor cursor = database.query(
                FavoriteRecord.TABLE,
                new String[] {
                        FavoriteRecord.STATUS_ID,
                        FavoriteRecord.REPLY_TO_STATUS_ID,
                        FavoriteRecord.USER_ID,
                        FavoriteRecord.RETWEETED_BY_USER_ID,
                        FavoriteRecord.AVATAR_URL,
                        FavoriteRecord.CREATED_AT,
                        FavoriteRecord.NAME,
                        FavoriteRecord.SCREEN_NAME,
                        FavoriteRecord.PROTECT,
                        FavoriteRecord.CHECK_IN,
                        FavoriteRecord.TEXT,
                        FavoriteRecord.RETWEET,
                        FavoriteRecord.RETWEETED_BY_USER_NAME,
                        FavoriteRecord.FAVORITE
                },
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return timelineRecordList;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FavoriteRecord record = getFavoriteRecord(cursor);
            timelineRecordList.add(record);
            cursor.moveToNext();
        }
        cursor.close();

        return timelineRecordList;
    }
}
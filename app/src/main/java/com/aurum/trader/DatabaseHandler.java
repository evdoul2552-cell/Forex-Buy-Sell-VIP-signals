package com.aurum.trader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Forex";
    private static final String KEY_MARKET_SYMBOLS_INDICATORS = "indicators";
    private static final String KEY_SYMBOL_SYMBOLS = "symbol";
    private static final String TABLE_CONFIG = "config";
    private static final String TABLE_MARKET_SYMBOLS = "market_symbols";
    static String TAG = DatabaseHandler.class.getName();
    private static DatabaseHandler mInstance = null;
    private Context con;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 58);
        this.con = context;
    }

    public static synchronized DatabaseHandler getInstance() {
        DatabaseHandler databaseHandler;
        synchronized (DatabaseHandler.class) {
            if (mInstance == null) {
                mInstance = new DatabaseHandler(MyApplication.getContext());
            }
            databaseHandler = mInstance;
        }
        return databaseHandler;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createTable(sQLiteDatabase, "CREATE TABLE calendar_symbols(oid INTEGER PRIMARY KEY,symbol TEXT,activated BOOLEAN,countryCode TEXT)");
        createTable(sQLiteDatabase, "CREATE TABLE market_symbols(oid INTEGER UNIQUE,name TEXT,activated BOOLEAN,decimals INTEGER,orderNumber INTEGER , indicators TEXT)");
        createTable(sQLiteDatabase, "CREATE TABLE market_notification(oid INTEGER PRIMARY KEY,symbolOid INTEGER,price DOUBLE,type INTEGER,activated BOOLEAN)");
        createTable(sQLiteDatabase, "CREATE TABLE news_read(oid INTEGER,last_read_time INTEGER)");
        createTable(sQLiteDatabase, "CREATE TABLE news_tags(oid INTEGER,name TEXT,activated BOOLEAN)");
        createTable(sQLiteDatabase, "CREATE TABLE config(name TEXT UNIQUE,value TEXT)");
        createTable(sQLiteDatabase, "CREATE TABLE outlook_symbols(oid INTEGER UNIQUE,name TEXT,activated BOOLEAN,decimals INTEGER,orderNumber INTEGER)");
        createTable(sQLiteDatabase, "CREATE TABLE outlook_notification(oid INTEGER PRIMARY KEY,symbolOid INTEGER,price DOUBLE,type INTEGER,action INTEGER,relation INTEGER,activated BOOLEAN)");
    }

    private void createTable(SQLiteDatabase sQLiteDatabase, String str) {
        try {
            Log.d(TAG, str);
            sQLiteDatabase.execSQL(str);
        } catch (SQLException unused) {
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onCreate(sQLiteDatabase);
        String str = TAG;
        Log.d(str, i + "-" + i2);
        if (i <= 54) {
            sQLiteDatabase.execSQL("ALTER TABLE market_symbols ADD COLUMN indicators TEXT");
            Log.i(TAG, "table market_symbols updated");
        }
    }


    public void addMarketSymbols(List<MarketSymbolObject> list) {
        if (list.size() != 0) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.beginTransaction();
            DatabaseUtils.InsertHelper insertHelper = new DatabaseUtils.InsertHelper(writableDatabase, TABLE_MARKET_SYMBOLS);
            try {
                int columnIndex = insertHelper.getColumnIndex(Definitions.PARAM_OID);
                int columnIndex2 = insertHelper.getColumnIndex("name");
                int columnIndex3 = insertHelper.getColumnIndex("activated");
                int columnIndex4 = insertHelper.getColumnIndex("decimals");
                int columnIndex5 = insertHelper.getColumnIndex("orderNumber");
                int columnIndex6 = insertHelper.getColumnIndex(KEY_MARKET_SYMBOLS_INDICATORS);
                for (MarketSymbolObject marketSymbolObject : list) {
                    insertHelper.prepareForInsert();
                    insertHelper.bind(columnIndex, marketSymbolObject.oid);
                    insertHelper.bind(columnIndex2, marketSymbolObject.symbol);
                    insertHelper.bind(columnIndex3, marketSymbolObject.activated);
                    insertHelper.bind(columnIndex4, marketSymbolObject.decimals);
                    insertHelper.bind(columnIndex5, marketSymbolObject.order);
                    if (!TextUtils.isEmpty(marketSymbolObject.indicators)) {
                        insertHelper.bind(columnIndex6, marketSymbolObject.indicators);
                    }
                    insertHelper.execute();
                }
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
                insertHelper.close();
            }
        }
    }

    public int getConfigAsInt(String str, int i) {
        try {
            Cursor query = getReadableDatabase().query(TABLE_CONFIG, new String[]{"name", "value"}, "name=?", new String[]{String.valueOf(str)}, null, null, null, null);
            if (query != null) {
                query.moveToFirst();
            }
            return Integer.parseInt(new ConfigObject(query.getString(0), query.getString(1)).getConfigValue());
        } catch (Exception e) {
            Log.e(TAG, "error", e);
            return i;
        }
    }

    public String getConfigAsString(String str, String str2) {
        try {
            Cursor query = getReadableDatabase().query(TABLE_CONFIG, new String[]{"name", "value"}, "name=?", new String[]{String.valueOf(str)}, null, null, null, null);
            if (query != null) {
                query.moveToFirst();
            }
            return String.valueOf(new ConfigObject(query.getString(0), query.getString(1)).getConfigValue());
        } catch (Exception e) {
            Log.e(TAG, "error", e);
            return str2;
        }
    }

    public SparseArray<MarketSymbolObject> getAllMarketSymbolsAsSparse() {
        SparseArray<MarketSymbolObject> sparseArray = new SparseArray<>();
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM market_symbols order by name asc", null);
        if (rawQuery.moveToFirst()) {
            do {
                MarketSymbolObject marketSymbolObject = new MarketSymbolObject();
                boolean z = false;
                marketSymbolObject.oid = rawQuery.getInt(0);
                marketSymbolObject.symbol = rawQuery.getString(1);
                if (1 == rawQuery.getInt(2)) {
                    z = true;
                }
                marketSymbolObject.activated = z;
                marketSymbolObject.decimals = rawQuery.getInt(3);
                marketSymbolObject.order = rawQuery.getInt(4);
                sparseArray.append(marketSymbolObject.oid, marketSymbolObject);
            } while (rawQuery.moveToNext());
        }
        return sparseArray;
    }

    public SparseArray<MarketSymbolObject> getAllMarketSymbols() {
        SparseArray<MarketSymbolObject> sparseArray = new SparseArray<>();
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM market_symbols order by name asc", null);
        if (rawQuery.moveToFirst()) {
            do {
                MarketSymbolObject marketSymbolObject = new MarketSymbolObject();
                boolean z = false;
                marketSymbolObject.oid = rawQuery.getInt(0);
                marketSymbolObject.symbol = rawQuery.getString(1);
                if (1 == rawQuery.getInt(2)) {
                    z = true;
                }
                marketSymbolObject.activated = z;
                marketSymbolObject.decimals = rawQuery.getInt(3);
                marketSymbolObject.order = rawQuery.getInt(4);
                sparseArray.append(marketSymbolObject.oid, marketSymbolObject);
            } while (rawQuery.moveToNext());
        }
        return sparseArray;
    }

    public ArrayList<MarketSymbolObject> getAllMarketSymbolsByUserOrder() {
        ArrayList<MarketSymbolObject> arrayList = new ArrayList<>();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT  * FROM market_symbols order by orderNumber asc", null);
        if (rawQuery.moveToFirst()) {
            do {
                MarketSymbolObject marketSymbolObject = new MarketSymbolObject();
                boolean z = false;
                marketSymbolObject.oid = rawQuery.getInt(0);
                marketSymbolObject.symbol = rawQuery.getString(1);
                if (1 == rawQuery.getInt(2)) {
                    z = true;
                }
                marketSymbolObject.activated = z;
                marketSymbolObject.decimals = rawQuery.getInt(3);
                marketSymbolObject.order = rawQuery.getInt(4);
                marketSymbolObject.indicators = rawQuery.getString(5);
                arrayList.add(marketSymbolObject);
            } while (rawQuery.moveToNext());
        }
        return arrayList;
    }

    public int updateConfig(ConfigObject configObject) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", configObject.getConfigValue());
        return writableDatabase.update(TABLE_CONFIG, contentValues, "name = ?", new String[]{String.valueOf(configObject.getConfigKey())});
    }

    public void deleteMarketNotificationBySymbols(int i) {
        getWritableDatabase().delete("market_notification", "symbolOid = ?", new String[]{String.valueOf(i)});
    }

    public void deleteAllMarketSymbols() {
        getWritableDatabase().delete(TABLE_MARKET_SYMBOLS, null, null);
    }

}

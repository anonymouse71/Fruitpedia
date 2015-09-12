package ncku.fruitpedia.main;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FruitpediaDbHelper extends SQLiteOpenHelper{
	
	// 資料庫名稱
	private static final String DB_NAME = "fruitpedia.db";
	
	// 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
	private static final int DB_VERSION = 1;
	
	// 建構子，在一般的應用都不需要修改
    public FruitpediaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }	
    
    public FruitpediaDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
}
 
    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_FAVORITE_TABLE_SQL = 
            "CREATE TABLE table_favorites (" + 
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "_fruitname VARCHAR(30) NOT NULL UNIQUE);"; 
    
    //private static final String TAG = "test";//Log TAG
    
	@Override
    public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FAVORITE_TABLE_SQL);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		db.execSQL("DROP TABLE IF EXISTS table_favorites"); //刪除舊有的資料表
		onCreate(db);
	}
	
	//加入我的最愛 資料表中
	public long insertIntoFavorites(String fruitName){
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues contentValues = new ContentValues();
		contentValues.put("_fruitname", fruitName);
		long rowID = db.insert("table_favorites", null, contentValues);
		db.close();
		return rowID;
	}
	
	//從我的最愛 資料表中 拿出所有水果名稱
	public ArrayList<String> getFavoritesFruit(){
		SQLiteDatabase db = this.getReadableDatabase();
		String getFavoritesFruit_sql = "SELECT * FROM table_favorites";
		Cursor cursor = db.rawQuery(getFavoritesFruit_sql, null);
		ArrayList<String> fruitNameArrayList = new ArrayList<String>();
		
		//將cursor以到資料表的第一列
		cursor.moveToFirst();
		
		//將FruitName資料餵給fruitNameArrayList，回傳
		do{
			//Log.d("test",cursor.getString(1));
			fruitNameArrayList.add(cursor.getString(1));
		}while(cursor.moveToNext());
		
		//關閉資料庫
		db.close();
		return fruitNameArrayList;
	}
	
	//確認table_favorites是否存在
	public boolean isFavoritesTableExists(){
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT * FROM table_favorites",null);
	    if(cursor.getCount()<=0){
	    	return false;
	    }
	    else{
	    	return true;
	    }
	}
	
	//清空我的最愛
	public void deleteFavoritesTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("table_favorites", null, null);
		db.close();
	}
	
}

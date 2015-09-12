package ncku.fruitpedia.fragment1_fruitlist;

import ncku.fruitpedia.main.FruitpediaDbHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import example.android.tabsample1.R;

public class FruitContentActivity extends Activity {
	//本頁的水果名稱，純英文
	private String fruitName;
	private FruitpediaDbHelper fruitpediaDbHelper = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	//在ActionBar上顯示返回鍵
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	setContentView(R.layout.fruit_content);
    	
    	fruitpediaDbHelper = new FruitpediaDbHelper(this);
    	
    	//動態產生水果的詳細內文
    	buildViews();
    }
    
   //動態產生水果內文
  	@SuppressLint("DefaultLocale") 
  	private void buildViews(){
  		Bundle bundle = this.getIntent().getExtras();
  		fruitName = bundle.getString("strFruitClicked"); //拿到被點選的水果名稱
  		
  		//用來設定水果每一項內文的暫時字串 
  		String tmp = null;
  		
  		//title
  		setTitle(getResources().getString(getResources().getIdentifier(fruitName + "_title", "string", getPackageName())));
  		
  		//fruitMainPicture
  		final ImageView fruitMainPicture;
  		fruitMainPicture = (ImageView) findViewById(R.id.imgIdFruitMain);
  		fruitMainPicture.setImageResource(getResources().getIdentifier(fruitName + "_main", "drawable", getPackageName()));
  		
  		//placeOfProduction
  		final TextView tvPlaceOfProduction;
  		tvPlaceOfProduction = (TextView) findViewById(R.id.tvIdPlaceOfProduction);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_placeOfProduction", "string", getPackageName()));
  		tvPlaceOfProduction.setText(tmp);
  		
  		//season
  		final TextView tvSeason;
  		tvSeason = (TextView) findViewById(R.id.tvIdSeason);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_season", "string", getPackageName()));
  		tvSeason.setText(tmp);
  		
  		//nutrition
  		final TextView tvNutrition;
  		tvNutrition = (TextView) findViewById(R.id.tvIdNutrition);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_nutrition", "string", getPackageName()));	
  		tvNutrition.setText(tmp);
  		
  		//feature
  		final TextView tvFeature;
  		tvFeature = (TextView) findViewById(R.id.tvIdFeature);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_feature", "string", getPackageName()));
  		tvFeature.setText(tmp);
  		
  		//pick
  		final TextView tvPick;
  		tvPick = (TextView) findViewById(R.id.tvIdPick);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_pick", "string", getPackageName()));	
  		tvPick.setText(tmp);
  		
  		//remind
  		final TextView tvRemind;
  		tvRemind = (TextView) findViewById(R.id.tvIdRemind);
  		tmp = getResources().getString(getResources().getIdentifier(fruitName + "_remind", "string", getPackageName()));		
  		tvRemind.setText(tmp);
  		
  	}    
  	
//	@Override
//	public void onResume() {
//		super.onResume();
//		if(fruitpediaDbHelper == null)
//			fruitpediaDbHelper = new FruitpediaDbHelper(this); 
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		if(fruitpediaDbHelper != null){
//			fruitpediaDbHelper.close(); 
//			fruitpediaDbHelper = null;
//		}
//	}    
	
    //Actionbar和menu設定
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		//將搜尋和更新的item從actionbar中拿掉，用不到，只留加入我的最愛。
		menu.findItem(R.id.actionbarId_search).setVisible(false);
		//menu.findItem(R.id.actionbarId_refresh).setVisible(false);
		
		return true;
	}
	
	//實作返回功能 和 加入我的最愛
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
        switch (item.getItemId()) {
	        case android.R.id.home: 
	            onBackPressed();
	            return true;
	        case R.id.actionbarId_addToFavorites: //加入到我的最愛
	        	String msg = null;
	        	
	        	//呼叫fruitpediaDbHelper裡的insertIntoFavorites函式，來將水果加入我的最愛。
	    		long rowID = fruitpediaDbHelper.insertIntoFavorites(fruitName); 
	    		
	    		//判斷是否以新增過了
	    		if(rowID != -1){
	    			msg = "新增 " + fruitName + " 到我的最愛";
	    		}else{
	    			msg = fruitName + " 已新增過囉! ";
	    		}
	    		Toast.makeText(FruitContentActivity.this, msg, Toast.LENGTH_SHORT).show();	
	        	return true;
        }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		fruitpediaDbHelper.close();
	}
}

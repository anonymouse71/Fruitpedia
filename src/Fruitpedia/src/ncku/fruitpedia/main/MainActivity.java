package ncku.fruitpedia.main;

import ncku.fruitpedia.fragment1_fruitlist.Frag1_FruitList;
import ncku.fruitpedia.fragment1_fruitlist.FruitContentActivity;
import ncku.fruitpedia.fragment2_bulletin.Frag2_Bulletin;
import ncku.fruitpedia.fragment3_Favorites.Frag3_Favorites;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import example.android.tabsample1.R;

public class MainActivity extends Activity {
	private FruitpediaDbHelper fruitpediaDbHelper = null;
    private String strFruitSearched = null;
    
    // onCreate 方法 (起始畫面時的事件處理器) 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //呼叫父類別的 onCreate 方法
        super.onCreate(savedInstanceState);
        //指定畫面設定檔
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        
        //設定主畫面 main page 的labyout
        setContentView(R.layout.activity_main);
        
        //將動作列設定為標籤頁次模式
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, 0);
        
        //設定標籤頁次 1 : 水果列表
        ActionBar.Tab tab1 = getActionBar().newTab();
        tab1.setText(R.string.tab_FruitList);
        tab1.setIcon(R.drawable.ic_action_view_as_grid);
        tab1.setTabListener(new TabListener(new Frag1_FruitList())); //向監聽器註冊

        //設定標籤頁次 2 : 佈告欄
        ActionBar.Tab tab2 = getActionBar().newTab();
        tab2.setText(R.string.tab_Bulletin);
        tab2.setIcon(R.drawable.ic_action_paste);
        tab2.setTabListener(new TabListener(new Frag2_Bulletin()));

        //設定標籤頁次3 : 我的最愛
        ActionBar.Tab tab3 = getActionBar().newTab();
        tab3.setText(R.string.tab_Favorites);
        tab3.setIcon(R.drawable.ic_action_favorite);
        tab3.setTabListener(new TabListener(new Frag3_Favorites()));

        //將各標籤頁次放入動作列
        getActionBar().addTab(tab1);
        getActionBar().addTab(tab2);
        getActionBar().addTab(tab3);
        
        if (savedInstanceState != null) {
        	getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
        
        //如果要清除我的最愛，先建立FruitpediaDbHelper物件。
        fruitpediaDbHelper = new FruitpediaDbHelper(this);
    }

    //設定選擇標籤頁次監聽器
    private class TabListener implements ActionBar.TabListener {
    	private Fragment fragment;
    	//建構子
    	public TabListener(Fragment fragment) {
    		//對應區塊元件
    		this.fragment = fragment;
    	}

    	//onTabReselected 方法 (重選標籤頁次時的事件處理器) 
    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	}

    	//onTabSelected 方法 (選擇標籤頁次時的事件處理器) 
    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		//加入對應區塊元件
    		ft.add(R.id.main, fragment);
    	}

    	//onTabUnselected 方法 (取消選擇標籤頁次時的事件處理器) 
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    		//移除對應區塊元件
    		ft.remove(fragment);
    	}
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
        
		menu.findItem(R.id.actionbarId_addToFavorites).setVisible(false);
		
		//得到搜尋欄的資料
		SearchView searchView = (SearchView) menu.findItem(R.id.actionbarId_search).getActionView();
		searchView.setQueryHint(getString(R.string.search_hint));
		searchView.setOnQueryTextListener(ActViewListener);
		return true;
	}
	
	//監聽搜尋欄
    private OnQueryTextListener ActViewListener = new OnQueryTextListener() {    
    	
	    public boolean onQueryTextChange(String newText) { 			
	        return true;
	    }
	    
	    @SuppressLint("DefaultLocale") 
	    public boolean onQueryTextSubmit(String query) {
	    	
	    	strFruitSearched = query;
	    	//分析actionbar search到的文字
	    	strFruitSearched = strFruitSearched.substring(strFruitSearched.indexOf(' ')+1, strFruitSearched.length()); //分析字串，只要英文字串
	    	strFruitSearched = strFruitSearched.toLowerCase();
	    	Log.d("test","strFruitSearched:" + strFruitSearched);
	    	
	    	//如果輸入水果英文全名，則會直接查找並輸出對應業面，輸入其他文字，會判斷是輸入中文還是查無資料
	    	String[] fruitStrArray = getResources().getStringArray(R.array.strArray_FruitList);
	    	boolean fonundOrNot = false;
	    	for(String elementFruit : fruitStrArray){
	    		//取出fruitStrArray裡的英文
	    		elementFruit = elementFruit.substring(elementFruit.indexOf(' ')+1, elementFruit.length());
	    		if(elementFruit.toLowerCase().equals(strFruitSearched)){
	    			fonundOrNot = true;
	    			break;
	    		}
	    		fonundOrNot = false;
	    	}
	    	if(fonundOrNot){
	    		MainActivityToFruitContentActivity(fonundOrNot);
	    		return true;
	    	}
	    	//如果輸入是除了水果英文全名以外的，繼續檢查是不是水果中文全名
	    	else{
		    	for(int i = 0; i < fruitStrArray.length; i++){
		    		String elementFruit = fruitStrArray[i];
		    		//取出fruitStrArray裡的中文
		    		String elementFruit_cn = elementFruit.substring(0, elementFruit.indexOf(' '));
		    		Log.d("test","elementFruit:" + elementFruit_cn);
		    		if(elementFruit_cn.equals(strFruitSearched)){
		    			fonundOrNot = true;
		    			strFruitSearched = fruitStrArray[i].substring(elementFruit.indexOf(' ')+1, elementFruit.length());
		    			strFruitSearched = strFruitSearched.toLowerCase();
		    			break;
		    		}
		    		fonundOrNot = false;
		    	}
		    	MainActivityToFruitContentActivity(fonundOrNot);
	    	}
	    	return true;
	    }
    };
    
    //如果有查詢到，把文字傳送到FruitContentActivity，顯示水果內文
    private void MainActivityToFruitContentActivity(boolean fonundOrNot){
    	if(fonundOrNot){
	    	//建立意圖物件，從MainAcitivuty到FruitContentActivity
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, FruitContentActivity.class);	
			
			//建立Bundle物件，來傳送水果像目字串
			Bundle bundle = new Bundle();
			bundle.putString("strFruitClicked", strFruitSearched);
			intent.putExtras(bundle);
			startActivity(intent);
    	}
    	else{
    		Toast.makeText(MainActivity.this, "查無 " + strFruitSearched + " 資料。", Toast.LENGTH_SHORT).show();
    	}
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
    		case R.id.menuId_clearFavorite:
    			fruitpediaDbHelper.deleteFavoritesTable();
    			Toast.makeText(this, "已清空我的最愛", Toast.LENGTH_SHORT).show();
    			return true;
    		case R.id.menuId_Info:
    			infoDialog();
    			return true;
    			
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    //按menu的"關於"時，挑出小視窗
    private void infoDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(R.string.infoDialog)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                //do things
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}

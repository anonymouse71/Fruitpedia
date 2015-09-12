package ncku.fruitpedia.fragment3_Favorites;

import java.util.ArrayList;
import java.util.HashMap;

import ncku.fruitpedia.fragment1_fruitlist.FruitArrayListView;
import ncku.fruitpedia.fragment1_fruitlist.FruitContentActivity;
import ncku.fruitpedia.main.FruitpediaDbHelper;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import example.android.tabsample1.R;

public class Frag3_Favorites extends ListFragment {	
	
	private ArrayList<String> favoritesFruit = null;
	private FruitpediaDbHelper fruitpediaDbHelper = null;
	 // 圖片和水果全名(中+英) 對應關係
	public ArrayList<HashMap<String,Object>> fruitListMap;
	
	@Override
	public void onAttach(Activity activity) {
		Log.d("test","onAttach");
		super.onAttach(activity);
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("test","onCreate");
		super.onCreate(savedInstanceState);
	}
	
	// onCreateView 方法 (區塊起始畫面的事件處理器)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		Log.d("test","onCreateView");
		//從畫面設定檔取得 View 元件
		View view = inflater.inflate(R.layout.main_frag3_favorites, container, false);

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("test","onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		fruitpediaDbHelper = new FruitpediaDbHelper(getActivity());
		
		if(fruitpediaDbHelper.isFavoritesTableExists()){
			Log.d("test","table exist");
			favoritesFruitMap();
			ArrayAdapter<String> fruitNameArrayAdapter = new ArrayAdapter<String>(getActivity(), 
					android.R.layout.simple_expandable_list_item_1, favoritesFruit);
			setListAdapter(fruitNameArrayAdapter);
		}
		else{
			Log.d("test","table not exist");
			//若還沒我的最愛還沒有添加任何水果，顯示提醒圖片和文字
			LinearLayout frag3_layout = (LinearLayout) getActivity().findViewById(R.id.frag3_layout_id);
			LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			ImageView imgView = new ImageView(getActivity());
			imgView.setImageResource(R.drawable.pikachu);
			frag3_layout.addView(imgView, imParams);
		}

	}
	
	@Override
	public void onDestroyView() {
		Log.d("test","onDestroyView");
		super.onDestroyView();
		try {
			FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
			ft.remove(this).commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//從資料庫內把我的最愛之水果英文名拿出來，並和中英文對照的fruitNameMap比對，最後產生 中文水果名+英文水果名，並和圖片對照後產生simpleAdapter
	@SuppressLint("DefaultLocale") 
	private void favoritesFruitMap(){
		
		//從資料庫抓取我的最愛之水果名稱陣列
		ArrayList<String> favoritesNameArrayList = fruitpediaDbHelper.getFavoritesFruit();
		//水果名稱 : 將水果英文字首變大寫，因為資源池裡的水果英文名稱第一個字母大寫，不改變的話會找不到對應中文。
		String tmp = null;
		for(int i = 0; i < favoritesNameArrayList.size(); i++){
			tmp = favoritesNameArrayList.get(i).replaceFirst(favoritesNameArrayList.get(i).substring(0, 1), 
															 favoritesNameArrayList.get(i).substring(0, 1).toUpperCase());
			favoritesNameArrayList.set(i, tmp);
		}
		
		//從FruitArrayListView抓取中英文對照關係的map，方便之後print listview
		HashMap<String,String> fruitNameMap = new FruitArrayListView(getActivity()).fruitNameMap();
		//中文水果名+英文水果名
		favoritesFruit = new ArrayList<String>();
		for(int i = 0; i < favoritesNameArrayList.size(); i++){
			favoritesFruit.add(fruitNameMap.get(favoritesNameArrayList.get(i)) + " " + favoritesNameArrayList.get(i));
		}
	}
	
	//當水果列表被點選時要處理的動作，呼叫水果的詳細內文
	@SuppressLint("DefaultLocale") 
	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		//取得被點選的水果項目字串
		String strFruitClicked = getListView().getItemAtPosition(position).toString();
		
		//分析字串
		strFruitClicked = strFruitClicked.substring(strFruitClicked.indexOf(' ')+1, strFruitClicked.length()); //分析字串，只要英文字串
		strFruitClicked = strFruitClicked.toLowerCase();
		
		//建立意圖物件，從Frag1_FruistList到FruitContentActivity
		Intent intent = new Intent();
		intent.setClass(getActivity(), FruitContentActivity.class);
		
		//建立Bundle物件，來傳送水果像目字串
		Bundle bundle = new Bundle();
		bundle.putString("strFruitClicked", strFruitClicked);
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
}
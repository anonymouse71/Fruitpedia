package ncku.fruitpedia.fragment1_fruitlist;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import example.android.tabsample1.R;

@SuppressLint("DefaultLocale") 
public class Frag1_FruitList extends ListFragment {
	private SimpleAdapter fruitSimpleAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//利用FruitArrayList Class來拿到圖片資源, fruitArrayList.fruitListMap
		FruitArrayListView fruitArrayList = new FruitArrayListView(getActivity());
		
		//新增SimpleAdapter
		fruitSimpleAdapter = new SimpleAdapter(getActivity(), fruitArrayList.fruitListMap, R.layout.listitem_tab_fruitlist,
				new String[] { "imgView", "txtView" }, new int[] { R.id.imageId_fruit ,R.id.textviewId_fruit });
		setListAdapter(fruitSimpleAdapter);
	}
	
	// onCreateView 方法 (區塊起始畫面的事件處理器) 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.main_frag1_fruitlist, container, false);
		
		//從畫面設定檔取得 View 元件
		return view;
	}
	
	//當水果列表被點選時要處理的動作，呼叫水果的詳細內文
	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		
		//取得被點選的水果項目字串
		String strFruitClicked = getListView().getItemAtPosition(position).toString();
		
		//分析字串
		strFruitClicked = strFruitClicked.substring(strFruitClicked.indexOf(' ')+1, strFruitClicked.indexOf(',')); //分析字串，只要英文字串
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
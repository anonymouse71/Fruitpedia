/*
產生水果名和圖片對應的fruitListMap，方便其他class產生listview
拆離水果名的中文和英文，並用hashmap存起來
*/

package ncku.fruitpedia.fragment1_fruitlist;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import example.android.tabsample1.R;

public class FruitArrayListView {
	private Context context;
	
	 // 圖片和水果全名(中+英) 對應關係
	public ArrayList<HashMap<String,Object>> fruitListMap;
	
	//水果圖片對應水果名稱全名
	private static String[] fruitStrArray;	
	
	//水果圖片
	private static int[] fruitImgArray={R.drawable.apple_main, R.drawable.avocado_main, R.drawable.banana_main, R.drawable.blueberry_main,
		R.drawable.cantaloupe_main, R.drawable.cherry_main, R.drawable.cranberry_main,R.drawable.durian_main, R.drawable.grapefruit_main, 
		R.drawable.grapes_main, R.drawable.guava_main, R.drawable.kiwi_main, R.drawable.lemon_main,
	  	R.drawable.lychee_main, R.drawable.mango_main, R.drawable.orange_main, R.drawable.pagaya_main, R.drawable.peach_main, 
	  	R.drawable.pear_main ,R.drawable.persimmon_main, R.drawable.pineapple_main, R.drawable.starfruit_main,
	  	R.drawable.strawberry_main, R.drawable.tangerine_main, R.drawable.tomato_main, R.drawable.watermelon_main};

	
	public FruitArrayListView(Context ctx){
		context = ctx;
		fruitStrArray = context.getResources().getStringArray(R.array.strArray_FruitList);
		
		//將水果文字與圖片作mapping, 存放HashMap的ArrayList
		fruitListMap = new ArrayList<HashMap<String,Object>>();
		
		//將水果和對應的圖片mapping起來到mapArrayFruitlist
		for (int i = 0; i < fruitStrArray.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>(); //每個元素
			item.put("imgView", fruitImgArray[i]);
			item.put("txtView", fruitStrArray[i]);
			fruitListMap.add(item);
		}
		
		//將中英文拆離結果放入fruitNameMap
	}
	
	//水果中英文名字 對應關係
	public HashMap<String,String> fruitNameMap(){
		HashMap<String,String> fruitNameMap = new HashMap<String,String>();
		String cn = null; // 中文水果名
		String en = null; // 英文水果名
		for(int i = 0; i<fruitStrArray.length; i++){
			cn = fruitStrArray[i].substring(0,fruitStrArray[i].indexOf(" "));
			en = fruitStrArray[i].substring(fruitStrArray[i].indexOf(" ")+1, fruitStrArray[i].length());
			fruitNameMap.put(en,cn);
		}
		return fruitNameMap;
	}
}

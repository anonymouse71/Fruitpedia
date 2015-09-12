package ncku.fruitpedia.fragment2_bulletin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled") 
public class Frag2_Bulletin extends WebViewFragment  {
		ProgressDialog mProgress;
	
	 	static Frag2_Bulletin newInstance(String title) {
	 		Frag2_Bulletin fragment = new Frag2_Bulletin();
			Bundle args = new Bundle();
			args.putString("title", title);
			fragment.setArguments(args);
			return fragment;
	    }

		@Override
	    public void onActivityCreated(final Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        Log.d("test","0");
	        if(!netConnect(getActivity())){
	        	Toast.makeText(getActivity(), "您還沒有連上網路。", Toast.LENGTH_SHORT).show();
	        }
	        else{
		        WebView webView = getWebView();
		        WebSettings setting = webView.getSettings();
		        setting.setJavaScriptEnabled(true);
		        //setting.setPluginsEnabled(true);
		        
		    	// the init state of progress dialog
		        mProgress = ProgressDialog.show(getActivity(), "Loading", "佈告欄加載中，請稍等....\n(第一次可能較久)");
	
		        // add a WebViewClient for WebView, which actually handles loading data from web
		        webView.setWebViewClient(new WebViewClient(){
		            // load url
		            public boolean shouldOverrideUrlLoading(WebView view, String url) {
		                view.loadUrl(url);
		                return true;
		            }
		 
		            // when finish loading page
		            public void onPageFinished(WebView view, String url) {
		                if(mProgress.isShowing()) {
		                    mProgress.dismiss();
		                }
		            }
		        }); 
		        
		        // set url for webview to load
		        webView.loadUrl("http://fruitpedia.blogspot.tw/");
	        }
	    }
		
		//Check the internet    && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()
		public boolean netConnect(Context ctx){
		    ConnectivityManager cm;
		    NetworkInfo info = null;
		    try{
		        cm = (ConnectivityManager) 
		              ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		        info = cm.getActiveNetworkInfo();
		    }
		    catch (Exception e){
		        e.printStackTrace();
		    }
		    if (info != null){
		        return true;
		    }
		    else{
		        return false;
		    }
		}
}
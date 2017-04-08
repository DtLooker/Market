package com.looker.market;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.looker.market.bean.Wares;
import com.looker.market.util.CartProvider;
import com.looker.market.widget.MToolbar;
import com.looker.market.widget.MWaitDialog;

import java.io.Serializable;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class WaresDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private MToolbar mToolbar;
    private WebView mWebView;
    private Wares mWares;
    private CartProvider mProvider;
    private WebAppInterface mAppInterface;
    private MWaitDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wares_detail);

        /**mob shareSdk 初始化**/
        ShareSDK.initSDK(this);

        Serializable extra = getIntent().getSerializableExtra(Constants.WARE);
        if (extra == null){
            this.finish();
        }

        mDialog = new MWaitDialog(this);
        mDialog.show();

        mWares = (Wares) extra;
        mProvider = CartProvider.getInstance(this);

        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        mToolbar = (MToolbar) findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setRightButtonText("分享");
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });

    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //加载JS上的图片
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(Constants.API.WARES_DETAIL);
        mAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterface, "appInterface");
        mWebView.setWebViewClient(new MWebClient());

    }

    @Override
    public void onClick(View view) {
        this.finish();
    }

    class MWebClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();

                mAppInterface.showDetail();
            }


        }
    }

    class WebAppInterface{

        private Context mContext;

        public WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" +mWares.getId()+")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id){

            mProvider.put(mWares);
            Toast.makeText(mContext, "已添加到购物车", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void addFavorites(long id){

        }
    }

    /***
     * 分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(mWares.getName());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mWares.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}

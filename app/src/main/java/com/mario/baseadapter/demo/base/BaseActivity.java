package com.mario.baseadapter.demo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mario.baseadapter.demo.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected TextView mTitle;
    protected TextView mMore;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(loadLayoutId());
        initToolbar();
        initViews();
        initData();
        bindListener();
        onProcessLogic();
    }


    protected void initToolbar() {
        mToolbar = $(R.id.toolbar);
        if (mToolbar == null)
            return;
        setSupportActionBar(mToolbar);
        mTitle = $(R.id.toolbar_title);
        mMore = $(R.id.toolbar_more);
    }


    protected <T extends View> T $(@IdRes int viewId) {
        return findViewById(viewId);
    }

    protected <T extends View> T $(View view, @IdRes int viewId) {
        return view.findViewById(viewId);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 为Activity加载布局文件
     */
    protected abstract int loadLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 初始化数剧
     */
    protected abstract void initData();

    /**
     * 为控件设置监听
     */
    protected void bindListener() {

    }

    /**
     * 逻辑操作，网络请求
     */
    protected void onProcessLogic() {

    }

    /**
     * 控件点击回调
     */
    protected void onClick(View view, int id) {

    }


    public void showToast(String message) {
        if (null != message)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitle != null)
            mTitle.setText(title);
    }
}

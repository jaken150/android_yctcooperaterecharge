package com.gzyct.app.api;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzyct.app.api.util.AppConstant;
import com.shelwee.update.UpdateHelper;

import java.util.Calendar;

public class AboutActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initListener();

    }

    private void initListener(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHelper updateHelper = new UpdateHelper.Builder(AboutActivity.this)
                        .checkUrl(AppConstant.getYCTIUrl()+"/update.html")
                        .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
                        .build();
                updateHelper.check();
            }
        });

        findViewById(R.id.tv_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApp.toast("功能开发中！");
            }
        });

        findViewById(R.id.tv_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(AppConstant.getYCTIUrl()+"busiruleForAPP.html");
                Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        String copyright = "Copyright © 1999-2016 GZYCT";
        Calendar calendar = Calendar.getInstance();
        copyright = copyright.replace("2016",""+calendar.get(Calendar.YEAR));
        ((TextView) findViewById(R.id.tv_copyright)).setText(copyright);

        ((TextView)findViewById(R.id.tv_version)).setText("版本：V"+UpdateHelper.getPackageInfo(this).versionName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

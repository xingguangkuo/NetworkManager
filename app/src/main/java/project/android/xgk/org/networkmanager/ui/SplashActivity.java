package project.android.xgk.org.networkmanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import project.android.xgk.org.networkmanager.R;
import project.android.xgk.org.networkmanager.ui.util.NetManagerUtil;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private TextView tv_version;
    private LinearLayout LinearLayout01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "进入SplashActivity-onCreate()");
        super.onCreate(savedInstanceState);
        // 取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        tv_version = (TextView) this.findViewById(R.id.tv_version);
        tv_version.setText("版本:" + getVersion());

        LinearLayout01 = (LinearLayout) this.findViewById(R.id.LinearLayout01);
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(2000);
        LinearLayout01.startAnimation(aa);

    }
    @Override
    protected void onStart() {
        super.onStart();

        if(NetManagerUtil.checkNet(SplashActivity.this)){
            Log.i(TAG,"进入主界面");
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();

        }else{
            //弹出对话框 让用户设置网络
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置网络");
            builder.setMessage("网络错误请设置网络");
            builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    if(android.os.Build.VERSION.SDK_INT > 10 ){
                        //3.0以上打开设置界面
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }else
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
        }
    }

    private String getVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  "";
        }
    }

}

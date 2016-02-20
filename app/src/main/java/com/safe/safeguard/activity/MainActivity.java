package com.safe.safeguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.safe.safeguard.R;
import com.safe.safeguard.constant.Url;
import com.safe.safeguard.entity.VersionInfo;
import com.safe.safeguard.utils.MD5Utils;
import com.safe.safeguard.utils.StringUtils;
import com.safe.safeguard.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = "TAG";
    private GridView gv_main;
    private GridAdapter gridAdapter;
    private String[] names;
    private int[] imageResource;
    private SharedPreferences updateSp;
    private SharedPreferences configSp;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initData();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }).start();
        if (updateSp.getBoolean("isChecked", false)) {

            checkVersion();
        }
        registerListener();
    }

    /**
     * 檢查版本
     */
    private void checkVersion() {

//            new VersionAsyncTask().execute();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Url.VERSION + "versioninfo.txt").build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                    call.cancel();
                Log.e(TAG, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e(TAG, "onResponse == " + string);
                //解析json数据
                //{"code":"2.0","apkurl":"http://192.168.79.201:8080/safeguard/zssq.apk","des":"鏂扮増鏈笂绾夸簡锛岃刀蹇笅杞藉惂锛�"}
                Gson gson = new Gson();
                final VersionInfo versionInfo = gson.fromJson(string, VersionInfo.class);
                Log.e(TAG, "versionInfo == " + versionInfo);

                if (!StringUtils.isNullOrEmpty(versionInfo.code) && !StringUtils.isNullOrEmpty(getVersionName()) && !getVersionName().equals(versionInfo.code)) {
                    //需要提升升级
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showUpdateDialog(versionInfo);
                        }
                    });
                }
            }
        });
    }

    private void showUpdateDialog(final VersionInfo versionInfo) {
        new AlertDialog.Builder(this)
                .setMessage(versionInfo.des)
                .setCancelable(false)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk(versionInfo.apkurl);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void downloadApk(String apkurl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apkurl).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "downloadapk  onResponse");
                InputStream is = null;
                OutputStream os = null;
                try {
                    is = response.body().byteStream();
                    long l = response.body().contentLength();
                    Log.e(TAG, "contentLength == " + l);

                    byte[] buffer = new byte[1024];
                    String path = "/mnt/sdcard/aaa.apk";
                    String s = Environment.getExternalStorageDirectory().getAbsolutePath() + path;

                    File file = new File(path);

                    os = new FileOutputStream(file);
                    int length = -1;
                    while ((length = is.read(buffer)) != -1) {
                        os.write(buffer, 0, length);
                        os.flush();
                    }
                    //下载完成  安装
                    installApk();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        is.close();
                    } else if (os != null) {
                        os.close();
                    }
                }
            }
        });
    }

    private void installApk() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");

        //因为setType和setData是会相互覆盖，不适用
//		intent.setType("application/vnd.android.package-archive");
//		intent.setData(Uri.fromFile(new File("/mnt/sdcard/mobliesafe612.0.apk")));

        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/aaa.apk")), "application/vnd.android.package-archive");
        //当前当前 activity消失的时候，会调用以前的activity的onActivityResult方法
        startActivityForResult(intent, 0);
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;

            Log.e(TAG, "checkVersion: Versionname = " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    private void initData() {

        updateSp = getSharedPreferences("update", MODE_PRIVATE);
        configSp = getSharedPreferences("config", MODE_PRIVATE);

        names = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        imageResource = new int[]{R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app, R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan, R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings};
        gridAdapter = new GridAdapter();

        gv_main.setAdapter(gridAdapter);
    }

    private void initView() {
        gv_main = (GridView) findViewById(R.id.gv_main);
    }

    private void registerListener() {
        gv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e(TAG, "onItemClick: " + position);
                switch (position) {
                    case 0://手机防盗
                        //判断是否设置密码
                        String password = configSp.getString("password", "");
                        if (!TextUtils.isEmpty(password)) {
                            //已经设置了密码，弹出输入密码框
                            enterPassword();
                        } else {
                            //还未设置密码，弹出设置密码框
                            setPassword();
                        }

                        break;
                    case 1://通讯卫士
                        break;
                    case 2://软件管理
                        break;
                    case 3://进程管理
                        break;
                    case 4://流量统计
                        break;
                    case 5://手机杀毒
                        break;
                    case 6://缓存清理
                        break;
                    case 7://高级工具
                        break;
                    case 8://设置中心
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    int count = 0;

    /**
     * 输入密码
     */
    private void enterPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);//设置对话框不能消息
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
        final EditText ed_home_password = (EditText) view.findViewById(R.id.ed_home_password);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        ImageView iv_enterpassword_imageview = (ImageView)view.findViewById(R.id.iv_enterpassword_imageview);
        iv_enterpassword_imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //隐藏和显示输入的密码
                if (count % 2 == 0) {
                    //显示密码
                    ed_home_password.setInputType(1);
                } else {
                    //隐藏密码
                    ed_home_password.setInputType(129);
                }
                count++;
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = ed_home_password.getText().toString().trim();
                //判断输入的密码时候为空
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;//不允许用户在执行其他操作
                }
                //输入密码和保存的密码比较
                //获取保存的密码
                String passwrod_sp = configSp.getString("password", "");
                if (MD5Utils.encryption(password).equals(passwrod_sp)) {
                    //提醒用户，消除对话框，跳转手机防盗界面
//                    Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //跳转手机防盗界面
                    Intent intent = new Intent(MainActivity.this,LostFindActivity.class);
                    startActivity(intent);
                }else{
                    //提醒用户密码错误
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
//        builder.setView(view);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /**
     * 设置密码
     */
    private void setPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);//设置对话框不能消息
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
        final EditText ed_home_password = (EditText) view.findViewById(R.id.ed_home_password);
        final EditText ed_home_password_confirm = (EditText) view.findViewById(R.id.ed_home_password_confirm);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = ed_home_password.getText().toString().trim();
                //判断输入的密码时候为空
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;//不允许用户在执行其他操作
                }
                String password_confirm = ed_home_password_confirm.getText().toString().trim();
                if (password.equals(password_confirm)) {
                    //保存密码
                    SharedPreferences.Editor edit = configSp.edit();
                    edit.putString("password", MD5Utils.encryption(password));
                    edit.commit();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                    //跳转手机防盗界面
                    Intent intent = new Intent(MainActivity.this,LostFindActivity.class);
                    startActivity(intent);
                }else{
                    //提醒用户
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        //viewSpacingLeft:距离左边的距离
        //viewSpacingTop：距离上部的距离
        //viewSpacingRight：距离右边的距离
        //viewSpacingBottom：距离下部的距离
//        builder.setView(view);//根据dialog.setview的效果一样
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }

    private class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.gridviewitem, null);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.iv_icon.setImageResource(imageResource[position]);
            holder.tv_name.setText(names[position]);

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

//    /**
//     * 请求网络，获取最新版本信息
//     */
//    private class VersionAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            return null;
//        }
//    }
}

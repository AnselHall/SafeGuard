package com.safe.safeguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.safe.safeguard.R;
import com.safe.safeguard.constant.Url;
import com.safe.safeguard.entity.VersionInfo;
import com.safe.safeguard.utils.StringUtils;

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

public class MainActivity extends Activity {

    private static final String TAG = "TAG";
    private GridView gv_main;
    private GridAdapter gridAdapter;
    private String[] names;
    private int[] imageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkVersion();
            }
        }).start();
        registerComponent();
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
        names = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        imageResource = new int[]{R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app, R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan, R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings};
        gridAdapter = new GridAdapter();

        gv_main.setAdapter(gridAdapter);
    }

    private void initView() {
        gv_main = (GridView) findViewById(R.id.gv_main);
    }

    private void registerComponent() {
        gv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: " + position);
                switch (position) {
                    case 0://手机防盗
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

    /**
     * 请求网络，获取最新版本信息
     */
    private class VersionAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }
    }
}

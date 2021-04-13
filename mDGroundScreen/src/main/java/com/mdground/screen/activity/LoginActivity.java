package com.mdground.screen.activity;

import org.apache.http.Header;
import org.mdground.api.base.PlatformType;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.ResponseCode;
import org.mdground.api.base.ResponseData;
import org.mdground.api.bean.Clinic;
import org.mdground.api.bean.Device;
import org.mdground.api.bean.Employee;
import org.mdground.api.server.clinic.GetClinic;
import org.mdground.api.server.global.LoginEmployee;
import org.mdground.api.utils.DebugUtil;
import org.mdground.api.utils.DeviceUtils;
import org.mdground.api.utils.StringUtil;

import com.mdground.screen.MedicalApplication;
import com.mdground.screen.R;
import com.mdground.screen.constant.MemberConstant;
import com.mdground.screen.permissionLibrary.PermissionsManager;
import com.mdground.screen.permissionLibrary.PermissionsResultAction;
import com.mdground.screen.receiver.ConnectChangeReceiver;
import com.mdground.screen.utils.DeviceIDUtil;
import com.mdground.screen.utils.DisplayUtils;
import com.mdground.screen.utils.L;
import com.mdground.screen.utils.NetworkStatusUtil;
import com.mdground.screen.utils.PreferenceUtils;
import com.mdground.screen.utils.SharedPreferUtils;
import com.mdground.screen.utils.SharedPreferUtils.ShareKey;
import com.mdground.screen.utils.crash.CrashHandler;
import com.mdground.screen.utils.mail.MailManager;
import com.mdground.screen.view.dialog.LoadingDialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class LoginActivity extends Activity implements OnClickListener {

    //private ResizeLayout LoginRootLayout;
    private EditText et_account, et_password;

    private LoadingDialog mLoadIngDialog;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyy_MM_dd HH:mm:ss");

    private BroadcastReceiver wifiConnectedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mLoadIngDialog.isShowing()) {
                autoLogin();
            }
        }
    };
    private int allow = 0;
    private int checkTimes = 0;
    private String[] permissionss;
    private static String fileName = "YiDeGuanScreenLogin";
    private static String fileType = ".txt";

    private String path = null;
    private String pathCrash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPower1();
        L.e("哈哈", "LoginActivity");
        DeviceIDUtil.createFile(this);
        String token = DeviceIDUtil.getToken(getApplicationContext());
        //String token = XGPushConfig.getToken(getApplicationContext());
        L.e("哈哈", "token是 : " + token);
//
//        XGPushManager.registerPush(getApplicationContext());

        setContentView(R.layout.activity_login2);
        DisplayUtils.HiddenStateBar(this, true);
        findViewById();
        setListener();

        mLoadIngDialog = new LoadingDialog(this).initText(getResources().getString(R.string.logining));

        if (android.os.Build.MODEL != null && !android.os.Build.MODEL.equals(""))
            fileName = android.os.Build.MODEL;

        path = DeviceIDUtil.getFile_path(this) + "/" + fileName + fileType;
        pathCrash = PreferenceUtils.getPrefString(this, MemberConstant.CRASH_PATH, "");
        // pathCrash = DeviceIDUtil.getFile_path(this) + "/" + CrashHandler.crashFile;
        sendEMail(pathCrash);
        PreferenceUtils.setPrefString(this, MemberConstant.CRASH_PATH, "");

        PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_OUT);

        autoLogin();

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(wifiConnectedReceiver,
                new IntentFilter(ConnectChangeReceiver.CONNECTED));

    }

    /**
     * 权限申请
     */
    @SuppressLint("HardwareIds")
    private void requestPower1() {
        permissionss = new String[]{
//                Manifest.permission.CAMERA,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_SYNC_SETTINGS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //权限申请
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(LoginActivity.this, permissionss,
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {
                        //Toast.makeText(LoginActivity.this, "Permissions failed. Please click to allow Permissions", Toast.LENGTH_SHORT).show();
                    }
                }, true);

    }

    /**
     * 加个获取权限的监听//顺便测试一下搜狗输入法
     */
    @SuppressLint(value = {"MissingPermission", "HardwareIds"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionsLength = permissions.length;
        for (int i = 0; i < permissions.length; i++) {
//            if (permissions[i].equals(Manifest.permission.CAMERA)) {
//                allow++;
//            }
//            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                allow++;
//            }

            if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.CALL_PHONE)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.WRITE_SETTINGS)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.READ_SYNC_SETTINGS)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                allow++;
            }

            if (permissions[i].equals(Manifest.permission.ACCESS_WIFI_STATE)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.CHANGE_WIFI_STATE)) {
                allow++;
            }
            if (permissions[i].equals(Manifest.permission.WRITE_APN_SETTINGS)) {
                allow++;
            }

            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                allow++;
            }
        }
        if (allow == permissionss.length) {

        } else if (allow == permissionsLength) {

        } else {
            allow = 0;
            checkTimes++;
            if (checkTimes > 2) {
                checkTimes = 0;
                Toast.makeText(LoginActivity.this, "没有必要权限，请手动开启", Toast.LENGTH_LONG).show();
                return;
            }
            requestPower1();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //DisplayUtils.HiddenStateBar(this, true);
//		CrashManager.register(this, "503880ea15f946c5a47042feda3e6517", new MyCrashManagerListener());
    }

    private void findViewById() {
        //LoginRootLayout = (ResizeLayout) this.findViewById(R.id.login_root_layout);
        //scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);

        String username = PreferenceUtils.getPrefString(this, MemberConstant.USERNAME, "");
        if (username != null) {
            et_account.setText(username);
        }
    }

    private void setListener() {
        //LoginRootLayout.setOnResizeListener(this);
    }

    private void autoLogin() {
//		L.e(this, "autoLogin()");
        // 自动登录
        String username = PreferenceUtils.getPrefString(LoginActivity.this, MemberConstant.USERNAME, null);
        String password = PreferenceUtils.getPrefString(LoginActivity.this, MemberConstant.PASSWORD, null);

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

            login(username, password);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login:

                if (!NetworkStatusUtil.isConnected(this)) {
                    Toast.makeText(this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                    return;
                }

                String acccount = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(acccount)) {
                    Toast.makeText(getApplicationContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                int deviceID = PreferenceUtils.getPrefInt(getApplicationContext(), MemberConstant.DEVICE_ID, -1);

                L.e(LoginActivity.this, "deviceID是 : " + deviceID);

                // login("vivi", "123");
                // login("2000078", "123");
                login(acccount, password);

                break;

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //判断是否有焦点
        DisplayUtils.HiddenStateBar(this, hasFocus);

    }


//	@Override
//	public void OnResize(int w, final int h, int oldw, final int oldh) {
////		L.e(LoginActivity.this, "OnResize()");
//		Handler handler = new Handler(Looper.getMainLooper());
//		handler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				int offset = oldh - h;
//				if (offset > 0) {
//					scrollView.scrollTo(0, offset);
//				}
//			}
//		});
//	}


    private void saveLoginMsg(String msg) {
        File file = new File(DeviceIDUtil.getFile_path(this), fileName + fileType);

//		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			file = new File(Environment.getExternalStorageState(), fileName);
//		}

        try {
            OutputStream out;
            out = new FileOutputStream(file, false);
            out.write(msg.getBytes());
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void sendEMail(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        //MailManager.getInstance().sendMail("导诊屏", "登录信息");
        MailManager.getInstance().sendMailWithFile("导诊屏", "登录信息", path);
    }

    private void login(final String userName, final String password) {

        final Device device = DeviceUtils.getDeviceInfo(getApplicationContext());
        device.setPlatform(PlatformType.AndroidScreen.platform()); // 设死成3
        device.setDeviceID(new DeviceIDUtil().getDeviceID(LoginActivity.this));

        final String token = DeviceIDUtil.getToken(getApplicationContext());
        L.e(LoginActivity.this, "token是 : " + token);

        if (token == null || "".equals(token)) {
            Toast.makeText(this, "信鸽token为空,登录失败", Toast.LENGTH_SHORT).show();
            //XGPushManager.registerPush(getApplicationContext());
            return;
        } else {
            new DeviceIDUtil().saveXgPushTokenToSDCard(LoginActivity.this, token);
        }

        device.setDeviceToken(token);


        new LoginEmployee(this).loginEmployee(userName, password, device, new RequestCallBack() {

            @Override
            public void onStart() {
                mLoadIngDialog.show();
            }

            @Override
            public void onSuccess(ResponseData response) {

                String builder = "时间：" + simpleDateFormat.format(new Date()) +
                        "\n设备信息：\n" + device.toString() +
                        "\ntoken:\n" +
                        token +
                        "\nresponse:\n" +
                        "Code:" + response.getCode() + "\n" +
                        "Content:" + response.getContent() + "\n" +
                        "Message:" + response.getMessage() + "\n";

                DebugUtil.e("LoginEmployee", builder);

                saveLoginMsg(builder);
                sendEMail(path);


                Employee employee = response.getContent(Employee.class);

                if (response.Code != 0) {
                    Toast.makeText(getApplicationContext(), "账号异常,请联系客服", Toast.LENGTH_LONG).show();
                    return;
                }

                ((MedicalApplication) LoginActivity.this.getApplication()).setLoginEmployee(employee);

                new SharedPreferUtils(getApplicationContext()).put(ShareKey.DEVICE_ID, employee.getDeviceID());

                PreferenceUtils.setPrefLong(getApplicationContext(), MemberConstant.LOGIN_EMPLOYEE,
                        employee.getEmployeeID());
                PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.LOGIN_STATUS,
                        MemberConstant.LOGIN_IN);
                PreferenceUtils.setPrefString(getApplicationContext(), MemberConstant.USERNAME, employee.getLoginID());
                PreferenceUtils.setPrefString(getApplicationContext(), MemberConstant.PASSWORD, employee.getLoginPwd());
                PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.DEVICE_ID, employee.getDeviceID());
                new DeviceIDUtil().saveDeviceIDToSDCard(LoginActivity.this, employee.getDeviceID());

                int EmployeeRole = ((MedicalApplication) LoginActivity.this.getApplication()).getLoginEmployee()
                        .getEmployeeRole() & 8;

                L.e(LoginActivity.this, "getEmployeeRole : " + ((MedicalApplication) LoginActivity.this.getApplication())
                        .getLoginEmployee().getEmployeeRole());

                if (EmployeeRole != 0) {
//					new UpdateDeviceToken(getApplicationContext()).updateDeviceToken(token, new RequestCallBack() {
//
//						@Override
//						public void onSuccess(ResponseData response) {
//							L.e(LoginActivity.this, "code : " + response.Code);
//							L.e(LoginActivity.this, "message : " + response.Message);
//							L.e(LoginActivity.this, "上传token成功");
//						}
//
//						@Override
//						public void onStart() {
//
//						}
//
//						@Override
//						public void onFinish() {
//
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers, String responseString,
//								Throwable throwable) {
//
//						}
//					});
                    getClinic();
                } else {
                    Toast.makeText(getApplicationContext(), "账号异常,请联系客服", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFinish() {
                mLoadIngDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String builder = "时间：" + simpleDateFormat.format(new Date()) +
                        "\n设备信息：\n" + device.toString() +
                        "\ntoken:\n" +
                        token +
                        "\nError:responseString:\n" +
                        "statusCode:" + statusCode + "\n" +
                        "responseString:" + responseString + "\n" +
                        "throwable:" + throwable.toString() + "\n";

                saveLoginMsg(builder);
                sendEMail(path);

                L.e(LoginActivity.this, "statusCode : " + statusCode);
                L.e(LoginActivity.this, "登录失败, 返回的responseString : " + responseString);
                L.e(LoginActivity.this, throwable.toString());
            }
        });
    }

    private void getClinic() {
        new GetClinic(getApplicationContext()).getClinic(new RequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(ResponseData response) {
                if (response.getCode() == ResponseCode.Normal.getValue()) {
                    MedicalApplication application = (MedicalApplication) getApplication();

                    application.setmClinic(response.getContent(Clinic.class));

                    Intent intent = new Intent(LoginActivity.this, UnisoundMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(wifiConnectedReceiver);
    }
}

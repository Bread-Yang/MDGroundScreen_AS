package com.mdground.screen.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdground.screen.MedicalApplication;
import com.mdground.screen.R;
import com.mdground.screen.constant.MedicalConstant;
import com.mdground.screen.fonts.CustomTypefaceSpan;
import com.mdground.screen.service.DataService;
import com.mdground.screen.utils.L;
import com.mdground.screen.utils.UpdateManager;
import com.mdground.screen.view.FlickerTextView;
import com.mdground.screen.view.GridViewPager;
import com.mdground.screen.view.TwoWayGridView;
import com.mdground.screen.view.dialog.LoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.ResponseData;
import org.mdground.api.bean.Appointment;
import org.mdground.api.bean.Doctor;
import org.mdground.api.server.clinic.GetAppointmentInfoListByDoctor;
import org.mdground.api.server.clinic.GetDoctorInfoListByScreen;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.yunzhisheng.tts.offline.TTSPlayerListener;
import cn.yunzhisheng.tts.offline.basic.ITTSControl;
import cn.yunzhisheng.tts.offline.basic.TTSFactory;
import cn.yunzhisheng.tts.offline.common.USCError;
import fi.iki.elonen.NanoHTTPD;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class UnisoundMainActivity extends BaseActivity implements TTSPlayerListener {

    private final String TAG = "UnisoundMainActivity";
    private static final String TEST_KEYSTORE_PWD = "ssltest";
    private static final String YIDEGUAN_KEYSTORE_PWD = "yideguanA815";
    private Object apkLibraryInstaller;
    private int gridViewHeight = 0;
    private int gridViewWidth = 0;

    /*这类就是要自定义一些返回值，我在这里定义了700。都属于自定义*/
    enum Status implements NanoHTTPD.Response.IStatus {
        SWITCH_PROTOCOL(101, "Switching Protocols"),
        NOT_USE_POST(700, "not use post");

        private final int requestStatus;
        private final String description;

        Status(int requestStatus, String description) {
            this.requestStatus = requestStatus;
            this.description = description;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public int getRequestStatus() {
            return 0;
        }
    }

    private GridViewPager viewPager;
    private RadioGroup rg_page;
    private DoctorAdapter doctorAdapter;
    private TextView tv_page;
    private AppCompatTextView tv_highlight_num;
    private int totalNum;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 2;
    private Timer timer;
    private static final int CHANGE_PAGE = 0x01;
    private View pageView;
    private ArrayList<Doctor> mDoctorsArray = new ArrayList<Doctor>();
    private HashMap<String, Integer> doctorsIndex = new HashMap<String, Integer>();
    private HashMap<String, ArrayList<Appointment>> allDoctorAppointmentArray = new HashMap<String, ArrayList<Appointment>>();

    /**
     * 指定license路径，需要保证该路径的可读写权限
     */
    private static final String LICENCE_FILE_NAME = Environment.getExternalStorageDirectory()
            + "/tts/baidu_tts_licence.dat";

    private ITTSControl mTTSPlayer;

    private boolean isPlaying;

    private Queue<String> speechQueue = new LinkedList<String>();

    private int currentSpeechCount = 0;
    private String currentSpeechMessage;

    private ClientReciver mClientRecive;

    private XGPushReceiver xgReceiver;

    private SparseArray<View> registeredViews = new SparseArray<View>();

    private LoadingDialog mLoadIngDialog;

    private Typeface ttf_NotoSans_Bold, ttf_NotoSans_Regular;

    //    private Server mServer;
    private HttpServer mHttpServer;

    /**
     * 预约是来自wechat的
     */
    public static final int ONLINE = 1;

    public enum EXIT_CODE {
        A(104), B(203);

        private int numVal;

        EXIT_CODE(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    @SuppressLint("HandlerLeak")
    public class MyHandlerTimers extends Handler {

        public static final int TIMER_1 = 0;
        public static final int TIMER_2 = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_1:
                    Log.e(TAG, "定期检测Server状态");

//                    if (!mServer.isRunning()) {
//                        Log.e(TAG, "Server还没启动！！！！！！！！！！！！！！");
//                        mServer.startup();
//                    }
                    sendEmptyMessageDelayed(TIMER_1, 10000);
                    break;
                case TIMER_2:
                    Log.e(TAG, "定期刷新病人数据");

                    for (int i = 0; i < mDoctorsArray.size(); i++) {
                        getAppointmentListByDoctor(i, (int) mDoctorsArray.get(i).getDoctorID());
                    }

                    sendEmptyMessageDelayed(TIMER_2, 10000);
                    break;
            }
        }
    }

//    class CallPatientHandler implements RequestHandler {
//
//        @Override
//        public void handle(final HttpRequest request, HttpResponse response, HttpContext context) {
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Map<String, String> params = HttpRequestParser.parseParams(request);
//
//                        String message = params.get("message");
//
//                        JSONObject json = new JSONObject(message);
//
//                        int opNO = json.getInt("OPNo");
//                        int doctorID = json.getInt("DoctorID");
//
//                        callAction(doctorID, opNO, message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    }

    class ClientReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable() {
                public void run() {
                    if ("com.mdground.message".equals(intent.getAction())) {
                        String message = intent.getStringExtra("message");
                        L.e(UnisoundMainActivity.this, "服务器推送过来的信息是 : " + message);
                        JSONObject json;
                        try {
                            json = new JSONObject(message);
                            int opNO = json.getInt("OPNo");
                            int doctorID = json.getInt("DoctorID");
                            int action = json.getInt("Action");
                            if (action == 1) { // 叫号
                                callAction(doctorID, opNO, message);
                            } else if (action == 2) { // 状态上报
                                int OPStatus = json.getInt("OPStatus");
                                if ((OPStatus & Appointment.STATUS_DIAGNOSING) != 0) {
                                    showStartOpNO(doctorID, opNO); // 开始
                                    // 重新拉一次数据
                                    Integer index = doctorsIndex.get(String.valueOf(doctorID));
                                    if (index != null) {
                                        getAppointmentListByDoctor(index, doctorID);
                                    }
                                    // for (int i = 0; i < doctorsArray.size();
                                    // i++) {
                                    // getAppointmentListByDoctor(i, (int)
                                    // doctorsArray.get(i).getDoctorID());
                                    // }
                                } else if ((OPStatus & Appointment.STATUS_FINISH) != 0) { // 结束
                                    hideOpNO(doctorID, opNO);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    class XGPushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.mdground.screen.xgPush".equals(intent.getAction())) {
                // Toast.makeText(MainActivity.this, "收到推送", Toast.LENGTH_SHORT)
                // .show();

                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                String customContent = intent.getStringExtra("customContent");
                L.e(UnisoundMainActivity.this, "拿到的title : " + title);
                L.e(UnisoundMainActivity.this, "拿到的content : " + content);
                L.e(UnisoundMainActivity.this, "拿到的customContent : " +
                        customContent);

                L.e(UnisoundMainActivity.this, "收到推送");

                try {
                    JSONObject json = new JSONObject(customContent);
                    String functionName = json.getString("FunctionName");

                    // 更新列表的推送
                    if ("RefreshAppointment".equals(functionName)) {
                        // Toast.makeText(getApplicationContext(),
                        // "收到重新更新列表的推送", Toast.LENGTH_SHORT).show();

                        int doctorId = Integer.valueOf(content);

                        Integer index = doctorsIndex.get(String.valueOf(doctorId));

                        if (index != null) {
                            getAppointmentListByDoctor(index, doctorId);
                        }

                        // for (int i = 0; i < doctorsArray.size(); i++) {
                        // ((FlickerTextView) registeredViews.get(i)
                        // .findViewById(R.id.tv_opNO))
                        // .setVisibility(View.INVISIBLE);
                        //
                        // getAppointmentListByDoctor(i, (int) doctorsArray
                        // .get(i).getDoctorID());
                        // }
                    } else if ("UpgradeScreenVersion".equals(functionName)) { // 更新
                        UpdateManager manager = new UpdateManager(UnisoundMainActivity.this);
                        manager.showDownloadDialog();
                    } else if ("CallAppointment".equals(functionName)) { // 叫号
                        L.e(this, "收到叫号推送");
                        JSONObject messageJson = new JSONObject(content);

                        int opNO = messageJson.getInt("OPNo");
                        int doctorID = messageJson.getInt("DoctorID");

                        callAction(doctorID, opNO, content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class HttpServer extends NanoHTTPD {

        public HttpServer(int port) {
            super(port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            /*我在这里做了一个限制，只接受POST请求。这个是项目需求。*/
            if (Method.POST.equals(session.getMethod())) {
                Map<String, String> files = new HashMap<String, String>();
                /*获取header信息，NanoHttp的header不仅仅是HTTP的header，还包括其他信息。*/
                Map<String, String> header = session.getHeaders();

                try {
                    /*这句尤为重要就是将将body的数据写入files中，大家可以看看parseBody具体实现，倒现在我也不明白为啥这样写。*/
                    session.parseBody(files);
                    /*看就是这里，POST请教的body数据可以完整读出*/
                    String body = session.getQueryParameterString();
                    /*这里是从header里面获取客户端的IP地址。NanoHttpd的header包含的东西不止是HTTP heaer的内容*/
                    header.get("http-client-ip");

                    final String postData = files.get("postData");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(postData);
                                int opNO = json.getInt("OPNo");
                                int doctorID = json.getInt("DoctorID");

                                callAction(doctorID, opNO, postData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ResponseException e) {
                    e.printStackTrace();
                }
                /*这里就是为客户端返回的信息了。我这里返回了一个200和一个HelloWorld*/
                Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "叫号成功");

                response.addHeader("Access-Control-Allow-Origin", "*");

                return response;
            } else if (Method.GET.equals(session.getMethod())) {
                String reponseString = null;

                try {
                    final String message = session.getParms().get("CallMessage");

                    if (TextUtils.isEmpty(message)) {
                        reponseString = "参数有误！！！";
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(message);
                                    int opNO = json.getInt("OPNo");
                                    int doctorID = json.getInt("DoctorID");

                                    callAction(doctorID, opNO, message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        reponseString = "叫号成功";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    reponseString = "参数有误！！！";
                }

                /*这里就是为客户端返回的信息了。我这里返回了一个200和一个HelloWorld*/
                Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", reponseString);

                response.addHeader("Access-Control-Allow-Origin", "*");

                return response;
            } else {
                Response response = newFixedLengthResponse(Status.NOT_USE_POST, "text/html", "请使用post");

                response.addHeader("Access-Control-Allow-Origin", "*");

                return response;
            }
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //XGPushManager.registerPush(getApplicationContext());
        setContentView(R.layout.activity_main);
        findViewById();
        init();
        setListeners();
        initService();
        initUnisoundTTS();
        getDoctorList();

        mLoadIngDialog = new LoadingDialog(this).initText(getResources().getString(R.string.logining));

        // new Handler().postDelayed(new Runnable() {
        // public void run() {
        // UpdateManager manager = new UpdateManager(MainActivity.this);
        // manager.showDownloadDialog();
        // }
        // }, 5000);

        // new Handler().postDelayed(new Runnable() {
        // public void run() {
        // int[] i = new int[2];
        // i[3] = 0;
        // }
        // }, 20000);

        createAndServer();
        createNanoHttpd();

        MyHandlerTimers timer = new MyHandlerTimers();
        timer.sendEmptyMessageDelayed(MyHandlerTimers.TIMER_1, 10000);
        timer.sendEmptyMessageDelayed(MyHandlerTimers.TIMER_2, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CrashManager.register(this, "503880ea15f946c5a47042feda3e6517", new
        // MyCrashManagerListener());
    }

    private void createAndServer() {
//        mServer = AndServer.serverBuilder()
////                .sslContext(getSLLContext())
////                .sslSocketInitializer(new SSLInitializer())
//                .port(9000)
//                .registerHandler("/callPatient", new CallPatientHandler())
//                .exceptionResolver(new ExceptionResolver() {
//                    @Override
//                    public void resolveException(Exception e, HttpRequest request, HttpResponse response, HttpContext context) {
//                        Log.e(TAG, "注册服务器的时候报异常");
//                    }
//                })
//                .build();
//
//        mServer.shutdown();
//        mServer.startup();
    }

    private void createNanoHttpd() {
        mHttpServer = new HttpServer(9000);
        try {
//            InputStream inputStream = null;
//            //选择安全协议的版本
//            SSLContext ctx = SSLContext.getInstance("TLS");
//            KeyManagerFactory keyManagers = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            //选择keystore的储存类型，andorid只支持BKS
//            KeyStore ks = KeyStore.getInstance("BKS");
//
//            inputStream = getResources().openRawResource(R.raw.test);
//            ks.load(inputStream, TEST_KEYSTORE_PWD.toCharArray());
//            keyManagers.init(ks, TEST_KEYSTORE_PWD.toCharArray());

//            inputStream = getResources().openRawResource(R.raw.yideguan);
//            ks.load(inputStream, YIDEGUAN_KEYSTORE_PWD.toCharArray());
//            keyManagers.init(ks, YIDEGUAN_KEYSTORE_PWD.toCharArray());


//            KeyStore trustStore = KeyStore.getInstance("PKCS12", "BC");
//            trustStore.load(getResources().openRawResource(R.raw.yideguannew), "123456".toCharArray());
//            keyManagers.init(trustStore, "123456".toCharArray());
//
//            ctx.init(keyManagers.getKeyManagers(), null, null);
//
//            SSLServerSocketFactory serverSocketFactory = ctx.getServerSocketFactory();
//            mHttpServer.makeSecure(serverSocketFactory,null);

            mHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }

//        try {
//            AssetManager am = getAssets();
//            //InputStream ins1 = am.open("server.cer");
//            InputStream ins2 = am.open("android.kbs");
//
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(ins2, null);
//
//            //读取证书,注意这里的密码必须设置
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "android".toCharArray());
//
//            mHttpServer.makeSecure(NanoHTTPD.makeSSLSocketFactory(keyStore, keyManagerFactory), null);
//            mHttpServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
//
//
//        } catch (IOException e) {
//            Log.e("IOException", "Couldn't start server:\n" + e.getMessage());
//        } catch (NumberFormatException e) {
//            Log.e("NumberFormatException", e.getMessage());
//        } catch (KeyStoreException  e) {
//            Log.e("HTTPSException", "HTTPS certificate error:\n " + e.getMessage());
//        } catch (UnrecoverableKeyException e) {
//            Log.e("UnrecoverableKeyExcep", "UnrecoverableKeyException" + e.getMessage());
//        }   catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }

    private SSLContext getSLLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private void callAction(int doctorID, int opNO, String message) {


        // 如果当前发过来的opNO是正在播放的,则不添加进队列里面

        // 当正在播放林医生的叫号时,这时收到黄医生的叫号,则继续播放林医生的叫号,不过要在导诊屏上的黄医生的位置显示黄医生的叫号
        if (!isOpNoTextViewVisible(doctorID)) {
            JSONObject json;
            try {
                json = new JSONObject(message);

                String patientName = json.getString("PatientName");
                startCallPatient(doctorID, opNO, patientName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 当正在播放4001号的叫号时,在播放两次声音的时间之内,又收到了一次同样是4001号的叫号,则只播放两次,而不是播放四次4001的叫号
        if (currentSpeechMessage != null) {
            CallAppointment currentCall = new CallAppointment(currentSpeechMessage);
            if (currentCall.getOpNO() == opNO) {
                return;
            }
        }

        L.e(this, "isPlaying : " + isPlaying);
        L.e(this, "currentSpeechCount == " + currentSpeechCount);

        if (speechQueue.size() == 0 && !isPlaying && currentSpeechCount == 0) {
            speech(message);
        } else {

            for (String callMessage : speechQueue) {
                try {
                    if (opNO == new JSONObject(callMessage).getInt("OPNo")) {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            L.e(this, "speechQueue.offer(message)执行了");
            speechQueue.offer(message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void findViewById() {
        viewPager = (GridViewPager) findViewById(R.id.gvp);
        rg_page = (RadioGroup) findViewById(R.id.rg_page);
        tv_page = (TextView) findViewById(R.id.page);
        tv_highlight_num = (AppCompatTextView) findViewById(R.id.tv_highlight_num);
        pageView = findViewById(R.id.page_view);

    }

    private void init() {
        ttf_NotoSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Bold.ttf");
        ttf_NotoSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
    }

    private void initIndicator() {
        rg_page.removeAllViews();

        LayoutInflater inflate = LayoutInflater.from(getApplicationContext());

        L.e(this, "size : " + mDoctorsArray.size());

        if (mDoctorsArray.size() > 2) {
            for (int i = 0; i < mDoctorsArray.size() / 2 + 1; i++) { // 一页显示两个医生
                RadioButton button = (RadioButton) inflate.inflate(R.layout.indicator_view, null);

                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(40, 30);
                // layoutParams.setMargins(10, 0, 0, 0);

                rg_page.addView(button, layoutParams);
            }
        }
    }

    private void setListeners() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                tv_page.setText((arg0 + 1) + "/" + getTotalPage(totalNum));

                RadioButton button = ((RadioButton) rg_page.getChildAt(arg0));
                if (button != null) {
                    button.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initService() {
        mClientRecive = new ClientReciver();
        registerReceiver(mClientRecive, new IntentFilter("com.mdground.message"));
        // startService(new Intent("com.mdground.screen.service.DataService"));
        startService(new Intent(this, DataService.class));

        xgReceiver = new XGPushReceiver();
        registerReceiver(xgReceiver, new IntentFilter("com.mdground.screen.xgPush"));
    }

    private void initUnisoundTTS() {
//        List a = getMeminfo();
//        String s = "";
//        for (int i = 0; i < a.size(); i++) {
//            s = s + a.toString();
//        }
//        Log.e("StringMEM",s);
        // 初始化语音合成对象
        mTTSPlayer = TTSFactory.createTTSControl(this, MedicalConstant.UNISOUND_APPKEY);
        // 设置音频流
        // mTTSPlayer.setStreamType(AudioManager.STREAM_ALARM);
        // 设置播放语速
        // mTTSPlayer.setVoiceSpeed(0f);
        mTTSPlayer.setVoiceSpeed(0.1f);
        // 设置播报音高
        // mTTSPlayer.setVoicePitch(100f);
        mTTSPlayer.setVoicePitch(0.9f);
        // 设置回调监听
        mTTSPlayer.setTTSListener(this);
        // 初始化合成引擎
        mTTSPlayer.init();
    }


    private void getDoctorList() {
        new GetDoctorInfoListByScreen(getApplicationContext()).getDoctorInfoListByScreen(MedicalApplication.employee.getEmployeeID(), new RequestCallBack() {

            @Override
            public void onSuccess(ResponseData response) {
                L.e(UnisoundMainActivity.this, "获取医生列表 : content : " + response.getContent());

                try {
                    JSONArray doctorArray = new JSONArray(response.getContent());

                    for (int i = 0; i < doctorArray.length(); i++) {
                        JSONObject item = doctorArray.getJSONObject(i);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Doctor doctor = gson.fromJson(item.toString(), Doctor.class);

                        mDoctorsArray.add(doctor);
                        doctorsIndex.put(String.valueOf(doctor.getDoctorID()), i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                mLoadIngDialog.show();
            }

            @Override
            public void onFinish() {
                mLoadIngDialog.dismiss();

                totalNum = mDoctorsArray.size();
                if (mDoctorsArray.size() > 1) {
                    // pageView.setVisibility(View.VISIBLE);
                } else {
                    // pageView.setVisibility(View.INVISIBLE);
                    viewPager.setColumnNum(1);
                }
                tv_page.setText("1/" + getTotalPage(totalNum));
                doctorAdapter = new DoctorAdapter(mDoctorsArray);
                viewPager.setAdapter(doctorAdapter);

                initIndicator();

                startPageSwitch();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < mDoctorsArray.size(); i++) {
                            getAppointmentListByDoctor(i, (int) mDoctorsArray.get(i).getDoctorID());
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mLoadIngDialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getDoctorList();
                    }
                }, 5000);
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getAppointmentListByDoctor(final int index, final int doctorId) {

        new GetAppointmentInfoListByDoctor(getApplicationContext())
                .getAppointmentInfoListByDoctor(Appointment.STATUS_WATTING, doctorId, new RequestCallBack() {

                    @Override
                    public void onSuccess(ResponseData response) {
                        L.e(UnisoundMainActivity.this, "医生(" + index + ") 拿到的预约是 content : " + response.getContent());

                        try {
                            JSONArray jsonArray = new JSONArray(response.getContent());

                            final ArrayList<Appointment> appointmentArray = new ArrayList<Appointment>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                Appointment appointment = gson.fromJson(item.toString(), Appointment.class);

                                if ((appointment.getOPStatus() & Appointment.STATUS_WATTING) != 0
                                        && (appointment.getOPStatus() & Appointment.STATUS_DIAGNOSING) == 0) {
                                    appointmentArray.add(appointment);
                                }

                            }

                            TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(index).findViewById(R.id.gridview));
                            LinearLayout ll_height = ((LinearLayout) registeredViews.get(index).findViewById(R.id.ll_height));


                            gridViewHeight = ll_height.getHeight();
                            gridViewWidth = ll_height.getWidth();

                            gridView.setRowHeight(ll_height.getHeight() / 7);
                            gridView.setAdapter(new NumAdapter(appointmentArray, index
                                    , doctorAdapter.getItemViewType(index) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));

                            // viewPager.notifyDataSetChanged();

                            Collections.sort(appointmentArray);

                            allDoctorAppointmentArray.put(String.valueOf(doctorId), appointmentArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                                          Throwable throwable) {

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                getAppointmentListByDoctor(index, doctorId);
                            }
                        }, 5000);
                    }
                });
    }

    class DoctorAdapter extends BaseAdapter {
        private List<Doctor> list;
        private LayoutInflater inflater;
        public static final int DOCOTOR_ITEM = 0;
        public static final int DOCOTOR_ITEM_SINGLE = 1;

        public DoctorAdapter(List<Doctor> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(UnisoundMainActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"NewApi", "SetTextI18n", "LongLogTag"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            DocotorViewHolder holder = null;
            DocotorSingleViewHolder singleHolder = null;

            Doctor doctorBean = list.get(position);

            //Log.e("doctorBean___",doctorBean.toString());

            if (getItemViewType(position) == DOCOTOR_ITEM) {

                if (null == convertView || null == convertView.getTag(R.layout.item_normal_docotor)) {
                    convertView = inflater.inflate(R.layout.item_normal_docotor, parent, false);
                    holder = new DocotorViewHolder();
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                    holder.tv_current_tips = (TextView) convertView.findViewById(R.id.tv_current_tips);
                    holder.tv_name = (AppCompatTextView) convertView.findViewById(R.id.name_txt);
                    holder.tv_subject = (AppCompatTextView) convertView.findViewById(R.id.tv_subject);
                    holder.tv_opNO = (FlickerTextView) convertView.findViewById(R.id.tv_opNO);
                    holder.ll_height = (LinearLayout) convertView.findViewById(R.id.ll_height);
                    holder.tv_opNO.setTypeface(ttf_NotoSans_Bold);
                    holder.gridView = (TwoWayGridView) convertView.findViewById(R.id.gridview);
                    holder.scrollView = (ScrollView) convertView.findViewById(R.id.scrollview);
                    holder.iv_line = (ImageView) convertView.findViewById(R.id.iv_line);


                    convertView.setTag(R.layout.item_normal_docotor, holder);

                } else {
                    holder = (DocotorViewHolder) convertView.getTag(R.layout.item_normal_docotor);
                }

                if ((position + 1) % PAGE_SIZE == 0) {
                    // holder.iv_line.setVisibility(View.INVISIBLE);
                } else {
                    // holder.iv_line.setVisibility(View.VISIBLE);
                }
                if (isShowPatientOPNum() == 0) {
                    holder.tv_current_tips.setText(R.string.current_num);
                } else {
                    holder.tv_current_tips.setText(R.string.current_patient);
                }

                holder.tv_name.setText(doctorBean.getEmployeeNickName());
                holder.tv_subject.setText(doctorBean.getEMRType());

            } else if (getItemViewType(position) == DOCOTOR_ITEM_SINGLE) {

                if (null == convertView || null == convertView.getTag(R.layout.item_single_big_docotor)) {
                    convertView = inflater.inflate(R.layout.item_single_big_docotor, parent, false);
                    singleHolder = new DocotorSingleViewHolder();
                    singleHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                    singleHolder.tv_current_tips = (TextView) convertView.findViewById(R.id.tv_current_tips);
                    singleHolder.tv_name = (AppCompatTextView) convertView.findViewById(R.id.name_txt);
                    singleHolder.tv_subject = (AppCompatTextView) convertView.findViewById(R.id.tv_subject);
                    singleHolder.tv_opNO = (FlickerTextView) convertView.findViewById(R.id.tv_opNO);
                    singleHolder.ll_height = (LinearLayout) convertView.findViewById(R.id.ll_height);
                    singleHolder.gridView = (TwoWayGridView) convertView.findViewById(R.id.gridview);


                    convertView.setTag(R.layout.item_single_big_docotor, singleHolder);
                } else {
                    singleHolder = (DocotorSingleViewHolder) convertView.getTag(R.layout.item_single_big_docotor);
                }

                singleHolder.tv_name.setText(doctorBean.getEmployeeNickName());
                singleHolder.tv_subject.setText(doctorBean.getEMRType());
            }


            ArrayList<Appointment> appointmentArray = allDoctorAppointmentArray
                    .get(String.valueOf(doctorBean.getDoctorID()));

            if (appointmentArray != null && appointmentArray.size() > 0) {
                if (getItemViewType(position) == DOCOTOR_ITEM) {
                    if (isShowPatientOPNum() == 0) {
                        holder.tv_current_tips.setText(R.string.current_num);
                        holder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getOPNo()));
                    } else if (isShowPatientOPNum() == 1) {
                        holder.tv_current_tips.setText(R.string.current_patient);
                        holder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getPatientName()));
                    } else {
                        holder.tv_current_tips.setText(R.string.current_patient);
                        String numb = String.valueOf(appointmentArray.get(0).getOPNo());
                        String name = String.valueOf(appointmentArray.get(0).getPatientName());
                        holder.tv_opNO.setText(numb + "  " + name);

                    }


                    holder.gridView.setAdapter(new NumAdapter(appointmentArray
                            , position, getItemViewType(position) == DOCOTOR_ITEM_SINGLE));
                    holder.gridView.post(new MyRunnable(holder.scrollView));
                } else {
                    if (isShowPatientOPNum() == 0) {
                        singleHolder.tv_current_tips.setText(R.string.current_num);
                        singleHolder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getOPNo()));
                    } else if (isShowPatientOPNum() == 1) {
                        singleHolder.tv_current_tips.setText(R.string.current_patient);
                        singleHolder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getPatientName()));
                    } else {
                        singleHolder.tv_current_tips.setText(R.string.current_patient);
                        String numb = String.valueOf(appointmentArray.get(0).getOPNo());
                        String name = String.valueOf(appointmentArray.get(0).getPatientName());
                        singleHolder.tv_opNO.setText(numb + "  " + name);
                    }


                    singleHolder.gridView.setAdapter(new NumAdapter(appointmentArray
                            , position, getItemViewType(position) == DOCOTOR_ITEM_SINGLE));
                }
            }


            // 头像显示
            ImageView iv_avatar = null;

            if (getItemViewType(position) == DOCOTOR_ITEM) {
                iv_avatar = holder.iv_avatar;
            } else {
                iv_avatar = singleHolder.iv_avatar;
            }
            // ImageLoader.getInstance().displayImage(
            // "soap://" + String.valueOf(docotorBean.getPhotoID()),
            // iv_avatar);

            long clinicID = doctorBean.getClinicID();
            long photoID = doctorBean.getPhotoID();

            long photoSID = doctorBean.getPhotoSID();

            if (clinicID != 0 && photoID != 0) {
                DisplayImageOptions option = new DisplayImageOptions.Builder().delayBeforeLoading(150)
                        .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(false).cacheOnDisk(false)
                        .considerExifParams(true).build();
                //Log.e("getPhotoURL",doctorBean.getPhotoURL());
                ImageLoader.getInstance().loadImage(doctorBean.getPhotoURL(), option,
                        new AnimateFirstDisplayListener(iv_avatar));
            }

            registeredViews.put(position, convertView);

            return convertView;
        }

        class MyRunnable implements Runnable {
            private ScrollView view;

            public MyRunnable(ScrollView view) {
                this.view = view;
            }

            public void run() {
                view.scrollTo(0, 0);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (list.size() == 1) {
                return DOCOTOR_ITEM_SINGLE;
            } else {
                return DOCOTOR_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        ImageView mView;

        public AnimateFirstDisplayListener(ImageView view) {
            this.mView = view;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            Log.i("HQL", "down: " + imageUri + " ViewTag:" + mView.getTag());
            if (loadedImage != null) {
                mView.setImageBitmap(loadedImage);

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(mView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    class NumAdapter extends BaseAdapter {
        private List<Appointment> list;
        private LayoutInflater inflater;
        boolean isSingle;
        private int countLimit;
        private int index;
        private static final int ITEM_SINGLE = 0, ITEM = 1;

        public NumAdapter(List<Appointment> list, int index, boolean isSingleItem) {
            this.list = list;
            this.index = index;
            this.isSingle = isSingleItem;
            this.inflater = LayoutInflater.from(UnisoundMainActivity.this);
            if (isSingleItem) {
                countLimit = 24;
            } else {
                countLimit = 12;
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"NewApi", "SetTextI18n", "LongLogTag"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NumViewHolder holder = null;
            if (null == convertView || convertView.getTag(R.layout.item_num) == null) {
                convertView = inflater.inflate(R.layout.item_num, parent, false);
                holder = new NumViewHolder();
                holder.tv_num = (AppCompatTextView) convertView.findViewById(R.id.tv_num);
                holder.ll_name = (PercentLinearLayout) convertView.findViewById(R.id.ll_name);
                holder.prl_name = (PercentRelativeLayout) convertView.findViewById(R.id.prl_name);
                holder.tv_num.setTypeface(ttf_NotoSans_Regular);
                holder.iv_yizhida = (ImageView) convertView.findViewById(R.id.iv_yizhida);
                holder.iv_emergency = (ImageView) convertView.findViewById(R.id.iv_emergency);
                holder.iv_wechat_came = (ImageView) convertView.findViewById(R.id.iv_wechat_came);
                convertView.setTag(R.layout.item_num, holder);
            } else {
                holder = (NumViewHolder) convertView.getTag(R.layout.item_num);
            }
            if (position == 0) {
                holder.tv_num.setTextColor(getResources().getColor(R.color.font_blue));
            } else {
                holder.tv_num.setTextColor(getResources().getColor(R.color.normal_color));
            }


            Appointment appointment = list.get(position);
            //Log.e("UnisoundMainActivity:optype:" + appointment.getOPType(), "opstatus:" + appointment.getOPStatus());

            if (appointment.getOPType() == ONLINE) {
                holder.iv_emergency.setVisibility(View.GONE);

                if ((appointment.getOPStatus()&Appointment.STATUS_CAME)==Appointment.STATUS_CAME) {
                    holder.iv_wechat_came.setVisibility(View.VISIBLE);
                    holder.iv_yizhida.setVisibility(View.GONE);

                } else {
                    holder.iv_yizhida.setVisibility(View.VISIBLE);
                    holder.iv_wechat_came.setVisibility(View.GONE);
                }
            } else {
                holder.iv_yizhida.setVisibility(View.GONE);
                holder.iv_emergency.setVisibility(View.GONE);
                holder.iv_wechat_came.setVisibility(View.GONE);
            }

            if (appointment.isEmergency()) {

                holder.iv_yizhida.setVisibility(View.GONE);
                holder.iv_wechat_came.setVisibility(View.GONE);
                holder.iv_emergency.setVisibility(View.VISIBLE);
            } else {
                holder.iv_emergency.setVisibility(View.GONE);
            }

            TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(index).findViewById(R.id.gridview));
            PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) holder.ll_name.getLayoutParams();
            layoutParams.height = gridViewHeight / 7;

//            if (position < countLimit) {

                if (isShowPatientOPNum() == 0) {//number



                    if (isSingle) {
                        layoutParams.width = gridViewWidth / 6;
                        gridView.setNumColumns(6);

                    }else {
                        layoutParams.width = gridViewWidth / 3;
                        gridView.setNumColumns(3);
                    }


                    String pattern = "0000";
                    java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
                    holder.tv_num.setText(df.format(appointment.getOPNo()));

                } else if (isShowPatientOPNum() == 1) {//name


                    if (isSingle) {
                        layoutParams.width = gridViewWidth / 6;
                        gridView.setNumColumns(6);

                    }else {
                        layoutParams.width = gridViewWidth / 3;
                        gridView.setNumColumns(3);
                    }

                    holder.tv_num.setText(appointment.getPatientName());
                } else {//number&name



                    if (isSingle) {
                        layoutParams.width = gridViewWidth / 4;
                        gridView.setNumColumns(4);

                    }else {
                        layoutParams.width = gridViewWidth / 2;
                        gridView.setNumColumns(2);
                    }



                    holder.tv_num.setText(appointment.getOPNo() + "  " + appointment.getPatientName());

                }

                convertView.setVisibility(View.VISIBLE);
//            } else {
//                holder.tv_num.setText("......");
//                convertView.setVisibility(View.INVISIBLE);
//            }
            holder.tv_num.setSelected(true);

            holder.ll_name.setLayoutParams(layoutParams);
            gridView.setColumnWidth(layoutParams.width);


            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (isSingle) {
                return ITEM_SINGLE;
            } else {
                return ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    private void startCallPatient(int doctorID, int opNO, String patientName) {
        Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

        if (viewPagerIndex != null) {
            String showString = null;
            String pattern = "0000";
            java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
            if (isShowPatientOPNum() == 0) {
                showString = df.format(opNO);
            } else if (isShowPatientOPNum() == 1) {
                showString = patientName;
            } else {
                showString = df.format(opNO) + " " + patientName;
            }
            ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO)).startFlicker(showString);

            ArrayList<Appointment> appointments = new ArrayList<Appointment>();

            ArrayList<Appointment> list = allDoctorAppointmentArray.get(String.valueOf(doctorID));

            if (list != null) {
                appointments.addAll(list);

                for (int i = 0; i < appointments.size(); i++) {
                    Appointment item = appointments.get(i);
                    if (item.getOPNo() == opNO) {
                        appointments.remove(i);
                        break;
                    }
                }
                TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview));
                gridView.setAdapter(new NumAdapter(appointments, viewPagerIndex,
                        doctorAdapter.getItemViewType(viewPagerIndex) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));
            }

        }
    }

    private void stopCallPatient(int doctorID, int opNO) {
        Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

        if (viewPagerIndex != null) {
            ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO)).stopFlicker();
        }
    }

    private boolean isOpNoTextViewVisible(int doctorID) {
        Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

        if (viewPagerIndex != null) {
            return ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO)).getVisibility() == View.VISIBLE;
        }
        return false;
    }

    private void showStartOpNO(int doctorID, int opNO) {
        Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

        if (viewPagerIndex != null) {

            FlickerTextView textView = ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO));
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(opNO));
            textView.stopFlicker();

            // 开始或者结束后,在本地删掉该预约

            ArrayList<Appointment> appointments = allDoctorAppointmentArray.get(String.valueOf(doctorID));

            if (appointments != null) {
                for (int i = 0; i < appointments.size(); i++) {
                    Appointment item = appointments.get(i);
                    if (item.getOPNo() == opNO) {
                        appointments.remove(i);
                        break;
                    }
                }
                TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview));
                gridView.setAdapter(new NumAdapter(appointments, viewPagerIndex,
                        doctorAdapter.getItemViewType(viewPagerIndex) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));
            }
        }
    }

    private void hideOpNO(int doctorID, int opNO) {
        Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

        if (viewPagerIndex != null) {
            ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO)).setVisibility(View.INVISIBLE);

            // 开始或者结束后,在本地删掉该预约
            ArrayList<Appointment> appointments = allDoctorAppointmentArray.get(String.valueOf(doctorID));

            if (appointments != null) {
                for (int i = 0; i < appointments.size(); i++) {
                    Appointment item = appointments.get(i);
                    if (item.getOPNo() == opNO) {
                        appointments.remove(i);
                        break;
                    }
                }
                TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview));
                gridView.setAdapter(new NumAdapter(appointments, viewPagerIndex,
                        doctorAdapter.getItemViewType(viewPagerIndex) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));
            }

        }
    }

    private int isShowPatientOPNum() {
        String type = ((MedicalApplication) getApplication()).getmClinic().getNavigationType();
        //Log.e("ClinicType", type);
        if (type.equals("Number")) {
            return 0;
        } else if (type.equals("Name")) {
            return 1;
        } else {//NumberName
            return 2;
        }

    }

    static class DocotorViewHolder {
        ScrollView scrollView;
        ImageView iv_avatar;
        TextView tv_current_tips;
        AppCompatTextView     tv_subject;
        AppCompatTextView tv_name;
        FlickerTextView tv_opNO;
        LinearLayout ll_height;

        TwoWayGridView gridView;
        ImageView iv_line;
    }

    static class DocotorSingleViewHolder {
        ImageView iv_avatar;
        TextView tv_current_tips;
                AppCompatTextView tv_subject;
        AppCompatTextView tv_name;
        FlickerTextView tv_opNO;
        LinearLayout ll_height;
        TwoWayGridView gridView;
    }

    static class NumViewHolder {
        PercentLinearLayout ll_name;
        AppCompatTextView tv_num;
        ImageView iv_yizhida, iv_emergency, iv_wechat_came;
        PercentRelativeLayout prl_name;
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_PAGE:
                    viewPager.setCurrentItem(currentPage - 1, false);
                    break;
            }
        }

        ;
    };

    private void startPageSwitch() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (currentPage >= getTotalPage(totalNum)) {
                    currentPage = 1;
                } else {
                    currentPage++;
                }
                handler.obtainMessage(CHANGE_PAGE).sendToTarget();
            }
        };

        timer.schedule(task, 10000, 10000);
    }

    // private void stopPageSwitch() {
    // if (timer != null) {
    // timer.cancel();
    // timer = null;
    // }
    // }

    private int getTotalPage(int num) {
        return num % PAGE_SIZE == 0 ? num / PAGE_SIZE : num / PAGE_SIZE + 1;
    }

    private void speech(String message) {

        L.e(this, "speech()方法执行了");

        if (message == null) {
            return;
        }
        currentSpeechMessage = message;
        try {

            JSONObject json = new JSONObject(message);

            int opNO = json.getInt("OPNo");
            String doctorName = json.getString("DoctorName");
            int doctorID = json.getInt("DoctorID");
            String patientName = json.getString("PatientName");

            startCallPatient(doctorID, opNO, patientName);

            // 切换到该医生的界面
            // Integer pageIndex = doctorsIndex.get(String.valueOf(doctorID));
            // if (pageIndex != null) {
            // viewPager.setCurrentItem(pageIndex / 3, false);
            // stopPageSwitch();
            // }

            String speechString = null;

            String callPatientString = patientName;   // 默认叫病人的名字
            if (isShowPatientOPNum() == 0) {
                callPatientString = String.valueOf(opNO + "号");  // 叫病人的预约号码
            } else if (isShowPatientOPNum() == 1) {
                callPatientString = patientName;  // 叫病人名字
            } else {
                callPatientString = opNO + "号" + patientName;  // 叫预约号和病人名字
            }
            speechString = "请" + callPatientString + "到" + doctorName + "处就诊";
//			if (doctorName.endsWith("医生")) {
//				speechString = "请" + callPatientString + "到" + doctorName + "处就诊";
//			} else {
//				speechString = "请" + callPatientString + "到" + doctorName + "医生处就诊";
//			}

            speechString = speechString.replace("曾", "增"); // 预防"曾"读成ceng
            speechString = speechString.replace("锜", "奇");

            // 播放语音
            mTTSPlayer.play(speechString);

            String showPatientString = null;
            String pattern = "0000";
            java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
            String showString = df.format(opNO);
            // 显示多个医生的时候
            if (isShowPatientOPNum() == 0) {
                showPatientString = String.valueOf(showString + "  ");  // 叫病人的预约号码
            } else if (isShowPatientOPNum() == 1) {
                showPatientString = patientName + "  ";
            } else {
                showPatientString = df.format(opNO) + "号 " + patientName + "  ";
            }

            if (mDoctorsArray.size() > 0) {
                String showString1 = "";
                if (isShowPatientOPNum() == 0) {
                    showString1 = "请  " + showPatientString + "号到" + doctorName + "处就诊";
                } else if (isShowPatientOPNum() == 1) {
                    showString1 = "请  " + showPatientString + "到" + doctorName + "处就诊";
                } else {
                    showString1 = "请  " + showPatientString + "到" + doctorName + "处就诊";
                }
                // 在顶部高亮
                SpannableString ss = new SpannableString(showString1);
                ss.setSpan(new CustomTypefaceSpan("", ttf_NotoSans_Bold), 3, 3 + showPatientString.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                ss.setSpan(new RelativeSizeSpan(1.44f), 3, 3 + showPatientString.length(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tv_highlight_num.setText(ss);

                tv_highlight_num.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class CallAppointment {
        private int opNO;
        private int doctorID;
        private String doctorName;

        public CallAppointment(String jsonString) {
            try {
                JSONObject json = new JSONObject(jsonString);

                this.opNO = json.getInt("OPNo");
                this.doctorName = json.getString("DoctorName");
                this.doctorID = json.getInt("DoctorID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int getOpNO() {
            return opNO;
        }

        public void setOpNO(int opNO) {
            this.opNO = opNO;
        }

        public int getDoctorID() {
            return doctorID;
        }

        public void setDoctorID(int doctorID) {
            this.doctorID = doctorID;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
//        mServer.shutdown();
        if (mHttpServer != null) {
            mHttpServer.stop();
        }
        // stopPageSwitch();
        unregisterReceiver(mClientRecive);
        unregisterReceiver(xgReceiver);
    }

    @Override
    public void onBuffer() {

    }

    @Override
    public void onCancel() {
        L.e(this, "云知声语音cancel()");
        isPlaying = false;
        speechQueue.clear();
        currentSpeechCount = 0;
        currentSpeechMessage = null;
    }

    @Override
    public void onError(USCError arg0) {
        L.e(this, "云知声语音出错了 onError(SpeechSynthesizer arg0, SpeechError arg1)");
        isPlaying = false;
        speechQueue.clear();
        currentSpeechCount = 0;
        currentSpeechMessage = null;

        initUnisoundTTS();
    }

    @Override
    public void onInitFinish() {
        L.e(this, "云知声初始化成功");
    }

    @Override
    public void onPlayBegin() {
        L.e(UnisoundMainActivity.this, "朗读开始");
        isPlaying = true;
        currentSpeechCount++;
    }

    @Override
    public void onPlayEnd() {
        L.e(UnisoundMainActivity.this, "朗读结束");

        isPlaying = false;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (currentSpeechCount == 2) {
                    JSONObject json;
                    try {
                        json = new JSONObject(currentSpeechMessage);
                        int opNO = json.getInt("OPNo");
                        int doctorID = json.getInt("DoctorID");
                        stopCallPatient(doctorID, opNO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    currentSpeechCount = 0;
                    currentSpeechMessage = null;

                    String message = speechQueue.poll();

                    if (message != null) {
                        currentSpeechMessage = message;
                        speech(message);
                    } else {
                        // startPageSwitch();
                        tv_highlight_num.setVisibility(View.INVISIBLE);
                    }
                } else {
                    speech(currentSpeechMessage);
                }
            }
        }, 2000);
    }
}


package com.mdground.screen.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.ResponseData;
import org.mdground.api.bean.Appointment;
import org.mdground.api.bean.Doctor;
import org.mdground.api.server.clinic.GetAppointmentInfoListByDoctor;
import org.mdground.api.server.global.GetDoctorList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.mdground.screen.R;
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
import com.tencent.android.tpush.XGPushManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class IflyTetTTsMainActivity extends BaseActivity {

	private GridViewPager viewPager;
	private DoctorAdapter doctorAdapter;
	private TextView tv_page;
	private int totalNum;
	private int currentPage = 1;
	private static final int PAGE_SIZE = 2;
	private Timer timer;
	private static final int CHANGE_PAGE = 0x01;
	private View pageView;
	private ArrayList<Doctor> doctorsArray = new ArrayList<Doctor>();
	private HashMap<String, Integer> doctorsIndex = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Appointment>> allDoctorAppointmentArray = new HashMap<String, ArrayList<Appointment>>();

	private SpeechSynthesizer speechSynthesizer;
	/** 指定license路径，需要保证该路径的可读写权限 */
	private static final String LICENCE_FILE_NAME = Environment.getExternalStorageDirectory()
			+ "/tts/baidu_tts_licence.dat";

	private Queue<String> speechQueue = new LinkedList<String>();

	private int currentSpeechCount = 0;
	private String currentSpeechMessage;

	private ClientReciver mClientRecive;

	private XGPushReceiver xgReceiver;

	private SparseArray<View> registeredViews = new SparseArray<View>();

	private LoadingDialog mLoadIngDialog;

	/**
	 * 预约是来自wechat的
	 */
	public static final int ONLINE = 1;

	// 科大讯飞

	// 语音合成对象
	private SpeechSynthesizer mTts;

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

	class ClientReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.mdground.message".equals(intent.getAction())) {
				String message = intent.getStringExtra("message");

				// Toast.makeText(MainActivity.this,
				// intent.getStringExtra("message"), Toast.LENGTH_SHORT)
				// .show();

				// L.e(MainActivity.this, "app发过来的socket信息是 : " + message);

				JSONObject json;

				try {
					json = new JSONObject(message);

					int opNO = json.getInt("OPNo");
					int doctorID = json.getInt("DoctorID");

					int action = json.getInt("Action");

					if (action == 1) { // 叫号
						// 如果当前发过来的opNO是正在播放的,则不添加进队列里面

						// 当正在播放林医生的叫号时,这时收到黄医生的叫号,则继续播放林医生的叫号,不过要在导诊屏上的黄医生的位置显示黄医生的叫号
						if (!isOpNoTextViewVisible(doctorID)) {
							startCallPatient(doctorID, opNO);
						}

						// 当正在播放4001号的叫号时,在播放两次声音的时间之内,又收到了一次同样是4001号的叫号,则只播放两次,而不是播放四次4001的叫号
						if (currentSpeechMessage != null) {
							CallAppointment currentCall = new CallAppointment(currentSpeechMessage);
							if (currentCall.getOpNO() == opNO) {
								return;
							}
						}

						if (speechQueue.size() == 0 && !speechSynthesizer.getSynthesizer().isSpeaking()
								&& currentSpeechCount == 0) {
							speech(message);
						} else {

							for (String callMessage : speechQueue) {
								if (opNO == new JSONObject(callMessage).getInt("OPNo")) {
									return;
								}
							}

							speechQueue.offer(message);
						}
					} else if (action == 2) { // 状态上报

						int OPStatus = json.getInt("OPStatus");

						if ((OPStatus & Appointment.STATUS_DIAGNOSING) != 0) {
							showStartOpNO(doctorID, opNO); // 开始

							// 重新拉一次数据

							getAppointmentListByDoctor(doctorsIndex.get(String.valueOf(doctorID)), doctorID);
							// for (int i = 0; i < doctorsArray.size(); i++) {
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
				// L.e(MainActivity.this, "拿到的title : " + title);
				// L.e(MainActivity.this, "拿到的content : " + content);
				// L.e(MainActivity.this, "拿到的customContent : " +
				// customContent);

				// L.e(MainActivity.this, "收到推送");

				try {
					JSONObject json = new JSONObject(customContent);
					String functionName = json.getString("FunctionName");

					// 更新列表的推送
					if ("RefreshAppointment".equals(functionName)) {
						// Toast.makeText(getApplicationContext(),
						// "收到重新更新列表的推送",
						// Toast.LENGTH_SHORT).show();

						int doctorId = Integer.valueOf(content);

						Integer index = doctorsIndex.get(String.valueOf(doctorId));

						if (index != null) {
							getAppointmentListByDoctor(index, doctorId);
						}

						// for (int i = 0; i < doctorsArray.size(); i++) {
						// ((FlickerTextView) registeredViews.get(i)
						// .findViewById(R.id.current_num))
						// .setVisibility(View.INVISIBLE);
						//
						// getAppointmentListByDoctor(i, (int) doctorsArray
						// .get(i).getDoctorID());
						// }
					} else if ("UpgradeScreenVersion".equals(functionName)) { // 更新
						UpdateManager manager = new UpdateManager(IflyTetTTsMainActivity.this);
						manager.showDownloadDialog();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		
		// 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
		// 设置你申请的应用appid
		SpeechUtility.createUtility(IflyTetTTsMainActivity.this, SpeechConstant.APPID +"=54859360");

		super.onCreate(arg0);
		XGPushManager.registerPush(getApplicationContext());
		setContentView(R.layout.activity_main);
		findViewById();
		setListeners();
		initService();
		initIflyTekTTS();
		getDoctorList();

		mLoadIngDialog = new LoadingDialog(this).initText(getResources().getString(R.string.logining));

		// new Handler().postDelayed(new Runnable() {
		// public void run() {
		// UpdateManager manager = new UpdateManager(MainActivity.this);
		// manager.showDownloadDialog();
		// }
		// }, 10000);

		// new Handler().postDelayed(new Runnable() {
		// public void run() {
		// int[] i = new int[2];
		// i[3] = 0;
		// }
		// }, 20000);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// CrashManager.register(this, "503880ea15f946c5a47042feda3e6517", new
		// MyCrashManagerListener());
	}

	// private static class MyCrashManagerListener extends CrashManagerListener
	// {
	//
	// public boolean shouldAutoUploadCrashes() {
	// return true;
	// }
	//
	// public String getDescription() {
	// String description = "";
	//
	// try {
	// Process process = Runtime.getRuntime().exec("logcat -d *:E");
	// BufferedReader bufferedReader = new BufferedReader(new
	// InputStreamReader(process.getInputStream()));
	//
	// StringBuilder log = new StringBuilder();
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// log.append(line);
	// log.append(System.getProperty("line.separator"));
	// }
	// bufferedReader.close();
	//
	// description = log.toString();
	// } catch (IOException e) {
	// }
	//
	// return description;
	// }
	// }

	private void findViewById() {
		viewPager = (GridViewPager) findViewById(R.id.gvp);
		tv_page = (TextView) findViewById(R.id.page);
		pageView = findViewById(R.id.page_view);
	}

	private void setListeners() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				tv_page.setText((arg0 + 1) + "/" + getTotalPage(totalNum));
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

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(IflyTetTTsMainActivity.this, "初始化失败,错误码：" + code, Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initIflyTekTTS() {
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

		// 设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		// 设置发音人 voicer为空默认通过语音+界面指定发音人。
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		mTts.setParameter(SpeechConstant.SPEED, "30");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "1");// 0则为听筒
		int code = mTts.startSpeaking("请1024号到林丹湧医生处就诊", mTtsListener);
		Log.e("bao", "读文本返回的code : " + code);
		if (code != ErrorCode.SUCCESS) {
			Toast.makeText(this, "未安装离线包", Toast.LENGTH_LONG).show();
		}
	}

	private void getDoctorList() {
		new GetDoctorList(getApplicationContext()).getDoctorList(new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				L.e(IflyTetTTsMainActivity.this, "获取医生列表 : content : " + response.getContent());

				try {
					JSONArray doctorArray = new JSONArray(response.getContent());

					for (int i = 0; i < doctorArray.length(); i++) {
						JSONObject item = doctorArray.getJSONObject(i);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
						Doctor doctor = gson.fromJson(item.toString(), Doctor.class);

						doctorsArray.add(doctor);
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

				totalNum = doctorsArray.size();
				if (doctorsArray.size() > 1) {
					// pageView.setVisibility(View.VISIBLE);
				} else {
					// pageView.setVisibility(View.INVISIBLE);
					viewPager.setColumnNum(1);
				}
				tv_page.setText("1/" + getTotalPage(totalNum));
				doctorAdapter = new DoctorAdapter(doctorsArray);
				viewPager.setAdapter(doctorAdapter);

				startPageSwitch();

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; i < doctorsArray.size(); i++) {
							getAppointmentListByDoctor(i, (int) doctorsArray.get(i).getDoctorID());
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

	private void getAppointmentListByDoctor(final int index, final int doctorId) {

		new GetAppointmentInfoListByDoctor(getApplicationContext())
				.getAppointmentInfoListByDoctor(Appointment.STATUS_WATTING, doctorId, new RequestCallBack() {

					@Override
					public void onSuccess(ResponseData response) {
						L.e(IflyTetTTsMainActivity.this, "医生(" + index + ") 拿到的预约是 content : " + response.getContent());

						try {
							JSONArray jsonArray = new JSONArray(response.getContent());

							final ArrayList<Appointment> appointmentArray = new ArrayList<Appointment>();

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject item = jsonArray.getJSONObject(i);
								Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
								Appointment appointment = gson.fromJson(item.toString(), Appointment.class);

								// L.e(MainActivity.this,
								// "appointment.getOPStatus() : " +
								// appointment.getOPStatus());

								if ((appointment.getOPStatus() & Appointment.STATUS_WATTING) != 0
										&& (appointment.getOPStatus() & Appointment.STATUS_DIAGNOSING) == 0) {
									appointmentArray.add(appointment);
								}

							}

							TwoWayGridView gridView = ((TwoWayGridView) registeredViews.get(index)
									.findViewById(R.id.gridview));

							gridView.setAdapter(new NumAdapter(appointmentArray,
									doctorAdapter.getItemViewType(index) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));

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
			this.inflater = LayoutInflater.from(IflyTetTTsMainActivity.this);
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

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			DocotorViewHolder holder = null;
			DocotorSingleViewHolder singleHolder = null;

			Doctor doctorBean = list.get(position);

			if (getItemViewType(position) == DOCOTOR_ITEM) {

				if (null == convertView || null == convertView.getTag(R.layout.item_normal_docotor)) {
					convertView = inflater.inflate(R.layout.item_normal_docotor, null);
					holder = new DocotorViewHolder();
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
					holder.tv_name = (TextView) convertView.findViewById(R.id.name_txt);
					holder.tv_opNO = (FlickerTextView) convertView.findViewById(R.id.tv_opNO);
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
				holder.tv_name.setText(doctorBean.getEmployeeName());

			} else if (getItemViewType(position) == DOCOTOR_ITEM_SINGLE) {

				if (null == convertView || null == convertView.getTag(R.layout.item_single_big_docotor)) {
					convertView = inflater.inflate(R.layout.item_single_big_docotor, null);
					singleHolder = new DocotorSingleViewHolder();
					singleHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
					singleHolder.tv_name = (TextView) convertView.findViewById(R.id.name_txt);
					singleHolder.tv_opNO = (FlickerTextView) convertView.findViewById(R.id.tv_opNO);
					singleHolder.gridView = (TwoWayGridView) convertView.findViewById(R.id.gridview);
					convertView.setTag(R.layout.item_single_big_docotor, singleHolder);
				} else {
					singleHolder = (DocotorSingleViewHolder) convertView.getTag(R.layout.item_single_big_docotor);
				}

				singleHolder.tv_name.setText(doctorBean.getEmployeeName());
			}

			ArrayList<Appointment> appointmentArray = allDoctorAppointmentArray
					.get(String.valueOf(doctorBean.getDoctorID()));

			if (appointmentArray != null && appointmentArray.size() > 0) {
				if (getItemViewType(position) == DOCOTOR_ITEM) {
					holder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getOPNo()));
					holder.gridView.setAdapter(
							new NumAdapter(appointmentArray, getItemViewType(position) == DOCOTOR_ITEM_SINGLE));
					holder.gridView.post(new MyRunnable(holder.scrollView));
				} else {
					singleHolder.tv_opNO.setText(String.valueOf(appointmentArray.get(0).getOPNo()));
					singleHolder.gridView.setAdapter(
							new NumAdapter(appointmentArray, getItemViewType(position) == DOCOTOR_ITEM_SINGLE));
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
						.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true).build();

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
			// if (list.size() == 1) {
			// return DOCOTOR_ITEM_SINGLE;
			// } else {
			// return DOCOTOR_ITEM;
			// }
			return DOCOTOR_ITEM;
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
		private static final int ITEM_SINGLE = 0, ITEM = 1;

		public NumAdapter(List<Appointment> list, boolean isSingleItem) {
			this.list = list;
			this.isSingle = isSingleItem;
			this.inflater = LayoutInflater.from(IflyTetTTsMainActivity.this);
			if (isSingleItem) {
				countLimit = 11;
			} else {
				countLimit = 11;
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

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NumViewHolder holder = null;
			if (isSingle) {
				if (null == convertView || convertView.getTag(R.layout.item_num) == null) {
					convertView = inflater.inflate(R.layout.item_num, null);
					holder = new NumViewHolder();
					holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
					convertView.setTag(R.layout.item_num, holder);
				} else {
					holder = (NumViewHolder) convertView.getTag(R.layout.item_num);
				}
			} else {
				if (null == convertView || convertView.getTag(R.layout.item_num) == null) {
					convertView = inflater.inflate(R.layout.item_num, null);
					holder = new NumViewHolder();
					holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
					holder.iv_yizhida = (ImageView) convertView.findViewById(R.id.iv_yizhida);
					holder.iv_emergency = (ImageView) convertView.findViewById(R.id.iv_emergency);
					convertView.setTag(R.layout.item_num, holder);
				} else {
					holder = (NumViewHolder) convertView.getTag(R.layout.item_num);
				}
			}
			if (position == 0) {
				// holder.tv_num.setTextColor(getResources().getColor(R.color.font_dark));
			} else {
				// holder.tv_num.setTextColor(getResources().getColor(R.color.font));
			}

			Appointment appointment = list.get(position);

			if (appointment.getOPType() == ONLINE) {
				holder.iv_yizhida.setVisibility(View.VISIBLE);
				holder.iv_emergency.setVisibility(View.GONE);
			} else {
				holder.iv_yizhida.setVisibility(View.GONE);
			}
			
			if (appointment.isEmergency()) {
				holder.iv_yizhida.setVisibility(View.GONE);
				holder.iv_emergency.setVisibility(View.VISIBLE);
			} else {
				holder.iv_emergency.setVisibility(View.GONE);
			}
			
			if (position < countLimit) {
				holder.tv_num.setText(String.valueOf(appointment.getOPNo()));
			} else {
				holder.tv_num.setText("......");
			}
			// if (isSingle) {
			// holder.numTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
			// getResources().getDimension(R.dimen.size_23));
			// } else {
			// holder.numTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
			// getResources().getDimension(R.dimen.size_17));
			// }
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

	private void startCallPatient(int doctorID, int opNO) {
		Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

		if (viewPagerIndex != null) {
			((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO))
					.startFlicker(String.valueOf(opNO));

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
				((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview))
						.setAdapter(new NumAdapter(appointments,
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
			return ((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO))
					.getVisibility() == View.VISIBLE;
		}
		return false;
	}

	private void showStartOpNO(int doctorID, int opNO) {
		Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

		if (viewPagerIndex != null) {

			FlickerTextView textView = ((FlickerTextView) registeredViews.get(viewPagerIndex)
					.findViewById(R.id.tv_opNO));

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
				((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview))
						.setAdapter(new NumAdapter(appointments,
								doctorAdapter.getItemViewType(viewPagerIndex) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));
			}
		}
	}

	private void hideOpNO(int doctorID, int opNO) {
		Integer viewPagerIndex = doctorsIndex.get(String.valueOf(doctorID));

		if (viewPagerIndex != null) {
			((FlickerTextView) registeredViews.get(viewPagerIndex).findViewById(R.id.tv_opNO))
					.setVisibility(View.INVISIBLE);

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
				((TwoWayGridView) registeredViews.get(viewPagerIndex).findViewById(R.id.gridview))
						.setAdapter(new NumAdapter(appointments,
								doctorAdapter.getItemViewType(viewPagerIndex) == DoctorAdapter.DOCOTOR_ITEM_SINGLE));
			}

		}
	}

	static class DocotorViewHolder {
		ScrollView scrollView;
		ImageView iv_avatar;
		TextView tv_name;
		FlickerTextView tv_opNO;
		TwoWayGridView gridView;
		ImageView iv_line;
	}

	static class DocotorSingleViewHolder {
		ImageView iv_avatar;
		TextView tv_name;
		FlickerTextView tv_opNO;
		TwoWayGridView gridView;
	}

	static class NumViewHolder {
		TextView tv_num;
		ImageView iv_yizhida, iv_emergency;
	}

	final Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_PAGE:
				viewPager.setCurrentItem(currentPage - 1, false);
				break;
			}
		};
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

	private void stopPageSwitch() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private int getTotalPage(int num) {
		return num % PAGE_SIZE == 0 ? num / PAGE_SIZE : num / PAGE_SIZE + 1;
	}

	private void speech(String message) {
		currentSpeechMessage = message;
		try {

			JSONObject json = new JSONObject(message);

			int opNO = json.getInt("OPNo");
			String doctorName = json.getString("DoctorName");
			int doctorID = json.getInt("DoctorID");

			startCallPatient(doctorID, opNO);

			// 切换到该医生的界面
			Integer pageIndex = doctorsIndex.get(String.valueOf(doctorID));
			if (pageIndex != null) {
				viewPager.setCurrentItem(pageIndex / 3, false);
				stopPageSwitch();
			}
			if (doctorName.endsWith("医生")) {
				int code = mTts.startSpeaking("请" + opNO + "号到" + doctorName + "处就诊", mTtsListener);
			} else {
				int code = mTts.startSpeaking("请" + opNO + "号到" + doctorName + "医生处就诊", mTtsListener);
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
		stopPageSwitch();
		unregisterReceiver(mClientRecive);
		unregisterReceiver(xgReceiver);
	}

	// @Override
	// public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
	//
	// }
	//
	// @Override
	// public void onCancel(SpeechSynthesizer arg0) {
	// speechQueue.clear();
	// currentSpeechCount = 0;
	// currentSpeechMessage = null;
	// }
	//
	// @Override
	// public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
	// L.e(this, "onError(SpeechSynthesizer arg0, SpeechError arg1)");
	// speechQueue.clear();
	// currentSpeechCount = 0;
	// currentSpeechMessage = null;
	//
	// initBaiduTTS();
	// }

	// @Override
	// public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1, boolean
	// arg2) {
	//
	// }
	//
	// @Override
	// public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {
	// L.e(IflyTetTTsMainActivity.this, "朗读结束");
	//
	// new Handler().postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// if (currentSpeechCount == 2) {
	// JSONObject json;
	// try {
	// json = new JSONObject(currentSpeechMessage);
	// int opNO = json.getInt("OPNo");
	// int doctorID = json.getInt("DoctorID");
	// stopCallPatient(doctorID, opNO);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// currentSpeechCount = 0;
	// currentSpeechMessage = null;
	//
	// String message = speechQueue.poll();
	//
	// if (message != null) {
	// currentSpeechMessage = message;
	// speech(message);
	// } else {
	// startPageSwitch();
	// }
	// } else {
	// speech(currentSpeechMessage);
	// }
	// }
	// }, 2000);
	// }
	//
	// @Override
	// public void onSpeechPause(SpeechSynthesizer arg0) {
	//
	// }
	//
	// @Override
	// public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
	//
	// }
	//
	// @Override
	// public void onSpeechResume(SpeechSynthesizer arg0) {
	//
	// }
	//
	// @Override
	// public void onSpeechStart(SpeechSynthesizer arg0) {
	// L.e(IflyTetTTsMainActivity.this, "朗读开始");
	// currentSpeechCount++;
	// }
	//
	// @Override
	// public void onStartWorking(SpeechSynthesizer arg0) {
	// L.e(IflyTetTTsMainActivity.this, "开始工作，请等待数据...");
	// }
	//
	// @Override
	// public void onSynthesizeFinish(SpeechSynthesizer arg0) {
	// L.e(IflyTetTTsMainActivity.this, "合成已完成");
	// };

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			L.e(IflyTetTTsMainActivity.this, "朗读结束");

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
							startPageSwitch();
						}
					} else {
						speech(currentSpeechMessage);
					}
				}
			}, 2000);
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

		}

		@Override
		public void onSpeakBegin() {
			L.e(IflyTetTTsMainActivity.this, "朗读开始");
			currentSpeechCount++;
		}

		@Override
		public void onSpeakPaused() {

		}

		@Override
		public void onSpeakResumed() {

		}
	};
}

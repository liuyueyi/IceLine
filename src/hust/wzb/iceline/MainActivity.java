package hust.wzb.iceline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobInterstitialAd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class MainActivity extends AndroidApplication {
	public static final String PUBLISHER_ID = "56OJwoVIuNBRIhuEJw";
	public static final String InlinePPID = "16TLuUEoApj9kNUE9t0XlAsi";
	public static final String chaPinID = "16TLuUEoApj9kNU0YyeTTcts"; // ����

	static DomobAdView MyAdview320x50;
	static DomobInterstitialAd mInterstitialAd; // �������

	static RelativeLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		View gameView = initializeForView(new MyGdxGame(this), cfg);

		layout = new RelativeLayout(this);
		// ���ô����ޱ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ȥ��ǿ����Ļװ�Σ���״̬������������
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		MyAdview320x50 = new DomobAdView(this, PUBLISHER_ID, InlinePPID);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		layout.addView(gameView);
		layout.addView(MyAdview320x50, adParams);

		mInterstitialAd = new DomobInterstitialAd(this, PUBLISHER_ID, chaPinID,
				DomobInterstitialAd.INTERSITIAL_SIZE_300X250);
		mInterstitialAd.loadInterstitialAd(); // ����

		setContentView(layout);

		mContext = this;

	}


	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.CHAPIN:
				// ��ʾ�������
				// �жϲ�������Ƿ��ѳ�ʼ����ɣ�����ȷ���Ƿ��ܳɹ����ò������
				if (mInterstitialAd.isInterstitialAdReady()) {
					mInterstitialAd.showInterstitialAd(mContext);
					Log.i("chapin", "��ʾ");
				} else {
					mInterstitialAd.loadInterstitialAd(); // �ټ���һ��
					Log.i("chapin", "����ʾ");
				}
				break;
			case Constants.MORE:
				// չʾ�����޻����Ƽ�ǽ
				break;
			case Constants.EXIT: // �˳�
				// �����˳��Ի���
				AlertDialog isExit = new AlertDialog.Builder(mContext).create();
				// ���öԻ������
				isExit.setTitle("ϵͳ��ʾ");
				// ���öԻ�����Ϣ
				isExit.setMessage("ȷ��Ҫ�˳���");
				// ����ѡ��ť��ע�����
				isExit.setButton("ȷ��", listener);
				isExit.setButton2("ȡ��", listener);
				// ��ʾ�Ի���
				isExit.show();
				break;
			}

		}
	};

	private static Context mContext;

	/** �����Ի��������button����¼� */
	static DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				Gdx.app.exit();
				((Activity) mContext).finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

	}

	public void showAdStatic(int adTag) {
		// TODO Auto-generated method stub
		Message msg = handler.obtainMessage();
		msg.what = adTag; // ˽�о�̬�����ͱ����������������ж���ֵ
		handler.sendMessage(msg);
	}
}
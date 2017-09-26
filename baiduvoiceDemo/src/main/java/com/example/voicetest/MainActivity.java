package com.example.voicetest;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;

public class MainActivity extends Activity implements SpeechSynthesizerListener {

	private SpeechSynthesizer speechSynthesizer;
	private EditText inputTextView;
	private Button startButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initVoice();

	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		inputTextView = (EditText) findViewById(R.id.inputTextView);
		startButton = (Button) findViewById(R.id.start);
		startButton.setOnClickListener(startClickListener);

	}

	private void initVoice() {
		speechSynthesizer = new SpeechSynthesizer(MainActivity.this, "holder",
				this);
		// 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
		speechSynthesizer.setApiKey("pYTVxRgD7z5l1v6nueqye6l9",
				"51938263457a4e7f1797eb24f6989d9b");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

	}

	private OnClickListener startClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					setParams();
					speechSynthesizer.speak(inputTextView.getText().toString());
				}
			}).start();

		}
	};

	private void setParams() {
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,
				SpeechSynthesizer.AUDIO_ENCODE_AMR);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,
				SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
	}

	@Override
	public void onBufferProgressChanged(SpeechSynthesizer synthesizer,
			int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(SpeechSynthesizer synthesizer, SpeechError error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer synthesizer,
			byte[] audioData, boolean isLastData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechPause(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer synthesizer,
			int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechResume(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechStart(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartWorking(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSynthesizeFinish(SpeechSynthesizer synthesizer) {
		// TODO Auto-generated method stub

	}
}

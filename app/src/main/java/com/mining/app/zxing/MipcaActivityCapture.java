package com.mining.app.zxing;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.ReCode;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SLikeCkeckActivity;
import com.yj.shopapp.ui.activity.wholesale.WLikeCkeckActivity;
import com.yj.shopapp.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Vector;

public class MipcaActivityCapture extends BaseActivity implements Callback {


    private ViewfinderView viewfinderView;
    private TextView title;
    private TextView id_right_btu;

    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;


    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;

    private int ScanType = -1;
    private String type = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initData() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        title = (TextView) findViewById(R.id.title);
        title.setText("扫描");

        id_right_btu = (TextView) findViewById(R.id.id_right_btu);

        if (getIntent().hasExtra(Contants.ScanValueType.KEY)) {
            ScanType = getIntent().getExtras().getInt(Contants.ScanValueType.KEY);
        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        if (type.equals("goodsRecord")) {
            id_right_btu.setVisibility(View.GONE);
        }
        if (ScanType == Contants.ScanValueType.original_type) {

        } else if (ScanType == Contants.ScanValueType.W_type || ScanType == Contants.ScanValueType.S_type) {
            id_right_btu.setText("条码输入");
            id_right_btu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new MaterialDialog.Builder(MipcaActivityCapture.this)
//                            .inputType(InputType.TYPE_CLASS_TEXT |
//                                    InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
//                                    InputType.TYPE_TEXT_FLAG_CAP_WORDS)
//                            .positiveText("确定")
//                            .negativeText("取消")
//                            .title("请先输入条码")
//                            .input("请输入条码", "", false, new MaterialDialog.InputCallback() {
//                                @Override
//                                public void onInput(MaterialDialog dialog, CharSequence input) {
//                                    if(input==null|| StringHelper.isEmpty(input.toString())){
//                                        showToastShort("请输入商品条形码");
//                                        return ;
//                                    }
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("result",input.toString());
//                                    CommonUtils.goResult(MipcaActivityCapture.this,bundle,Contants.Photo.REQUEST_SCAN_CODE);
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
                    if (ScanType == Contants.ScanValueType.W_type) {
                        CommonUtils.goActivity(mContext, WLikeCkeckActivity.class, null);
                    }
                    if (ScanType == Contants.ScanValueType.S_type) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("checkGoods", type);
                        CommonUtils.goActivity(mContext, SLikeCkeckActivity.class, null);
                    }
                }
            });

        }
//        else if(ScanType==Contants.ScanValueType.S_type){
//
//        }

        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }


    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //onResultHandler(resultString, barcode);
        if (type.equals("home")) {
            EventBus.getDefault().postSticky(new ReCode(1, resultString));
        } else {
            EventBus.getDefault().postSticky(new ReCode(2, resultString));
        }

        finish();
    }

    /**
     * 跳转到上一个页面
     *
     * @param resultString
     * @param bitmap
     */
    private void onResultHandler(String resultString, Bitmap bitmap) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            return;
        }

//        Intent resultIntent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("result", resultString);
//        resultIntent.putExtras(bundle);
        //this.setResult(Contants.Photo.REQUEST_SCAN_CODE, resultIntent);
//        // EventBus.getDefault().post(new EventMassg(resultString));
//        MipcaActivityCapture.this.finish();
        MipcaActivityCapture.this.finish();
//        if (ScanType==Contants.ScanValueType.W_type)
//        {
//            CommonUtils.goActivity(mContext, WGoodsDetailActivity.class, null);
//        }
//        if (ScanType==Contants.ScanValueType.S_type) {
////                            Bundle bundle = new Bundle();
////                            bundle.putString("checkGoods", type);
//            CommonUtils.goActivity(mContext, SGoodsDetailActivity.class, null);
//        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }


    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


}
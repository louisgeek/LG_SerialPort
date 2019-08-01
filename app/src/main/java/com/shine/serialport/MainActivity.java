package com.shine.serialport;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shine.seriablebedlib.serialdog.SerialManager;
import com.shine.seriablebedlib.serialdog.bean.KeyUp;
import com.shine.seriablebedlib.serialdog.serialutil.LogPlus;
import com.shine.seriablebedlib.serialdog.serialutil.RxBus;
import com.shine.seriablebedlib.services.KeyDownService;
import com.shine.seriablebedlib.system.SYSTEM;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {

    private SerialManager serialManager;
    private boolean keyOne = true;
    private boolean keyTwo = true;
    private boolean keyThree = true;
    private boolean keyFour = true;
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serialManager = new SerialManager();
        LogPlus.i("开启KeyDownService.");
        Intent intent = new Intent(this, KeyDownService.class);
        try {
            stopService(intent);
        } catch (Exception ignored) {
        }
        startService(intent);
        initObserverKey();
    }

    private void initObserverKey() {
        if (subscribe != null) {
            subscribe.dispose();
        }
        subscribe = RxBus.INSTANCE.toObservable(KeyUp.class).map(new Function<KeyUp, Integer>() {
            @Override
            public Integer apply(KeyUp keyUp) throws Exception {
                return keyUp.keyCode;
            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return ((integer == 137 || integer == 82) && keyOne)
                        || (integer == 138 && keyTwo)
                        || (integer == 139 && keyThree)
                        || (integer == 140 && keyFour);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer keycode) throws Exception {
                        switch (keycode) {
                            case 137:
                                keyOne = false;
                                keyFour = false;
                                BaseApplication.getMainHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        keyOne = true;
                                        keyFour = true;
                                    }
                                }, 2000);
                                SYSTEM.INSTANCE.doLightOn();
                                LogPlus.i("", "床头屏呼叫按键");
                                break;
                            case 138:
                                keyTwo = false;
                                keyTwo = false;
                                BaseApplication.getMainHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        keyTwo = true;
                                    }
                                }, 1000);
                                SYSTEM.INSTANCE.doLightOn();
                                LogPlus.i("", "床头屏增援按键");
                                break;
                            case 139:
                                keyThree = false;
                                BaseApplication.getMainHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        keyThree = true;
                                    }
                                }, 1000);
                                LogPlus.i("", "床头屏定位按键");
                                break;
                            case 140:
                                keyFour = false;
                                keyOne = false;
                                BaseApplication.getMainHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        keyFour = true;
                                        keyOne = true;
                                    }
                                }, 2000);
                                LogPlus.i("", "床头屏取消按键");
                                break;
                            case 82:
                                keyOne = false;
                                keyFour = false;
                                BaseApplication.getMainHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        keyOne = true;
                                        keyFour = true;
                                    }
                                }, 2000);
                                SYSTEM.INSTANCE.doLightOn();
                                LogPlus.i("", "床头屏手柄按键");
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serialManager != null) {
            serialManager.stopSerial();
        }
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }
        Intent intent = new Intent(this, KeyDownService.class);
        try {
            stopService(intent);
        } catch (Exception ignored) {
        }
    }
}

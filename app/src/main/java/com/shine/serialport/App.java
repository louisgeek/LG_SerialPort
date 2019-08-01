package com.shine.serialport;

import com.shine.seriablebedlib.SeriableBedLibProvider;

/**
 * @author noxingde@163.com
 * @time 2019/7/5 13:36
 * @description:
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //
        SeriableBedLibProvider.init(this);
    }
}

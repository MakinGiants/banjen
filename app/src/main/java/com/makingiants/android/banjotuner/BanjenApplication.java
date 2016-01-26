package com.makingiants.android.banjotuner;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by danielgomez22 on 9/15/15.
 */
public class BanjenApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
  }
}

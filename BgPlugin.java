package com.resonar.eq;

import android.content.Intent;
import android.os.Build;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Bg")
public class BgPlugin extends Plugin {
  @PluginMethod
  public void start(PluginCall call) {
    Intent i = new Intent(getContext(), MediaService.class);
    if (Build.VERSION.SDK_INT >= 26) getContext().startForegroundService(i);
    else getContext().startService(i);
    call.resolve();
  }

  @PluginMethod
  public void stop(PluginCall call) {
    getContext().stopService(new Intent(getContext(), MediaService.class));
    call.resolve();
  }
}

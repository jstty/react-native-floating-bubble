
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

public class RNFloatingBubbleModule extends ReactContextBaseJavaModule {

  private BubblesManager bubblesManager;
  private final ReactApplicationContext reactContext;

  public RNFloatingBubbleModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    try {
      initializeBubblesManager();
    } catch (Exception e) {

    }
  }

  @Override
  public String getName() {
    return "RNFloatingBubble";
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void showFloatingBubble(final Promise promise) {
    try {
      this.addNewBubble();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  private void addNewBubble() {
    BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, null);
    bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(BubbleLayout bubble) {
        sendEvent("floating-bubble-remove");
      }
    });
    bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

      @Override
      public void onBubbleClick(BubbleLayout bubble) {
        sendEvent("floating-bubble-press");
      }
    });
    bubbleView.setShouldStickToWall(true);
    bubblesManager.addBubble(bubbleView, 60, 20);
  }

  private void initializeBubblesManager() {
    bubblesManager = new BubblesManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout)
        .setInitializationCallback(new OnInitializedCallback() {
          @Override
          public void onInitialized() {
            // addNewBubble();
          }
        }).build();
    bubblesManager.initialize();
  }

  private void sendEvent(String eventName) {
    WritableMap params = Arguments.createMap();
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }
}
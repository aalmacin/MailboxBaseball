package com.aldrinmike.mailboxbaseball;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.os.Handler;

public class SplashActivity extends BaseGameActivity {

	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
//	private int CAMERA_WIDTH;
//	private int CAMERA_HEIGHT;
	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mSplashTextureRegion;
	private Handler mHandler;
	
	public Engine onLoadEngine() {
//		Point pointSize = new Point();
//		getWindowManager().getDefaultDisplay().getSize(pointSize);
//		int CAMERA_WIDTH = pointSize.x;
//		int CAMERA_HEIGHT = pointSize.y;
		mHandler = new Handler();
		this.mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT)
				, this.mCamera));
	}

	public void onLoadResources() {
		this.mTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSplashTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/splashScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene(1);
		final int centerX = (CAMERA_WIDTH-this.mSplashTextureRegion.getWidth());
		final int centerY = (CAMERA_HEIGHT-this.mSplashTextureRegion.getHeight());
		
		final Sprite splash = new Sprite(centerX,centerY,this.mSplashTextureRegion);
		scene.getLastChild().attachChild(splash);
		return scene;
	}

	public void onLoadComplete() {
		mHandler.postDelayed(mLaunchTask,3000);
	}
	
	private Runnable mLaunchTask = new Runnable() {
		
		@Override
		public void run() {
			Intent myIntent = new Intent(SplashActivity.this,MainMenuActivity.class);
			SplashActivity.this.startActivity(myIntent);
			finish();
		}
	};
}

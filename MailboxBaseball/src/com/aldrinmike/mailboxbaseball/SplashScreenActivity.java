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
/**
 * FileName: SplashScreenActivity.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>December 8, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>SplashScreenActivity shows the game's splash screen for 3 seconds then proceed with the game.
 *         </p>
 * 
 */
public class SplashScreenActivity extends BaseGameActivity {

	// The camera width and height
	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
	
	// The duration of the splash screen
	private final static int SPLASH_DURATION = 3000;

	// A reference to the game camera.
	private Camera mCamera;
	
	// The handler that calls the Runnables that are to be called when needed
	private Handler mHandler;
	
	// The sprite that is used to show the splash art.
	private Sprite mSplash;
	
	@Override
	public Engine onLoadEngine() {
		mHandler = new Handler();
		this.mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT)
				, this.mCamera));
	} // End of onLoadEngine method

	@Override
	public void onLoadResources() {
		// Load and position the splash screen sprite.
		Texture mTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mSplashTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this, "gfx/splashScreen.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mTexture);

		final int centerX = (CAMERA_WIDTH-mSplashTextureRegion.getWidth());
		final int centerY = (CAMERA_HEIGHT-mSplashTextureRegion.getHeight());
		mSplash = new Sprite(centerX,centerY,mSplashTextureRegion);
	} // End of onLoadResources method

	@Override
	public Scene onLoadScene() {
		// Create the scene and attach the Sprite
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene(1);
		scene.getLastChild().attachChild(mSplash);
		return scene;
	} // End of onLoadScene method

	@Override
	public void onLoadComplete() {
		// Launch the control screen after the delay
		mHandler.postDelayed(mLaunchControlScreen,SPLASH_DURATION);
	} // End of onload complete
	
	/**
	 * Runnable that launches the ControlScreenActivity
	 */
	private Runnable mLaunchControlScreen = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(SplashScreenActivity.this,ControlScreenActivity.class);
			SplashScreenActivity.this.startActivity(myIntent);
			finish();
		} // End of run method
	}; // End of Runnable anonymous inner class
} // End of SplashScreenActivity class

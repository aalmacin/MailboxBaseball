package com.aldrinmike.mailboxbaseball;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;

public class HelpActivity extends BaseGameActivity {


	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
	
	private Camera mCamera;
	private Font mFont;
	private Texture mFontTexture;
	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundTextureRegion;
	private Scene mMainScene;
	


	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "kulminoituva.ttf", 32, true, Color.BLUE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
		
		
		this.mBackgroundTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(mBackgroundTexture, this, "gfx/background.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.createStaticMenuScene();
		this.mMainScene = new Scene(1);
		final int centerX = (CAMERA_WIDTH-this.mBackgroundTextureRegion.getWidth());
		final int centerY = (CAMERA_HEIGHT-this.mBackgroundTextureRegion.getHeight());
		
		final Text text = new Text(20, 20, mFont, "The game is played by avoiding trucks.");
		this.mMainScene.attachChild(text);
		
		final Sprite main = new Sprite(centerX,centerY,this.mBackgroundTextureRegion);
		mMainScene.getLastChild().attachChild(main);
		return this.mMainScene;
	}

	private void createStaticMenuScene() {		
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
}

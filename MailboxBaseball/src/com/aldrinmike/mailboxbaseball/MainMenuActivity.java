package com.aldrinmike.mailboxbaseball;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;


import android.graphics.Color;
import android.widget.Toast;

public class MainMenuActivity extends BaseGameActivity  implements IOnMenuItemClickListener{

	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;


	private static final int CAR_LEFT_POSITION = 100;
	private static final int CAR_RIGHT_POSITION = 230;
	private static final int CAR_YPOSITION = CAMERA_HEIGHT-300;
	
	private final static int MENU_START = 0;
	private final static int MENU_HELP = 1;
	private final static int MENU_HIGHSCORES = 2;
	private final static int MENU_EXIT = 3;
	
	private Camera mCamera;
	private Font mFont;
	private Texture mFontTexture;
	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundTextureRegion;
	private Scene mMainScene;
	private MenuScene mStaticMenuScene;
	private MainBG mMainBGSprite;
	private Sprite mHelpScreenSprite;
	private Texture mHelpScreenTexture;
	private TextureRegion mHelpScreenTextureRegion;
	private Texture mBackButtonTexture;
	private TextureRegion mBackButtonTextureRegion;
	private Sprite mBackButtonSprite;
	private Scene mGameScene;
	private Scene mHelpScene;
//	private Texture mCarTexture;
//	private TextureRegion mCarTextureRegion;
//	private Sprite mCarSprite;
	public boolean carInRight;
	private Texture mCarTiledTexture;
	private TiledTextureRegion mCarTiledTextureRegion;
	private TiledSprite mCarTiledSprite;
	private Texture mGameScreenTexture;
	private TiledTextureRegion mGameScreenTextureRegion;
	private AnimatedSprite mRoadTiledSprite;
	private Texture mTruckTexture;
	private TextureRegion mTruckTextureRegion;
	private Sprite mTruckSprite;
	private Timer truckTimer;


	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		carInRight = true;
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
		
		mMainBGSprite = new MainBG(CAMERA_WIDTH-this.mBackgroundTextureRegion.getWidth(),
				CAMERA_HEIGHT-this.mBackgroundTextureRegion.getHeight(),this.mBackgroundTextureRegion);
		

		this.mHelpScreenTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mHelpScreenTextureRegion = TextureRegionFactory.createFromAsset(mHelpScreenTexture, this, "gfx/helpScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mHelpScreenTexture);

		mHelpScreenSprite = new Sprite(CAMERA_WIDTH-this.mHelpScreenTextureRegion.getWidth(),
				CAMERA_WIDTH-this.mHelpScreenTextureRegion.getWidth(),this.mHelpScreenTextureRegion);

		this.mBackButtonTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackButtonTextureRegion = TextureRegionFactory.createFromAsset(mBackButtonTexture, this, "gfx/backButton.png", 0,0);
		this.mEngine.getTextureManager().loadTexture(this.mBackButtonTexture);
		
		mBackButtonSprite = new BackButton(40,CAMERA_HEIGHT-100,this.mBackButtonTextureRegion);		


//		this.mCarTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.mCarTextureRegion = TextureRegionFactory.createFromAsset(mCarTexture, this, "gfx/car.png", 0,0);
//		this.mEngine.getTextureManager().loadTexture(this.mCarTexture);
//		
//		mCarSprite = new Sprite(CAR_RIGHT_POSITION,CAR_YPOSITION,this.mCarTextureRegion);
		
		this.mCarTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCarTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarTiledTexture, this, "gfx/carTile.png", 
				0, 0, 3, 1);
		this.mEngine.getTextureManager().loadTexture(this.mCarTiledTexture);
		
		this.mCarTiledSprite = new TiledSprite(CAR_RIGHT_POSITION, CAR_YPOSITION, mCarTiledTextureRegion);
		

		
		this.mGameScreenTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGameScreenTextureRegion = TextureRegionFactory.createTiledFromAsset(mGameScreenTexture, this, "gfx/gameScreen.png", 
				0, 0, 2, 1);
		this.mEngine.getTextureManager().loadTexture(this.mGameScreenTexture);
		
		this.mRoadTiledSprite = new AnimatedSprite(0, 0, mGameScreenTextureRegion);
		mRoadTiledSprite.animate(10);
		


		this.mTruckTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTruckTextureRegion = TextureRegionFactory.createFromAsset(mTruckTexture, this, "gfx/truck.png", 0,0);
		this.mEngine.getTextureManager().loadTexture(this.mTruckTexture);
		
		mTruckSprite = new Sprite(-CAMERA_WIDTH,-CAMERA_HEIGHT,this.mTruckTextureRegion);		
		
	}
	
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.createStaticMenuScene();
		this.mMainScene = new Scene(1);
		mMainScene.getLastChild().attachChild(mMainBGSprite);
		mMainScene.setChildScene(mStaticMenuScene);	
		mMainScene.registerTouchArea(mBackButtonSprite);	
		return this.mMainScene;
	}

	private void createStaticMenuScene() {		
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem startMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mFont, "Start"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		startMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(startMenuItem);
		
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HELP, mFont, "Help"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		
		final IMenuItem highScoreMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HIGHSCORES, mFont, "High Scores"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		highScoreMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(highScoreMenuItem);
		
		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_EXIT, mFont, "Exit"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		exitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(exitMenuItem);		
		
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
	}

	private void createGameScene()
	{
		if(mGameScene == null){
			mGameScene = new Scene(1);
			mGameScene.getLastChild().attachChild(mMainBGSprite);
			mGameScene.getLastChild().attachChild(mRoadTiledSprite);
			mGameScene.getLastChild().attachChild(mCarTiledSprite);
			mGameScene.getLastChild().attachChild(mTruckSprite);
			mGameScene.registerTouchArea(mMainBGSprite);
			
			truckTimer = new Timer();
			truckTimer.schedule(new TimerTask() {			
				private int timeElapsed;
				@Override
				public void run() {
					timeElapsed++;
					if(timeElapsed % 5 == 0)
					{
						int truckPosition = (Math.random()>0.5)?CAR_LEFT_POSITION:CAR_RIGHT_POSITION;
						mTruckSprite.setPosition(truckPosition, -CAMERA_HEIGHT);
						dropTheTruck();
					}
				}
				
			}, (long)0, (long)1000);
		}
		
		mGameScene.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(mTruckSprite.collidesWith(mCarTiledSprite)){
					mRoadTiledSprite.stopAnimation();
					truckTimer.cancel();
					mTruckSprite.clearEntityModifiers();
					mTruckSprite.setPosition(mCarTiledSprite.getX(), CAR_YPOSITION-mTruckSprite.getHeight());
				}
			}
		});
	}
	
	private void dropTheTruck() {
		mTruckSprite.registerEntityModifier(new MoveYModifier(2, -30, CAMERA_HEIGHT));
	}

	private void createHelpScreen() {
		if(mHelpScene == null){
			mHelpScene = new Scene(1);
			final Text text = new Text(20, 20, mFont, "The Mailbox Baseball Game");
			mHelpScene.getLastChild().attachChild(mHelpScreenSprite);
			mHelpScene.getLastChild().attachChild(text);
			mHelpScene.getLastChild().attachChild(mBackButtonSprite);
		}
	}
	
	@Override
	public void onLoadComplete() {
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_START:		
			Toast.makeText(MainMenuActivity.this,"Start Selected", Toast.LENGTH_SHORT).show();
			createGameScene();
			this.mMainScene.setChildScene(mGameScene);
			return true;
		case MENU_HELP:
			createHelpScreen();
			this.mMainScene.setChildScene(mHelpScene);
			Toast.makeText(MainMenuActivity.this,"Help Selected", Toast.LENGTH_SHORT).show();
			return true;
		case MENU_HIGHSCORES:
			Toast.makeText(MainMenuActivity.this,"High Score Selected", Toast.LENGTH_SHORT).show();
			return true;
		case MENU_EXIT:
			this.finish();
			return true;
		default:
			return false;
		}
	}

	private class BackButton extends Sprite
	{

		public BackButton(float pX, float pY, TextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
		}
		
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mMainScene.getLastChild().attachChild(mMainBGSprite);
				mMainScene.setChildScene(mStaticMenuScene);
			
			return super
					.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	}	

	private class MainBG extends Sprite
	{
		public MainBG(float pX, float pY, TextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
		}
		
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if(pTouchAreaLocalX < CAMERA_WIDTH/2)
			{
				if(carInRight)
				{
					mCarTiledSprite.setCurrentTileIndex(0);
					mCarTiledSprite.setPosition(CAR_LEFT_POSITION, mCarTiledSprite.getY());
				}
				else
				{
					mCarTiledSprite.setCurrentTileIndex(1);
				}
				carInRight = false;
			}
			else
			{
				if(!carInRight)
				{
					mCarTiledSprite.setCurrentTileIndex(0);
					mCarTiledSprite.setPosition(CAR_RIGHT_POSITION, mCarTiledSprite.getY());
				}
				else
				{
					mCarTiledSprite.setCurrentTileIndex(2);
				}
				carInRight = true;
			}
			
			return super
					.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	}	
}

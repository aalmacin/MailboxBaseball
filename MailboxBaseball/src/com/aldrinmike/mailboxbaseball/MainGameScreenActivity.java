package com.aldrinmike.mailboxbaseball;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class MainGameScreenActivity extends BaseGameActivity  implements IOnMenuItemClickListener{

	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
	
	private final static int MENU_START = 0;
	private final static int MENU_HELP = 1;
	private final static int MENU_HIGHSCORES = 2;
	private final static int MENU_EXIT = 3;
	
	private Camera mCamera;
	private Font mFont;
	
	private Scene mMainScene;
	private Scene mHelpScene;
	private HighScoreScene mHighScoreScene;
	private MenuScene mStaticMenuScene;
	
	
	private Sprite mBackgroundSprite;	
	private Sprite mHelpScreenSprite;
	private Sprite mBackButtonSprite;
	private Sprite mHighScoreSceneSprite;
	
	public Context mContext;
	private Font mHighScoreFont;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		
		
		mContext = this;
		Texture mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(mFontTexture, this, "kulminoituva.ttf", 48, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);	
		
		Texture mScoreFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mHighScoreFont = FontFactory.createFromAsset(mScoreFontTexture, this, "FLORLRG_.ttf", 20, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mScoreFontTexture);
		mEngine.getFontManager().loadFont(mHighScoreFont);	
		
		Texture mBackgroundTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(mBackgroundTexture, this, "gfx/background.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mBackgroundTexture);	
		
		mBackgroundSprite = new Sprite(CAMERA_WIDTH-mBackgroundTextureRegion.getWidth(),
				CAMERA_HEIGHT-mBackgroundTextureRegion.getHeight(),mBackgroundTextureRegion);		

		Texture mHelpScreenTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mHelpScreenTextureRegion = TextureRegionFactory.createFromAsset(mHelpScreenTexture, this, "gfx/helpScreen.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mHelpScreenTexture);

		mHelpScreenSprite = new Sprite(CAMERA_WIDTH-mHelpScreenTextureRegion.getWidth(),
				CAMERA_WIDTH-mHelpScreenTextureRegion.getWidth(),mHelpScreenTextureRegion);
		
		Texture mHighScoreTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mHighScoreTextureRegion = TextureRegionFactory.createFromAsset(mHighScoreTexture, this, "gfx/highScore.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mHighScoreTexture);
		
		mHighScoreSceneSprite = new Sprite(CAMERA_WIDTH-mHighScoreTextureRegion.getWidth(),
				CAMERA_WIDTH-mHighScoreTextureRegion.getWidth(),mHighScoreTextureRegion);

		Texture mBackButtonTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mBackButtonTextureRegion = TextureRegionFactory.createFromAsset(mBackButtonTexture, this, "gfx/backButton.png", 0,0);
		mEngine.getTextureManager().loadTexture(mBackButtonTexture);
		
		mBackButtonSprite = new BackButton(40,CAMERA_HEIGHT-100,mBackButtonTextureRegion);		
	}
	
	@Override
	public Scene onLoadScene() {


		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.createStaticMenuScene();
		this.createHelpScreen();
		this.createHighScoreScreen();
		this.mMainScene = new Scene(1);
		mMainScene.getLastChild().attachChild(mBackgroundSprite);
		mMainScene.setChildScene(mStaticMenuScene);	
		mMainScene.registerTouchArea(mBackButtonSprite);	
		
		return this.mMainScene;
	}

	private void createStaticMenuScene() {		
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem startMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mFont, "Start"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		startMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(startMenuItem);
		
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HELP, mFont, "Help"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		
		final IMenuItem highScoreMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HIGHSCORES, mFont, "High Scores"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		highScoreMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(highScoreMenuItem);
		
		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_EXIT, mFont, "Exit"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		exitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(exitMenuItem);		
		
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
		this.mStaticMenuScene.setPosition(mStaticMenuScene.getInitialX(), mStaticMenuScene.getInitialY() + 90);
	}

	private void createHelpScreen() {
		if(mHelpScene == null){
			mHelpScene = new Scene(1);
			mHelpScene.getLastChild().attachChild(mHelpScreenSprite);
			mHelpScene.getLastChild().attachChild(mBackButtonSprite);
		}
	}
	
	private void createHighScoreScreen() {
		if(mHighScoreScene == null){
			mHighScoreScene = new HighScoreScene(1);
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
			Intent myIntent = new Intent(MainGameScreenActivity.this,MainGameActivity.class);
			MainGameScreenActivity.this.startActivity(myIntent);
			mMainScene.detachChildren();
			finish();
			return true;
		case MENU_HELP:
			this.mMainScene.setChildScene(mHelpScene);
			return true;
		case MENU_HIGHSCORES:
			mHighScoreScene.updateTexts();
			this.mMainScene.setChildScene(mHighScoreScene);
			return true;
		case MENU_EXIT:
			this.finish();
			return true;
		default:
			return false;
		}
	}

	private class HighScoreScene extends Scene
	{
		private Controller mController;
		private ArrayList<ArrayList<String>> top10Players;
		private ChangeableText mTextLeft;
		private ChangeableText mTextRight;
		public HighScoreScene(int pLayerCount) {
			super(pLayerCount);
			mTextLeft = new ChangeableText(100, 300, mHighScoreFont, "Empty text                   ");
			mTextRight = new ChangeableText(300, 300, mHighScoreFont, "");
			mController = new Controller(mContext);
			this.getLastChild().attachChild(mHighScoreSceneSprite);
			this.getLastChild().attachChild(mBackButtonSprite);
			this.getLastChild().attachChild(mTextLeft);
			this.getLastChild().attachChild(mTextRight);
			updateTexts();
		}

		private void updateTexts() {
			top10Players = mController.getTop10Scores();
			if(top10Players == null)
				mTextLeft.setText("No player played the game yet");
			else
			{
				String players = "";
				String scores = "";
				for(int i = 0;i<top10Players.size();i++){
					players += (top10Players.get(i).get(0)+"\n");
					scores += (top10Players.get(i).get(1)+"\n");
				}
				mTextLeft.setText(players);
				mTextRight.setText(scores);
			}
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
				mMainScene.getLastChild().attachChild(mBackgroundSprite);
				mMainScene.setChildScene(mStaticMenuScene);
			
			return super
					.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	}	
	
}

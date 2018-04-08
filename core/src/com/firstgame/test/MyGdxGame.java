package com.firstgame.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Random;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture gameOver,backGround;
	boolean istouched=false;
	Texture[] birds,tubes;
	int gamestate=0,scoringTube=0,score=0,velocity =0,birdY,tubeVelcity=0,state =0;
	float gap = 400;
    Random random = new Random();
    ShapeRenderer shapeRenderer;
    BitmapFont font;




	int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
	float distanceBetweenTubes;

	float[] maxTubeOffSet = new float[numberOfTubes];
	Circle birdCircle;
	Rectangle[] tubeTopRectangles,tubeButtomRectangles;


	@Override
	public void create () {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(10);


        birdCircle = new Circle();
        tubeTopRectangles = new Rectangle[numberOfTubes];
        tubeButtomRectangles= new Rectangle[numberOfTubes];
        batch = new SpriteBatch();
		backGround = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		tubes = new Texture[2];
		tubes[0] = new Texture("bottomtube.png");
		tubes[1] = new Texture("toptube.png");

		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;

        for (int i = 0; i <numberOfTubes ; i++) {
            maxTubeOffSet[i] = (random.nextFloat() -0.5f)*(Gdx.graphics.getHeight() - gap -200);
            tubeX[i] = Gdx.graphics.getWidth()/2 - tubes[0].getWidth()/2 +tubeVelcity +Gdx.graphics.getWidth()+i*distanceBetweenTubes;

            tubeTopRectangles[i] = new Rectangle();
            tubeButtomRectangles[i] = new Rectangle();
        }
    }

	@Override
	public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
		batch.begin();
        batch.draw(backGround,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

if(gamestate==1) {
    if (Gdx.input.justTouched()) {
        velocity = -23;
        birdY -= velocity;
        istouched=true;
    }
    if(birdY>Gdx.graphics.getHeight() - birds[0].getHeight()){
        gamestate=2;
    }

    if (birdY > 0) {
        velocity += 2;
        birdY -= velocity;

    }else {
        gamestate =2;
    }
    if (state == 0) {
        state = 1;
    } else {
        state = 0;
    }

    if (istouched) {
        if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
            score++;
            if (scoringTube < numberOfTubes - 1) {
                scoringTube++;
            } else {
                scoringTube = 0;
            }
        }


        for (int i = 0; i < numberOfTubes; i++) {
            if (tubeX[i] < -tubes[0].getWidth()) {
                tubeX[i] += numberOfTubes * distanceBetweenTubes;
                maxTubeOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            } else {
                tubeX[i] -= 3;
            }

            batch.draw(tubes[1], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + maxTubeOffSet[i]);
            batch.draw(tubes[0], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[1].getHeight() + maxTubeOffSet[i]);
            if(score>9){
                font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2 -60, 1200);
            }else{
                font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2 +7, 1200);
            }

            tubeTopRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + maxTubeOffSet[i], tubes[1].getWidth(), tubes[1].getHeight());
            tubeButtomRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[0].getHeight() + maxTubeOffSet[i], tubes[0].getWidth(), tubes[0].getHeight());
        }

    }
}else if(gamestate ==0){
    if(Gdx.input.justTouched()){
        gamestate =1;
        velocity = -20;
        birdY -= velocity;
    }
}else if (gamestate ==2){
    batch.draw(birds[0], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
    if(istouched){
        batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
    }

    if(score>9){
        font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2 , 1200);
    }else{
        font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2 + 7, 1200);
    }


    if(Gdx.input.justTouched()){

        score=0;
        scoringTube=0;
        gamestate=1;
        velocity =0;
        velocity = -20;
        birdY -= velocity;
        birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

        for (int i = 0; i <numberOfTubes ; i++) {
            maxTubeOffSet[i] = (random.nextFloat() -0.5f)*(Gdx.graphics.getHeight() - gap -200);
            tubeX[i] = Gdx.graphics.getWidth()/2 - tubes[0].getWidth()/2 +tubeVelcity +Gdx.graphics.getWidth()+i*distanceBetweenTubes;

            tubeTopRectangles[i] = new Rectangle();
            tubeButtomRectangles[i] = new Rectangle();
        }
    }
}
            if(birdY<10){
                batch.draw(birds[0], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
            }else {
                batch.draw(birds[state], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
            }

		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLUE);
		birdCircle.set(Gdx.graphics.getWidth() / 2,birdY+birds[state].getHeight()/2,birds[state].getWidth()/2);
        for (int i = 0; i <numberOfTubes ; i++) {
            //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + maxTubeOffSet[i],tubes[1].getWidth(),tubes[1].getHeight());
            //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[1].getHeight() + maxTubeOffSet[i],tubes[0].getWidth(),tubes[0].getHeight());
            if(Intersector.overlaps(birdCircle,tubeTopRectangles[i])||Intersector.overlaps(birdCircle,tubeButtomRectangles[i])){
                gamestate =2;
            }

        }
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		shapeRenderer.end();
        }
	
	@Override
	public void dispose () {
		batch.dispose();
		backGround.dispose();
	}
}

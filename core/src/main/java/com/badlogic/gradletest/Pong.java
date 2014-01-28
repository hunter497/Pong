
package com.badlogic.gradletest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Pong extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture paddleTexture;
    Texture pipTexture;
    Rectangle player1, player2;
    Rectangle pip;
    private int paddleSpeed = 250;
    private int paddleHeight = 144;
    private int pipWidth = 10, pipHeight = 10;
    private int screenHeight = 400, screenWidth = 800;
    private float deltaTime;
    private int pipSpeedX = 200, pipSpeedY = 200;
    private int pipSignX = 1, pipSignY = 1;
    private int playerScore1 = 0, playerScore2 = 0, lastHit = 0;

    @Override
    public void create () {
        Gdx.app.log("Pong", "create()");
        Texture.setEnforcePotImages(false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        batch = new SpriteBatch();
        paddleTexture = new Texture("PaddleTexture.png");
        pipTexture = new Texture("PipTexture.png");
        int paddleWidth = 24;
        player1 = new Rectangle(2, screenHeight/2 - paddleHeight/2, paddleWidth + 5, paddleHeight);
        player2 = new Rectangle(screenWidth - (paddleWidth + 2), screenHeight/2 - paddleHeight/2, paddleWidth, paddleHeight);
        pip = new Rectangle(screenWidth/2 - (pipWidth/2), screenHeight/2 - pipHeight/2, pipWidth, pipHeight);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        deltaTime = Gdx.graphics.getDeltaTime();
        drawPlayers();
        drawBall();
        batch.end();
    }

    @Override
    public void dispose() {
        Gdx.app.log("Pong", "Dispose()");
        batch.dispose();
        paddleTexture.dispose();
        pipTexture.dispose();
    }

    private void drawPlayers() {
        player1Movement();
        player2Movement();
        batch.draw(paddleTexture, player1.x, player1.y);
        batch.draw(paddleTexture, player2.x, player2.y);
    }

    private void drawBall() {
        pipMovement();
        batch.draw(pipTexture, pip.x, pip.y);
    }

    private void resetBall() {
        pip.x = screenWidth/2 - pipWidth/2;
        pip.y = screenHeight/2 - pipHeight/2;
        pipSpeedY = 200;
        pipSpeedX = 200;
    }

    private void pipMovement() {
        pip.x += pipSpeedX * pipSignX * deltaTime;
        pip.y += pipSpeedY * pipSignY * deltaTime;
        if(pip.y > screenHeight - pipHeight || pip.y < 0) {
            pipSpeedY += 5;
            pipSignY = -pipSignY;
            Gdx.app.log("pipSpeedY", Integer.toString(pipSpeedY));
        }
        if(pip.overlaps(player1)) {
            pipSpeedX += 5;
            pipSignX = -pipSignX;
            lastHit = 1;
            Gdx.app.log("pipSpeedX", Integer.toString(pipSpeedX) + " and Player 1 Hit the ball!");
        }
        if(pip.overlaps(player2)) {
            pipSpeedX += 5;
            pipSignX = -pipSignX;
            lastHit = 2;
            Gdx.app.log("pipSpeedX", Integer.toString(pipSpeedX) + " and Player 2 Hit the ball!");
        }
        if(pip.x > screenWidth - pipWidth || pip.x < 0) {
            resetBall();
            if(lastHit == 1) playerScore1 += 1;
            else playerScore2 += 1;
            Gdx.app.log("Point", "Player " + Integer.toString(lastHit) + " Scored!");
            Gdx.app.log("Score", "Score is \nPlayer 1: " + Integer.toString(playerScore1) + "\nPlayer 2: " + Integer.toString(playerScore2));
        }
    }

    private void player1Movement() {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) player1.y += paddleSpeed * deltaTime;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) player1.y -= paddleSpeed * deltaTime;
        if(player1.y < 0) player1.y = 0;
        if(player1.y > screenHeight - paddleHeight) player1.y = screenHeight - paddleHeight;
    }

    private void player2Movement() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) player2.y += paddleSpeed * deltaTime;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) player2.y -= paddleSpeed * deltaTime;
        if(player2.y < 0) player2.y = 0;
        if(player2.y > screenHeight - paddleHeight) player2.y = screenHeight - paddleHeight;
    }

}

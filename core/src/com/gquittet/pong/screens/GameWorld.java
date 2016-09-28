package com.gquittet.pong.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gquittet.pong.assets.AssetLoader;
import com.gquittet.pong.helpers.Collisions;
import com.gquittet.pong.logs.Log;
import com.gquittet.pong.objects.Ball;
import com.gquittet.pong.objects.Bat;
import com.gquittet.pong.objects.ScoreBoard;

import java.util.Timer;
import java.util.TimerTask;

public class GameWorld {

    private Bat batLeft;
    private Bat batRight;
    private Ball ball;
    private float gameWidth;

    // The scoreboard
    private ScoreBoard scoreBoard;

    public GameWorld() {
        int batWidth = 5;
        int batHeight = 20;
        gameWidth = 160;
        batLeft = new Bat(batWidth, batHeight, new Vector2(0, 0));
        batRight = new Bat(batWidth, batHeight, new Vector2(gameWidth - batWidth, 0));
        ball = new Ball(5, 5, new Vector2(0, 0), 2f, true);
        scoreBoard = new ScoreBoard();
        start();
    }

    private void start() {
        ball.setDirection(ball.getSpeed(), 0);
        ball.setPosition(new Vector2((gameWidth - ball.getWidth())/2, (gameWidth - ball.getWidth())/2));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ball.setPause(false);
            }
        }, 2000);
    }

    public void update(float delta) {
        batLeft.update(delta);
        batRight.update(delta);
        ball.update(delta);
        collides();
    }

    private void collides() {
        if (Collisions.collides(ball, batLeft, true)) {
            ball.setPosition(new Vector2(batLeft.getPosition().x + batLeft.getWidth(), ball.getPosition().y));
            batLeft.computeDirection(ball, true);
            AssetLoader.pongBat.play();
        } else if (Collisions.collides(ball, batRight, false)) {
            ball.setPosition(new Vector2(batRight.getPosition().x - ball.getWidth(), ball.getPosition().y));
            batRight.computeDirection(ball, false);
            AssetLoader.pongBat.play();
        } else if ((ball.getPosition().y <= 0 || ball.getPosition().y + ball.getHeight() >= 144) &&
                (ball.getPosition().x > 0 || ball.getPosition().x + ball.getWidth() < 144)) {
            ball.setDirection(ball.getDirection().x, -ball.getDirection().y);
            AssetLoader.pongWall.play();
        } else if (ball.getPosition().x <= 0) {
                scoreBoard.increaseBatRightScore();
            ball.setPause(true);
            start();
        } else if (ball.getPosition().x >= 160) {
            scoreBoard.increaseBatLeftScore();
            ball.setPause(true);
            start();
        }
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Bat getBatLeft() {
        return batLeft;
    }

    public Bat getBatRight() {
        return batRight;
    }

    public Ball getBall() {
        return ball;
    }
}
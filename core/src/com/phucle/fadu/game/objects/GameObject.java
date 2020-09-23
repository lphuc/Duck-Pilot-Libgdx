package com.phucle.fadu.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject {
    Vector2 position;
    Vector2 velocity;
    Vector2 gravity;

    // object size
    public float width, height;
    Rectangle bounds;
    public Body body;

    public GameObject() {
        position = new Vector2();
        velocity = new Vector2();
        gravity = new Vector2();
        bounds = new Rectangle();
    }

    // implement these 2 method on specific object
    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
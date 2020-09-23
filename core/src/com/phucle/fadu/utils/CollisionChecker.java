package com.phucle.fadu.utils;

import com.phucle.fadu.game.Level;
import com.phucle.fadu.game.objects.Coin;
import com.phucle.fadu.game.objects.Fuel;
import com.phucle.fadu.game.objects.Duck;
import com.phucle.fadu.game.objects.Rock;

/**
 * Check all collision between objects in game
 */
public class CollisionChecker {
    private static Duck duck;
    private static Rock rock;
    private static Fuel fuel;
    private static Coin coin;

    public static boolean plainCollideCoin() {
        boolean isHit = false;
        for (Coin coin : Level.getInstance().coinList) {
            if (!coin.collected && duck.getBounds().overlaps(coin.getBounds())) {
                isHit = true;
                coin.collected = true;
            }

        }
        return isHit;
    }


    public static boolean planeCollideFuel() {
        boolean isHit = false;
        for (int i = 0; i < Level.getInstance().fuelList.size(); i++) {
            fuel = Level.getInstance().fuelList.get(i);
            if (!fuel.collected && duck.getBounds().overlaps(fuel.getBounds())) {
                isHit = true;
                fuel.collected = true;
            }

        }
        return isHit;
    }

}

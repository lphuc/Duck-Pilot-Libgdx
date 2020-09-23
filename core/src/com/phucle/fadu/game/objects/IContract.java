package com.phucle.fadu.game.objects;

/*
 * Created by lhphuc on 1/24/2019.
 */
public interface IContract {

    // only class that allow object to move implement this
    interface MoveAble {
    }

    // only class that allows object can interact with other CollideAble object
    interface CollideAble {
    }

}

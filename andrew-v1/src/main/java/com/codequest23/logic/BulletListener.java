package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.events.ChangeEvent;
import com.codequest23.model.Bullet;
import com.codequest23.model.GameMap;
import com.codequest23.model.GameObject;
import com.codequest23.util.DoublePair;

import java.util.Map;
import java.util.function.Consumer;

public class BulletListener implements Consumer<ChangeEvent> {


    private final BulletTracker bulletTracker = new BulletTracker();

    @Override
    public void accept(ChangeEvent changeEvent) {
        System.err.println("event called!");
        Game game = changeEvent.game();
        GameMap map = game.map();
        Map<String, GameObject> changed = changeEvent.updated();
        for (Map.Entry<String, GameObject> entry : changed.entrySet()) {
            String objectId = entry.getKey();
            GameObject existingBullet = map.getObject(entry.getKey());
            GameObject updated = entry.getValue();
            if (!(existingBullet instanceof Bullet bullet) || !(updated instanceof Bullet updatedBullet)) {
                continue;
            }
            DoublePair existingVelocity = bullet.velocity();
            DoublePair newVelocity = updatedBullet.velocity();
            if (!existingVelocity.equals(newVelocity)) {
                this.bulletTracker.addCollision(objectId);
                System.err.println("Id: " + objectId + "Collisions: " + this.bulletTracker.getNumCollisions(objectId));
            }
        }
        for (String id : changeEvent.deleted()) {
            this.bulletTracker.clear(id);
        }
    }
}

package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.ObjectTypes;
import com.codequest23.events.ChangeEvent;
import com.codequest23.model.Bullet;
import com.codequest23.model.GameMap;
import com.codequest23.model.GameObject;
import com.codequest23.util.DoublePair;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.function.Consumer;

public class BulletListener implements Consumer<ChangeEvent> {


    private final BulletTracker bulletTracker = new BulletTracker();

    @Override
    public void accept(ChangeEvent changeEvent) {
        System.err.println("event called!");
        Game game = changeEvent.game();
        GameMap map = game.map();
        Map<String, JsonObject> changed = changeEvent.updated();
        for (Map.Entry<String, JsonObject> entry : changed.entrySet()) {
            JsonObject object = entry.getValue();
            if (object.get("type").getAsInt() != ObjectTypes.BULLET.getValue()) {
                continue;
            }
            if (!object.has("velocity")) {
                continue;
            }
            String id = entry.getKey();
            GameObject existingBullet = map.getObject(id);
            if (!(existingBullet instanceof Bullet bullet)) {
                continue;
            }
            DoublePair existingVelocity = bullet.velocity();
            DoublePair newVelocity = game.serializer().readDoublePair(object.get("velocity").getAsJsonArray());
            if (!existingVelocity.equals(newVelocity)) {
                this.bulletTracker.addCollision(id);
                System.err.println("Id: " + id + "Collisions: " + this.bulletTracker.getNumCollisions(id));
            }
        }
        for (String id : changeEvent.deleted()) {
            this.bulletTracker.clear(id);
        }
    }
}

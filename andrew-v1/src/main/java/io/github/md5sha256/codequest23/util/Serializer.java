package io.github.md5sha256.codequest23.util;

import io.github.md5sha256.codequest23.model.Boundary;
import io.github.md5sha256.codequest23.model.Bullet;
import io.github.md5sha256.codequest23.model.ClosingBoundary;
import io.github.md5sha256.codequest23.model.DestructibleWall;
import io.github.md5sha256.codequest23.model.Powerup;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.model.TankAspect;
import io.github.md5sha256.codequest23.model.Wall;
import io.github.md5sha256.codequest23.model.component.BoundingBox;
import io.github.md5sha256.codequest23.model.component.Hitbox;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Serializer {

    private final GameObjectFactory gameObjectFactory;

    public Serializer(GameObjectFactory gameObjectFactory) {
        this.gameObjectFactory = gameObjectFactory;
    }

    public Powerup readPowerup(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.get("position").getAsJsonArray());
        TankAspect aspect = TankAspect.valueOf(object.get("powerup_type").getAsString());
        return this.gameObjectFactory.createPowerup(position)
                .objectId(id)
                .tankAspect(aspect)
                .build();
    }

    public Boundary readBoundary(String id, JsonObject object) {
        BoundingBox boundingBox = readBoundingBox(object.get("position").getAsJsonArray());
        return new Boundary(id, boundingBox);
    }

    public ClosingBoundary readClosingBoundary(String id, JsonObject object) {
        BoundingBox boundingBox = readBoundingBox(object.get("position").getAsJsonArray());
        DoublePair[] velocities;
        return new ClosingBoundary(id, boundingBox, new DoublePair[4]);
    }

    public void updateClosingBoundary(ClosingBoundary existing, JsonObject update) {
        BoundingBox updated = readBoundingBox(update.get("position").getAsJsonArray());
        existing.hitbox(updated);
    }

    public Wall readWall(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        return this.gameObjectFactory.createWall(position).objectId(id).build();
    }

    public DestructibleWall readDestructibleWall(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        int hp = object.get("hp").getAsInt();
        DestructibleWall wall = this.gameObjectFactory.createDestructibleWall(position)
                .objectId(id)
                .build();
        wall.healthComponent().health(hp);
        return wall;
    }

    public void updateDestructibleWall(DestructibleWall existing, JsonObject update) {
        if (update.has("position")) {
            BoundingBox updated = readBoundingBox(update.get("position").getAsJsonArray());
            existing.hitbox(updated);
        }
        if (update.has("hp")) {
            existing.healthComponent().health(update.get("hp").getAsInt());
        }
    }

    public Tank readTank(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        DoublePair velocity = readDoublePair(object.getAsJsonArray("velocity"));
        int hp = object.get("hp").getAsInt();
        Set<TankAspect> aspects = readTankAspects(object.getAsJsonArray("powerups"));
        Tank tank = this.gameObjectFactory.createTank(position).objectId(id)
                .velocity(velocity)
                .activePowerups(aspects)
                .build();
        tank.healthComponent().health(hp);
        return tank;
    }

    public void updateTank(Tank existing, JsonObject update) {
        if (update.has("position")) {
            DoublePair updated = readDoublePair(update.get("position").getAsJsonArray());
            Hitbox existingHitbox = existing.hitbox();
            existing.hitbox(existingHitbox.reCentered(updated));
        }
        if (update.has("velocity")) {
            DoublePair updated = readDoublePair(update.get("velocity").getAsJsonArray());
            existing.velocity(updated);
        }
        if (update.has("hp")) {
            existing.healthComponent().health(update.get("hp").getAsInt());
        }
        if (update.has("powerups")) {
            Set<TankAspect> aspects = readTankAspects(update.getAsJsonArray("powerups"));
            existing.powerups().clear();
            existing.powerups().addAll(aspects);
        }
    }

    public Bullet readBullet(String id, JsonObject object) {
        String tankId = object.get("tank_id").getAsString();
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        DoublePair velocity = readDoublePair(object.getAsJsonArray("velocity"));
        int damage = object.get("damage").getAsInt();
        return this.gameObjectFactory.createBullet(position)
                .objectId(id)
                .tankId(tankId)
                .velocity(velocity)
                .damage(damage)
                .build();
    }

    public void updateBullet(Bullet existing, JsonObject update) {
        if (update.has("position")) {
            DoublePair updated = readDoublePair(update.get("position").getAsJsonArray());
            Hitbox existingHitbox = existing.hitbox();
            existing.hitbox(existingHitbox.reCentered(updated));
        }
        if (update.has("velocity")) {
            DoublePair updated = readDoublePair(update.get("velocity").getAsJsonArray());
            existing.velocity(updated);
        }
        if (update.has("damage")) {
            int damage = update.get("damage").getAsInt();
            existing.damage(damage);
        }
    }

    public Set<TankAspect> readTankAspects(JsonArray array) {
        if (array.size() == 0) {
            return Collections.emptySet();
        }
        return array.asList().stream()
                .map(JsonElement::getAsString)
                .map(TankAspect::valueOf)
                .collect(Collectors.toSet());
    }

    public DoublePair readDoublePair(JsonArray jsonArray) {
        if (jsonArray.size() != 2) {
            throw new IllegalArgumentException("Invalid array size: " + jsonArray);
        }
        double x = jsonArray.get(0).getAsDouble();
        double y = jsonArray.get(1).getAsDouble();
        return new DoublePair(x, y);
    }

    public BoundingBox readBoundingBox(JsonArray jsonArray) {
        if (jsonArray.size() != 4) {
            throw new IllegalArgumentException("Invalid array size: " + jsonArray);
        }
        DoublePair bottomLeft = readDoublePair(jsonArray.get(1).getAsJsonArray());
        DoublePair topRight = readDoublePair(jsonArray.get(3).getAsJsonArray());
        return BoundingBox.ofCorners(bottomLeft, topRight);
    }

}

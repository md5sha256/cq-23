package com.codequest23.util;

import com.codequest23.model.TankAspect;
import com.codequest23.model.Boundary;
import com.codequest23.model.Bullet;
import com.codequest23.model.DestructibleWall;
import com.codequest23.model.Powerup;
import com.codequest23.model.Tank;
import com.codequest23.model.Wall;
import com.codequest23.model.component.BoundingBox;
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

    private Powerup readPowerup(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.get("position").getAsJsonArray());
        TankAspect aspect = TankAspect.valueOf(object.get("powerup_type").getAsString());
        return this.gameObjectFactory.createPowerup(position)
                .objectId(id)
                .tankAspect(aspect)
                .build();
    }

    private Boundary readBoundary(String id, JsonObject object) {
        BoundingBox boundingBox = readBoundingBox(object.get("position").getAsJsonArray());
        return new Boundary(id, boundingBox);
    }

    private Wall readWall(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        return this.gameObjectFactory.createWall(position).objectId(id).build();
    }

    private DestructibleWall readDestructibleWall(String id, JsonObject object) {
        DoublePair position = readDoublePair(object.getAsJsonArray("position"));
        int hp = object.get("hp").getAsInt();
        DestructibleWall wall = this.gameObjectFactory.createDestructibleWall(position)
                .objectId(id)
                .build();
        wall.healthComponent().health(hp);
        return wall;
    }

    private Tank readTank(String id, JsonObject object) {
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

    public Set<TankAspect> readTankAspects(JsonArray array) {
        if (array.size() == 0) {
            return Collections.emptySet();
        }
        return array.asList().stream()
                .map(JsonElement::getAsString)
                .map(TankAspect::valueOf)
                .collect(Collectors.toSet());
    }

    private DoublePair readDoublePair(JsonArray jsonArray) {
        if (jsonArray.size() != 2) {
            throw new IllegalArgumentException("Invalid array size: " + jsonArray);
        }
        double x = jsonArray.get(0).getAsDouble();
        double y = jsonArray.get(1).getAsDouble();
        return new DoublePair(x, y);
    }

    private BoundingBox readBoundingBox(JsonArray jsonArray) {
        if (jsonArray.size() != 4) {
            throw new IllegalArgumentException("Invalid array size: " + jsonArray);
        }
        DoublePair bottomLeft = readDoublePair(jsonArray.get(0).getAsJsonArray());
        DoublePair topRight = readDoublePair(jsonArray.get(3).getAsJsonArray());
        return BoundingBox.ofCorners(bottomLeft, topRight);
    }

}

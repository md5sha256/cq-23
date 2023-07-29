package com.codequest23;

import com.codequest23.events.ChangeEvent;
import com.codequest23.events.EventOrchestrator;
import com.codequest23.logic.BulletDodgeResponse;
import com.codequest23.logic.BulletListener;
import com.codequest23.logic.ChaseEnemyResponse;
import com.codequest23.logic.ChasePowerupResponse;
import com.codequest23.logic.ClosingWallResponse;
import com.codequest23.logic.DirectShotStrategy;
import com.codequest23.logic.ResponseGenerator;
import com.codequest23.logic.ShootEnemyResponse;
import com.codequest23.message.Action;
import com.codequest23.message.MoveAction;
import com.codequest23.message.OutboundMessage;
import com.codequest23.model.Bullet;
import com.codequest23.model.ClosingBoundary;
import com.codequest23.model.DestructibleWall;
import com.codequest23.model.GameMap;
import com.codequest23.model.GameObject;
import com.codequest23.model.Powerup;
import com.codequest23.model.Tank;
import com.codequest23.model.component.Hitbox;
import com.codequest23.util.DefaultGameObjectFactory;
import com.codequest23.util.DoublePair;
import com.codequest23.util.GameObjectFactory;
import com.codequest23.util.MathUtil;
import com.codequest23.util.Serializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Game {
    private final EventOrchestrator eventOrchestrator = new EventOrchestrator();
    private final GameObjectFactory gameObjectFactory = new DefaultGameObjectFactory();
    private final Serializer serializer = new Serializer(this.gameObjectFactory);
    private final GameMap gameMap = new GameMap();
    private String tankId;
    private Map<String, JsonObject> objects;
    private double width;
    private double height;
    private OutboundMessage lastOutboundMessage;

    public Game() {
        readModern();
    }

    public OutboundMessage lastOutboundMessage() {
        return this.lastOutboundMessage;
    }

    private void readModern() {
        // Read the tank ID message
        JsonElement tankIdMessage = Comms.readMessage();
        String friendlyTankId = tankIdMessage.getAsJsonObject()
                .getAsJsonObject("message")
                .get("your-tank-id")
                .getAsString();

        String enemyTankId = tankIdMessage.getAsJsonObject()
                .getAsJsonObject("message")
                .get("enemy-tank-id")
                .getAsString();

        // Read the initialization messages until the end signal is received
        JsonElement nextInitMessage = Comms.readMessage();
        while (!nextInitMessage.isJsonPrimitive()
                || !nextInitMessage.getAsString().equals(Comms.END_INIT_SIGNAL)) {
            JsonObject objectInfo =
                    nextInitMessage
                            .getAsJsonObject()
                            .getAsJsonObject("message")
                            .getAsJsonObject("updated_objects");

            // Store the object information in the map
            for (Map.Entry<String, JsonElement> entry : objectInfo.entrySet()) {
                String objectId = entry.getKey();
                JsonObject objectData = entry.getValue().getAsJsonObject();
                GameObject gameObject = readNewObject(objectId, objectData);
                if (gameObject != null) {
                    this.gameMap.addObject(gameObject);
                }
                if (gameObject instanceof ClosingBoundary closingBoundary) {
                    this.gameMap.setBoundary(closingBoundary);
                }
            }

            nextInitMessage = Comms.readMessage();
        }
        this.gameMap.setFriendlyTank((Tank) this.gameMap.getObject(friendlyTankId));
        this.gameMap.setEnemyTank((Tank) this.gameMap.getObject(enemyTankId));
        this.eventOrchestrator.registerListener(ChangeEvent.class, new BulletListener());
    }

    public boolean canShootThisTick() {
        return this.lastOutboundMessage == null || this.lastOutboundMessage.action() != Action.SHOOT;
    }

    public boolean readNextTurnData() {
        // Read the next turn message
        JsonElement currentTurnMessage = Comms.readMessage();

        if (currentTurnMessage.isJsonPrimitive()
                && currentTurnMessage.getAsString().equals(Comms.END_SIGNAL)) {
            return false;
        }

        Collection<String> deletedObjectIds = new ArrayList<>();
        // Delete objects that have been removed
        for (JsonElement deletedObjectId : currentTurnMessage
                .getAsJsonObject()
                .getAsJsonObject("message")
                .getAsJsonArray("deleted_objects")) {
            String id = deletedObjectId.getAsString();
            deletedObjectIds.add(id);
        }

        Map<String, GameObject> updatedGameObjects = new HashMap<>();
        Map<String, GameObject> addedGameObjects = new HashMap<>();
        // Update objects with new or updated data
        JsonObject updatedObjects = currentTurnMessage.getAsJsonObject()
                .getAsJsonObject("message")
                .getAsJsonObject("updated_objects");

        for (Map.Entry<String, JsonElement> entry : updatedObjects.entrySet()) {
            JsonObject objectData = entry.getValue().getAsJsonObject();
            if (this.gameMap.containsObject(entry.getKey())) {
                GameObject updated = updateExistingObject(entry.getKey(), objectData);
                updatedGameObjects.put(entry.getKey(), updated);
            } else {
                GameObject newObject = readNewObject(entry.getKey(), objectData);
                addedGameObjects.put(entry.getKey(), newObject);
            }
        }

        ChangeEvent changeEvent = new ChangeEvent(this, deletedObjectIds, updatedGameObjects, addedGameObjects);
        this.eventOrchestrator.callEvent(changeEvent);

        for (String deleted : deletedObjectIds) {
            this.gameMap.removeObject(deleted);
        }
        for (GameObject gameObject : addedGameObjects.values()) {
            this.gameMap.addObject(gameObject);
        }
        return true;
    }


    public void respondToTurn() {
        // Write your code here... For demonstration, this bot just shoots randomly every turn.
        long timeNow = System.nanoTime();
        // Create the message with the shoot angle
        ResponseGenerator wallChecker = new ClosingWallResponse(100);
        OutboundMessage message = ResponseGenerator.chain(
                        new BulletDodgeResponse(50),
                        new ShootEnemyResponse(600),
                        new ChasePowerupResponse(),
                        new ShootEnemyResponse(1000),
                        new ChaseEnemyResponse())
                .generateMessage(this)
                .orElseGet(() -> wallChecker.generateMessage(this).orElse(OutboundMessage.EMPTY_RESPONSE));
        if (message instanceof MoveAction nextMessage
                && lastOutboundMessage instanceof MoveAction lastMessage
                && nextMessage.destination().equals(lastMessage.destination())) {
            // If we are already moving, try to take a shot if possible
            message = new ShootEnemyResponse(1000, new DirectShotStrategy(true))
                    .generateMessage(this)
                    .orElseGet(() -> wallChecker.generateMessage(this).orElse(OutboundMessage.EMPTY_RESPONSE));
        }
        // Send the message
        Comms.postMessage(message.toJson());
        long elapsed = System.nanoTime() - timeNow;
        if (elapsed > TimeUnit.MILLISECONDS.toNanos(100)) {
            System.err.println("Exceeded time limit: " + TimeUnit.NANOSECONDS.toMillis(elapsed));
        }
        this.lastOutboundMessage = message;
    }

    private Powerup findClosestPowerUp() {
        Tank us = (Tank) this.gameMap.getObject(this.tankId);
        DoublePair ourPosition = us.hitbox().centre();
        Map<String, GameObject> powerups = this.gameMap.getObjectsByType(ObjectTypes.POWERUP);
        ClosingBoundary boundary = (ClosingBoundary) this.gameMap.getObjectsByType(ObjectTypes.CLOSING_BOUNDARY).values().iterator().next();
        Hitbox boundaryBoundingBox = boundary.hitbox();
        Comparator<GameObject> distanceComparator = Comparator.comparing(gameObject -> MathUtil.distanceSquared(ourPosition, gameObject.hitbox().centre()));
        return powerups.values().stream()
                .filter(object -> boundaryBoundingBox.intersects(object.hitbox().centre()))
                .sorted(distanceComparator.reversed())
                .map(Powerup.class::cast)
                .findFirst()
                .orElse(null);
    }

    private GameObject readNewObject(String objectId, JsonObject objectData) {
        int type = objectData.get("type").getAsInt();
        ObjectTypes objectType = ObjectTypes.fromId(type);
        return switch (objectType) {
            case TANK -> this.serializer.readTank(objectId, objectData);
            case BULLET -> this.serializer.readBullet(objectId, objectData);
            case WALL -> this.serializer.readWall(objectId, objectData);
            case BOUNDARY -> this.serializer.readBoundary(objectId, objectData);
            case POWERUP -> this.serializer.readPowerup(objectId, objectData);
            case DESTRUCTIBLE_WALL -> this.serializer.readDestructibleWall(objectId, objectData);
            case CLOSING_BOUNDARY -> this.serializer.readClosingBoundary(objectId, objectData);
        };
    }

    private GameObject updateExistingObject(String objectId, JsonObject objectData) {
        int type = objectData.get("type").getAsInt();
        ObjectTypes objectType = ObjectTypes.fromId(type);
        GameObject existing = this.gameMap.getObject(objectId);
        switch (objectType) {
            case TANK -> this.serializer.updateTank((Tank) existing, objectData);
            case BULLET -> this.serializer.updateBullet((Bullet) existing, objectData);
            case DESTRUCTIBLE_WALL -> this.serializer.updateDestructibleWall((DestructibleWall) existing, objectData);
            case CLOSING_BOUNDARY -> this.serializer.updateClosingBoundary((ClosingBoundary) existing, objectData);
        }
        return existing;
    }

    private boolean tryChasePowerup(JsonObject message) {
        Powerup powerup = findClosestPowerUp();
        if (powerup == null) {
            return false;
        }
        DoublePair position = powerup.hitbox().centre();
        JsonArray array = new JsonArray();
        array.add(position.x());
        array.add(position.y());
        message.add("path", array);
        return true;
    }

    private boolean tryChaseEnemyTank(JsonObject message) {
        Map<String, GameObject> tanks = this.gameMap.getObjectsByType(ObjectTypes.TANK);
        Tank us = null;
        Tank other = null;
        for (Map.Entry<String, GameObject> entry : tanks.entrySet()) {
            if (entry.getKey().equals(this.tankId)) {
                us = (Tank) entry.getValue();
            } else {
                other = (Tank) entry.getValue();
            }
        }
        if (us == null || other == null) {
            throw new IllegalStateException("Could not find both tanks");
        }
        DoublePair theirPosition = other.hitbox().centre();
        JsonArray array = new JsonArray();
        array.add(theirPosition.x());
        array.add(theirPosition.y());
        message.add("path", array);
        return true;
    }

    private boolean tryShootEnemyTank(JsonObject message) {
        Map<String, GameObject> tanks = this.gameMap.getObjectsByType(ObjectTypes.TANK);
        Tank us = null;
        Tank other = null;
        for (Map.Entry<String, GameObject> entry : tanks.entrySet()) {
            if (entry.getKey().equals(this.tankId)) {
                us = (Tank) entry.getValue();
            } else {
                other = (Tank) entry.getValue();
            }
        }
        if (us == null || other == null) {
            throw new IllegalStateException("Could not find both tanks");
        }
        DoublePair ourPos = us.hitbox().centre();
        DoublePair theirPos = other.hitbox().centre();
        double distanceSquared = MathUtil.distanceSquared(ourPos, theirPos);
        // FIXME add check for clear path
        if (distanceSquared <= 600 * 600) {
            double angleBetween = MathUtil.angleDegBetween(ourPos, theirPos);
            message.addProperty("shoot", angleBetween);
            return true;
        }
        return false;
    }

    public EventOrchestrator eventOrchestrator() {
        return this.eventOrchestrator;
    }

    public Serializer serializer() {
        return this.serializer;
    }

    public GameObjectFactory gameObjectFactory() {
        return this.gameObjectFactory;
    }

    public GameMap map() {
        return this.gameMap;
    }

}

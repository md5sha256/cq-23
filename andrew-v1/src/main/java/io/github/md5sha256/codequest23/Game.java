package io.github.md5sha256.codequest23;

import com.codequest23.Comms;
import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.logic.BulletDodgeResponse;
import io.github.md5sha256.codequest23.logic.ChaseEnemyResponse;
import io.github.md5sha256.codequest23.logic.ChasePowerupResponse;
import io.github.md5sha256.codequest23.logic.ClosingBoundaryCheck;
import io.github.md5sha256.codequest23.logic.DirectShotStrategy;
import io.github.md5sha256.codequest23.logic.KeepDistanceResponse;
import io.github.md5sha256.codequest23.logic.ResponseGenerator;
import io.github.md5sha256.codequest23.logic.ShootEnemyResponse;
import io.github.md5sha256.codequest23.logic.ShootStrategyChain;
import io.github.md5sha256.codequest23.logic.WallSpacedShotFilter;
import io.github.md5sha256.codequest23.message.Action;
import io.github.md5sha256.codequest23.message.PathAction;
import io.github.md5sha256.codequest23.message.OutboundMessage;
import io.github.md5sha256.codequest23.model.Bullet;
import io.github.md5sha256.codequest23.model.ClosingBoundary;
import io.github.md5sha256.codequest23.model.DestructibleWall;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.GameObject;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.util.DefaultGameObjectFactory;
import io.github.md5sha256.codequest23.util.GameObjectFactory;
import io.github.md5sha256.codequest23.util.Serializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private final GameObjectFactory gameObjectFactory = new DefaultGameObjectFactory();
    private final Serializer serializer = new Serializer(this.gameObjectFactory);
    private final GameMap gameMap = new GameMap();
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

        Map<String, GameObject> addedGameObjects = new HashMap<>();
        // Update objects with new or updated data
        JsonObject updatedObjects = currentTurnMessage.getAsJsonObject()
                .getAsJsonObject("message")
                .getAsJsonObject("updated_objects");

        for (Map.Entry<String, JsonElement> entry : updatedObjects.entrySet()) {
            JsonObject objectData = entry.getValue().getAsJsonObject();
            if (this.gameMap.containsObject(entry.getKey())) {
                updateExistingObject(entry.getKey(), objectData);
            } else {
                GameObject newObject = readNewObject(entry.getKey(), objectData);
                addedGameObjects.put(entry.getKey(), newObject);
            }
        }

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
        // Create the message with the shoot angle
        ResponseGenerator boundaryCheck = new ClosingBoundaryCheck(25);
        OutboundMessage message = ResponseGenerator.chain(
                        boundaryCheck,
                        new BulletDodgeResponse(50),
                        new ShootEnemyResponse(400, ShootStrategyChain.chain(new WallSpacedShotFilter(60), new DirectShotStrategy(true))),
                        new KeepDistanceResponse(100),
                        new ChasePowerupResponse(),
                        new ShootEnemyResponse(600),
                        new ChaseEnemyResponse())
                .generateMessage(this)
                .orElse(OutboundMessage.EMPTY_RESPONSE);
        if (message instanceof PathAction nextMessage
                && lastOutboundMessage instanceof PathAction lastMessage
                && nextMessage.destination().equals(lastMessage.destination())) {
            // If we are already moving, try to take a shot if possible
            message = new ShootEnemyResponse(600, new DirectShotStrategy(true))
                    .generateMessage(this)
                    .orElseGet(() -> boundaryCheck.generateMessage(this).orElse(OutboundMessage.EMPTY_RESPONSE));
        }
        // Send the message
        Comms.postMessage(message.toJson());
        this.lastOutboundMessage = message;
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

    private void updateExistingObject(String objectId, JsonObject objectData) {
        int type = objectData.get("type").getAsInt();
        ObjectTypes objectType = ObjectTypes.fromId(type);
        GameObject existing = this.gameMap.getObject(objectId);
        switch (objectType) {
            case TANK -> this.serializer.updateTank((Tank) existing, objectData);
            case BULLET -> this.serializer.updateBullet((Bullet) existing, objectData);
            case DESTRUCTIBLE_WALL -> this.serializer.updateDestructibleWall((DestructibleWall) existing, objectData);
            case CLOSING_BOUNDARY -> this.serializer.updateClosingBoundary((ClosingBoundary) existing, objectData);
        }
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

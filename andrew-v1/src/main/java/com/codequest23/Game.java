package com.codequest23;

import com.codequest23.events.ChangeEvent;
import com.codequest23.events.EventOrchestrator;
import com.codequest23.logic.BulletListener;
import com.codequest23.model.ClosingBoundary;
import com.codequest23.model.GameMap;
import com.codequest23.model.GameObject;
import com.codequest23.model.Powerup;
import com.codequest23.model.Tank;
import com.codequest23.model.component.BoundingBox;
import com.codequest23.model.component.ShapeComponent;
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
import java.util.Random;

public class Game {
    private String tankId;
    private Map<String, JsonObject> objects;
    private double width;
    private double height;
    private JsonElement currentTurnMessage;

    private final EventOrchestrator eventOrchestrator = new EventOrchestrator();

    private final GameObjectFactory gameObjectFactory = new DefaultGameObjectFactory();
    private final Serializer serializer = new Serializer(this.gameObjectFactory);

    private final GameMap gameMap = new GameMap();

    public Game() {
        readModern();
    }

    private void readModern() {
        // Read the tank ID message
        JsonElement tankIdMessage = Comms.readMessage();
        this.tankId =
                tankIdMessage
                        .getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("your-tank-id")
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
                int type = objectData.get("type").getAsInt();
                ObjectTypes objectType = ObjectTypes.fromId(type);
                GameObject gameObject = switch (objectType) {
                    case TANK -> this.serializer.readTank(objectId, objectData);
                    case BULLET -> this.serializer.readBullet(objectId, objectData);
                    case WALL -> this.serializer.readWall(objectId, objectData);
                    case BOUNDARY -> this.serializer.readBoundary(objectId, objectData);
                    case POWERUP -> this.serializer.readPowerup(objectId, objectData);
                    case DESTRUCTIBLE_WALL -> this.serializer.readDestructibleWall(objectId, objectData);
                    case CLOSING_BOUNDARY -> this.serializer.readClosingBoundary(objectId, objectData);
                };
                if (gameObject != null) {
                    this.gameMap.addObject(gameObject);
                }
            }

            nextInitMessage = Comms.readMessage();
        }
        this.eventOrchestrator.registerListener(ChangeEvent.class, new BulletListener());
    }

    private void readLegacy() {
        // Read the tank ID message
        JsonElement tankIdMessage = Comms.readMessage();
        this.tankId =
                tankIdMessage
                        .getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("your-tank-id")
                        .getAsString();

        this.currentTurnMessage = null;
        this.objects = new HashMap<>();

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
                objects.put(objectId, objectData);
            }

            nextInitMessage = Comms.readMessage();
        }

        // Find the boundaries to determine the map size
        double biggestX = Double.MIN_VALUE;
        double biggestY = Double.MIN_VALUE;

        for (JsonObject gameObject : objects.values()) {
            int objectType = gameObject.get("type").getAsInt();
            if (objectType == ObjectTypes.BOUNDARY.getValue()) {
                // Parse the position array
                double[][] position = GameUtils.parsePosition(gameObject.get("position").getAsJsonArray());
                for (double[] singlePosition : position) {
                    // Update the biggestX and biggestY values
                    biggestX = Math.max(biggestX, singlePosition[0]);
                    biggestY = Math.max(biggestY, singlePosition[1]);
                }
            }
        }

        // Set the width and height of the map
        this.width = biggestX;
        this.height = biggestY;
    }

    public boolean readNextTurnData() {
        // Read the next turn message
        this.currentTurnMessage = Comms.readMessage();

        if (this.currentTurnMessage.isJsonPrimitive()
                && this.currentTurnMessage.getAsString().equals(Comms.END_SIGNAL)) {
            return false;
        }

        Collection<String> deletedObjectIds = new ArrayList<>();
        // Delete objects that have been removed
        for (JsonElement deletedObjectId :
                this.currentTurnMessage
                        .getAsJsonObject()
                        .getAsJsonObject("message")
                        .getAsJsonArray("deleted_objects")) {

            String id = deletedObjectId.getAsString();
            deletedObjectIds.add(id);
        }

        Map<String, JsonObject> updatedGameObjects = new HashMap<>();
        // Update objects with new or updated data
        JsonObject updatedObjects =
                this.currentTurnMessage
                        .getAsJsonObject()
                        .getAsJsonObject("message")
                        .getAsJsonObject("updated_objects");
        for (Map.Entry<String, JsonElement> entry : updatedObjects.entrySet()) {
            JsonObject objectData = entry.getValue().getAsJsonObject();
            updatedGameObjects.put(entry.getKey(), objectData);
            if (objectData.get("type").getAsInt() == ObjectTypes.POWERUP.getValue()) {
                Powerup powerup = this.serializer.readPowerup(entry.getKey(), objectData);
                this.gameMap.addObject(powerup);
            }
        }

        ChangeEvent changeEvent = new ChangeEvent(this, deletedObjectIds, updatedGameObjects);
        this.eventOrchestrator.callEvent(changeEvent);

        for (String deleted : deletedObjectIds) {
            this.gameMap.removeObject(deleted);
        }
        return true;
    }

    public void respondToTurn() {
        // Write your code here... For demonstration, this bot just shoots randomly every turn.

        // Create the message with the shoot angle
        JsonObject message = new JsonObject();
        if (!tryChasePowerup(message) && !tryShootEnemyTank(message)) {
            tryChaseEnemyTank(message);
        }
        // Send the message
        Comms.postMessage(message);
    }

    private Powerup findClosestPowerUp() {
        Tank us = (Tank) this.gameMap.getObject(this.tankId);
        DoublePair ourPosition = us.shapeComponent().centre();
        Map<String, GameObject> powerups = this.gameMap.getObjectsByType(ObjectTypes.POWERUP);
        ClosingBoundary boundary = (ClosingBoundary) this.gameMap.getObjectsByType(ObjectTypes.CLOSING_BOUNDARY).values().iterator().next();
        ShapeComponent boundaryBoundingBox = boundary.shapeComponent();
        Comparator<GameObject> distanceComparator = Comparator.comparing(gameObject -> MathUtil.distanceSquared(ourPosition, gameObject.shapeComponent().centre()));
        return powerups.values().stream()
                .filter(object -> boundaryBoundingBox.intersects(object.shapeComponent().centre()))
                .sorted(distanceComparator.reversed())
                .map(Powerup.class::cast)
                .findFirst()
                .orElse(null);
    }

    private boolean tryChasePowerup(JsonObject message) {
        Powerup powerup = findClosestPowerUp();
        if (powerup == null) {
            return false;
        }
        DoublePair position = powerup.shapeComponent().centre();
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
        DoublePair theirPosition = other.shapeComponent().centre();
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
        DoublePair ourPos = us.shapeComponent().centre();
        DoublePair theirPos = other.shapeComponent().centre();
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

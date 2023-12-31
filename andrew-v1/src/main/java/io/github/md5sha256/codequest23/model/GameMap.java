package io.github.md5sha256.codequest23.model;

import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.component.BoundingBox;
import io.github.md5sha256.codequest23.model.component.CircularHitbox;
import io.github.md5sha256.codequest23.model.component.Hitbox;
import io.github.md5sha256.codequest23.util.DoublePair;
import io.github.md5sha256.codequest23.util.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GameMap {

    private final Map<String, GameObject> objects = new HashMap<>();
    private final Map<ObjectTypes, Map<String, GameObject>> typeCache = new EnumMap<>(ObjectTypes.class);

    private Tank friendlyTank;
    private Tank enemyTank;

    private ClosingBoundary boundary;

    public GameMap() {
        for (ObjectTypes type : ObjectTypes.values()) {
            typeCache.put(type, new HashMap<>());
        }
    }

    public Tank friendlyTank() {
        return this.friendlyTank;
    }

    public void setFriendlyTank(Tank friendlyTank) {
        this.friendlyTank = friendlyTank;
    }

    public Tank enemyTank() {
        return this.enemyTank;
    }

    public void setEnemyTank(Tank enemyTank) {
        this.enemyTank = enemyTank;
    }

    public ClosingBoundary boundary() {
        return this.boundary;
    }

    public void setBoundary(ClosingBoundary boundary) {
        this.boundary = boundary;
    }

    public Map<String, GameObject> getObjectsByType(ObjectTypes type) {
        return Collections.unmodifiableMap(this.typeCache.get(type));
    }

    public Stream<GameObject> streamObjectsByType(ObjectTypes types) {
        return this.typeCache.get(types).values().stream();
    }

    public Stream<GameObject> streamObjects() {
        return this.objects.values().stream();
    }

    public Stream<GameObject> streamWallObjects() {
        Collection<GameObject> gameObjects = new ArrayList<>(this.typeCache.get(ObjectTypes.WALL).values());
        gameObjects.addAll(this.typeCache.get(ObjectTypes.DESTRUCTIBLE_WALL).values());
        return gameObjects.stream();
    }

    public boolean isWithinBounds(GameObject gameObject) {
        return this.boundary.hitbox().intersects(gameObject.hitbox().centre());
    }

    public <T extends GameObject> Comparator<T> closestTo(DoublePair point) {
        return Comparator.comparingDouble(object -> MathUtil.distanceSquared(point, object.hitbox().centre()));
    }

    public <T extends GameObject> Predicate<T> inLineOfSight(DoublePair origin, DoublePair dest) {
        double gradient = MathUtil.gradient(origin, dest);
        BoundingBox checkBox = BoundingBox.ofCorners(origin, dest);
        // y = mx + c, c = y - mx
        double yIntercept = origin.y() - gradient * origin.x();
        return (gameObject) -> {
            Hitbox hitbox = gameObject.hitbox();
            BoundingBox boundingBox = hitbox.asBoundingBox();
            if (!checkBox.intersects(hitbox.centre())) {
                return false;
            }
            return boundingBox.intersectsLine(gradient, yIntercept);
        };

    }

    public <T extends GameObject> Predicate<T> inLineOfSight(DoublePair origin, double angleDeg, double distance) {
        double gradient = Math.tan(Math.toDegrees(angleDeg));
        Hitbox checkBox = new CircularHitbox(origin, distance);
        // y = mx + c, c = y - mx
        double yIntercept = origin.y() - gradient * origin.x();
        return (gameObject) -> {
            Hitbox hitbox = gameObject.hitbox();
            BoundingBox boundingBox = hitbox.asBoundingBox();
            if (!checkBox.intersects(hitbox.centre())) {
                return false;
            }
            return boundingBox.intersectsLine(gradient, yIntercept);
        };

    }

    public <T extends GameObject> Predicate<T> inLineOfSight(DoublePair origin, DoublePair dest, double width) {
        double gradient = MathUtil.gradient(origin, dest);
        // y = mx + c, c = y - mx
        double yIntercept = origin.y() - gradient * origin.x();
        return (gameObject) -> {
            BoundingBox boundingBox = gameObject.hitbox().asBoundingBox();
            return boundingBox.intersectsLine(gradient, yIntercept)
                    || boundingBox.intersectsLine(gradient, yIntercept - width)
                    || boundingBox.intersectsLine(gradient, yIntercept + width);
        };

    }

    public boolean containsObject(String id) {
        return this.objects.containsKey(id);
    }

    public GameObject getObject(String id) {
        return this.objects.get(id);
    }

    public void addObject(GameObject gameObject) {
        this.objects.put(gameObject.objectId(), gameObject);
        this.typeCache.get(gameObject.objectType()).put(gameObject.objectId(), gameObject);
    }

    public void removeObject(String id) {
        GameObject object = this.objects.remove(id);
        if (object != null) {
            this.typeCache.get(object.objectType()).remove(id);
        }
    }


}

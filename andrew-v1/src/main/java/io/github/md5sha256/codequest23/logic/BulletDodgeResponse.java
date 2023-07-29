package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.message.MoveAction;
import io.github.md5sha256.codequest23.message.OutboundMessage;
import io.github.md5sha256.codequest23.model.Bullet;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.util.DoublePair;
import io.github.md5sha256.codequest23.util.MathUtil;
import io.github.md5sha256.codequest23.util.Trajectory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BulletDodgeResponse implements ResponseGenerator {

    private final int checkRadiusSquared;
    private ResponseGenerator next;

    public BulletDodgeResponse(int checkRadius) {
        this.checkRadiusSquared = checkRadius * checkRadius;
    }

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        DoublePair velocity = us.velocity();
        DoublePair pos = us.hitbox().centre();
        List<Bullet> nearbyBullets = map.streamObjectsByType(ObjectTypes.BULLET)
                .filter(object -> MathUtil.distanceSquared(pos, object.hitbox().centre()) <= this.checkRadiusSquared)
                .sorted(Comparator.comparingDouble(object -> MathUtil.distanceSquared(pos, object.hitbox().centre())))
                .map(Bullet.class::cast)
                .toList();
        Trajectory trajectory = Trajectory.fromVelocity(pos, velocity);
        boolean changed = false;
        for (Bullet bullet : nearbyBullets) {
            DoublePair bulletVelocity = bullet.velocity();
            Trajectory bulletTrajectory = Trajectory.fromVelocity(bullet.hitbox().centre(), bulletVelocity);
            Optional<DoublePair> intersect = trajectory.intercept(bulletTrajectory);
            if (intersect.isEmpty()) {
                continue;
            }
            DoublePair intersectPoint = intersect.get();
            if (MathUtil.distanceSquared(pos, intersectPoint) > this.checkRadiusSquared) {
                continue;
            }
            double velocityGradient = bulletVelocity.y() / bulletVelocity.x();
            double negativeReciprocal = -1 / velocityGradient;
            // y = mx + c
            // 0 = mx + c
            // c = -mx
            double yIntercept = -(negativeReciprocal * intersectPoint.x());
            trajectory = new Trajectory(negativeReciprocal, yIntercept);
            changed = true;
            break;
        }
        if (!changed) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        // move 1 unit of distance away
        double gradient = trajectory.gradient();
        double yIntercept = trajectory.yIntercept();
        final double len = 10;
        double a = gradient * gradient + 1;
        double b = 2 * gradient * yIntercept;
        double c = yIntercept * yIntercept - Math.sqrt(len);
        double root = Math.sqrt(b * b - 4 * a * c);
        double x1 = (-2 * b + root) / (2 * a);
        DoublePair target;
        if (x1 < 0 && root != 0) {
            double x2 = (-2 * b - root) / (2 * a);
            target = new DoublePair(x2, trajectory.y(x2));
        } else {
            target = new DoublePair(x1, trajectory.y(x1));
        }
        if (Double.isNaN(target.x()) || Double.isNaN(target.y())) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        return Optional.of(new MoveAction(target));
    }
}

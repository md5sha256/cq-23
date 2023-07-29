package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.ObjectTypes;
import com.codequest23.message.MoveAction;
import com.codequest23.message.OutboundMessage;
import com.codequest23.model.Bullet;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;
import com.codequest23.util.DoublePair;
import com.codequest23.util.MathUtil;
import com.codequest23.util.Trajectory;

import java.util.List;
import java.util.Optional;

public class BulletDodgeResponse implements ResponseGenerator {

    private ResponseGenerator next;

    private final int checkRadiusSquared;

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
        }
        if (!changed) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        // move 1 unit of distance away
        double gradient = trajectory.gradient();
        double yIntercept = trajectory.yIntercept();
        double a = gradient * gradient + 1;
        double b = 2 * gradient * yIntercept;
        double c = yIntercept * yIntercept - 1;
        double root = Math.sqrt(b * b - 4 * a * c);
        double x1 = (-2 * b + root) / (2 * a);
        DoublePair target;
        if (x1 < 0 && root != 0) {
            double x2 = (-2 * b - root) / (2 * a);
            target = new DoublePair(x2, trajectory.y(x2));
        } else {
            target = new DoublePair(x1, trajectory.y(x1));
        }
        return Optional.of(new MoveAction(target));
    }
}

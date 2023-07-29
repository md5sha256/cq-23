package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.MoveAction;
import com.codequest23.message.OutboundMessage;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;
import com.codequest23.model.component.BoundingBox;
import com.codequest23.util.DoublePair;
import com.codequest23.util.MathUtil;

import java.util.Optional;

public class ClosingWallResponse implements ResponseGenerator {

    private final int minimumDistanceSquared;
    private ResponseGenerator next;

    public ClosingWallResponse(int minimumDistance) {
        this.minimumDistanceSquared = minimumDistance * minimumDistance;
    }

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        DoublePair pos = us.hitbox().centre();
        BoundingBox boundary = map.boundary().hitbox().asBoundingBox();
        DoublePair bottom = new DoublePair(pos.x(), boundary.minY());
        DoublePair top = new DoublePair(pos.x(), boundary.maxY());
        DoublePair left = new DoublePair(boundary.minX(), pos.y());
        DoublePair right = new DoublePair(boundary.maxX(), pos.y());

        if (MathUtil.distanceSquared(pos, bottom) < this.minimumDistanceSquared
        || MathUtil.distanceSquared(pos, top) < this.minimumDistanceSquared
        || MathUtil.distanceSquared(pos, left) < this.minimumDistanceSquared
        || MathUtil.distanceSquared(pos, right) < this.minimumDistanceSquared) {
            return Optional.of(new MoveAction(boundary.centre()));
        }
        return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
    }
}

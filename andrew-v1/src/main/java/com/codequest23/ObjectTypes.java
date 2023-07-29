package com.codequest23;

public enum ObjectTypes {
  TANK(1),
  BULLET(2),
  WALL(3),
  DESTRUCTIBLE_WALL(4),
  BOUNDARY(5),
  CLOSING_BOUNDARY(6),
  POWERUP(7);

  private final int value;

  ObjectTypes(int value) {
    this.value = value;
  }

  public static ObjectTypes fromId(int id) {
    return switch (id) {
      case 1 -> TANK;
      case 2 -> BULLET;
      case 3 -> WALL;
      case 4 -> DESTRUCTIBLE_WALL;
      case 5 -> BOUNDARY;
      case 6 -> CLOSING_BOUNDARY;
      case 7 -> POWERUP;
      default -> throw new IllegalArgumentException("Invalid id: " + id);
    };
  }

  public int getValue() {
    return value;
  }
}

package org.example.gateway.model;

import lombok.Data;

@Data
public class MasterCard {

  private String id;

  private String name;

  private String uri;

  private Integer power;

  public MasterCard() {
  }

  public MasterCard(String id, String name, String uri, Integer power) {
    this.id = id;
    this.name = name;
    this.uri = uri;
    this.power = power;
  }

  public MasterCard(String id, String name) {
    this.id = id;
    this.name = name;
  }
}

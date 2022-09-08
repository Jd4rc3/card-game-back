package org.example.gateway.model;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Integer getPower() {
    return power;
  }

  public void setPower(Integer power) {
    this.power = power;
  }
}

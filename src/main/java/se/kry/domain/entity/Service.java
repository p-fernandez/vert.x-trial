package se.kry.domain.entity;

import io.vertx.core.json.JsonObject;

public class Service {
  private Integer id = 0;
  private String uri;
  private String name;
  private String status;
  private String creation;

  public Service(String uri) {
    this.uri = uri;
    this.name = null;
    this.status = null;
    this.creation = null;
  }

  public Service(int id, String uri, String name, String status, String creation) {
    this.id = id;
    this.uri = uri;
    this.name = name;
    this.status = status;
    this.creation = creation;
  }

  public Service(String uri, String name, String status, String creation) {
    this.uri = uri;
    this.name = name;
    this.status = status;
    this.creation = creation;
  }

  public Service(JsonObject jsonObject) {
    this.id = jsonObject.getInteger("id");
    this.uri = jsonObject.getString("uri");
    this.name = jsonObject.getString("name");
    this.status = jsonObject.getString("status");
    this.creation = jsonObject.getString("creation");
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreation() {
    return creation;
  }

  public void setCreation(String creation) {
    this.creation = creation;
  }
}

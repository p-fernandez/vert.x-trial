package se.kry.domain.entities;

import io.vertx.core.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Service {
  private Integer id = 0;
  private String url;
  private String name;
  private String status;
  private String creation;

  public Service(String url) {
    this.url = url;
    this.name = null;
    this.status = null;
    this.creation = null;
  }

  public Service(int id, String url, String name, String status, String creation) {
    this.id = id;
    this.url = url;
    this.name = name;
    this.status = status;
    this.creation = creation;
  }

  public Service(String url, String name, String status, String creation) {
    this.url = url;
    this.name = name;
    this.status = status;
    this.creation = creation;
  }

  public Service(JsonObject jsonObject) {
    setId(jsonObject.getInteger("id"));
    setUrl(jsonObject.getString("url"));
    setName(jsonObject.getString("name"));
    setStatus(jsonObject.getString("status"));
    setCreation(jsonObject.getString("creation"));
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id != null ? id : 0;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name != null ? name : "";
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status != null ? status : "UNKNOWN";
  }

  public String getCreation() {
    return creation;
  }

  public void setCreation(String creation) {
    this.creation = creation != null
      ? creation
      : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }

  public JsonObject toJsonObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", this.getId());
    jsonObject.put("url", this.getUrl());
    jsonObject.put("name", this.getName());
    jsonObject.put("status", this.getStatus());
    jsonObject.put("creation", this.getCreation());

    return jsonObject;
  }
}

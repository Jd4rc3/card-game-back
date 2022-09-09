package com.example.appservice;

import co.com.sofka.domain.generic.DomainEvent;
import com.example.appservice.generic.StoredEvent;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class GsonEventSerializer implements StoredEvent.EventSerializer {

  @Override
  public <T extends DomainEvent> T deserialize(String aSerialization, Class<?> aType) {
    return (T) new Gson().fromJson(aSerialization, aType);
  }

  @Override
  public String serialize(DomainEvent object) {
    return new Gson().toJson(object);
  }
}

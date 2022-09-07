package org.example.game.values;

import co.com.sofka.domain.generic.Identity;

public class MasterCardId extends Identity {

  public MasterCardId(String uuid) {
    super(uuid);
  }

  public MasterCardId() {
  }

  public static MasterCardId of(String uuid) {
    return new MasterCardId(uuid);
  }
}

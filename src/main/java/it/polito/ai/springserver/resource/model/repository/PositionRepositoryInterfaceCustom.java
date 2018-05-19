package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Position;
import org.springframework.stereotype.Service;

public interface PositionRepositoryInterfaceCustom {
  Position getUserLatestPosition(long userid);
}

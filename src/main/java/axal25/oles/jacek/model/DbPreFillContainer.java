package axal25.oles.jacek.model;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class DbPreFillContainer {
    ApplicationEntity application;
    ReleaseEntity release;
    TicketEntity ticket;
}

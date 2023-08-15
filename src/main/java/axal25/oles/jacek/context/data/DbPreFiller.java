package axal25.oles.jacek.context.data;

import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import axal25.oles.jacek.model.DbPreFillContainer;
import axal25.oles.jacek.service.application.ApplicationService;
import axal25.oles.jacek.service.release.ReleaseService;
import axal25.oles.jacek.service.ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DbPreFiller implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private TicketService ticketService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        prefillInMemDb();
    }

    private void prefillInMemDb() {
        prefillInMemDb(generateContainer());
    }

    void prefillInMemDb(DbPreFillContainer container) {
        applicationService.addApplication(container.getApplication());
        releaseService.addRelease(container.getRelease());
        ticketService.addTicket(container.getTicket());
    }

    public DbPreFillContainer generateContainer() {
        return DbPreFillContainer.builder()
                .application(ApplicationEntityFactory
                        .produce("generateContainer",
                                DbPreFiller.class,
                                null,
                                EntityFactory.IdGenerateMode.NULL))
                .release(ReleaseEntityFactory
                        .produce("generateContainer",
                                DbPreFiller.class,
                                null,
                                null,
                                EntityFactory.IdGenerateMode.NULL))
                .ticket(TicketEntityFactory
                        .produce("generateContainer",
                                DbPreFiller.class,
                                null,
                                null,
                                null,
                                EntityFactory.IdGenerateMode.NULL))
                .build();
    }
}

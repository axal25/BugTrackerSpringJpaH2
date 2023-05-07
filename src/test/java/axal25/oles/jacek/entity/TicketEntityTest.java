package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class TicketEntityTest {

    public static TicketEntity getDeepCopy(TicketEntity inputTicket) {
        TicketEntity deepCopyTicket = new TicketEntity();

        deepCopyTicket.setId(inputTicket.getId());
        deepCopyTicket.setTitle(inputTicket.getTitle());
        deepCopyTicket.setStatus(inputTicket.getStatus());
        deepCopyTicket.setDescription(inputTicket.getDescription());
        deepCopyTicket.setApplication(inputTicket.getApplication().deepCopy());
        deepCopyTicket.setRelease(inputTicket.getRelease().deepCopy());

        return deepCopyTicket;
    }

    @Test
    public void deepCopy_isEqualToExpected() {
        TicketEntity ticket = TicketEntityFactory.produce(
                "deepCopy_isEqualToExpected",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.RANDOM);

        assertThat(ticket.deepCopy()).isEqualTo(getDeepCopy(ticket));
    }
}

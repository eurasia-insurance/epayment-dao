package test.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.epayment.dao.InvoiceDAO.InvoiceDAOLocal;
import tech.lapsa.epayment.domain.Invoice;
import tech.lapsa.java.commons.function.MyNumbers;

public class InvoiceDAOTest extends ArquillianBaseTestCase {

    @Inject
    private InvoiceDAOLocal dao;

    @Test
    public void createNewTest() {
	final Invoice newEntity = EntitiesHelper.invoice();
	final Invoice entity = dao.save(newEntity);
	assertThat(entity, not(nullValue()));
	assertTrue(MyNumbers.positive(entity.getId()));
    }

    @Test
    public void uniqueNumberCheckTest() {
	Invoice entity = dao.save(EntitiesHelper.invoiceBuilder().withGeneratedNumber().build(dao::isUniqueNumber));
	assertThat(entity, not(nullValue()));
	assertTrue(MyNumbers.positive(entity.getId()));
    }
}

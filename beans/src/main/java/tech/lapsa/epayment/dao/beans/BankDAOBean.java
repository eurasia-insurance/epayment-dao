package tech.lapsa.epayment.dao.beans;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import tech.lapsa.epayment.dao.BankDAO;
import tech.lapsa.epayment.dao.BankDAO.BankDAOLocal;
import tech.lapsa.epayment.dao.BankDAO.BankDAORemote;
import tech.lapsa.epayment.domain.Bank;
import tech.lapsa.epayment.domain.Bank_;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.patterns.dao.NotFound;

@Stateless(name = BankDAO.BEAN_NAME)
public class BankDAOBean extends ABaseDAO<Bank, Integer>
	implements BankDAOLocal, BankDAORemote {

    public BankDAOBean() {
	super(Bank.class);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Bank getByCode(final String code) throws IllegalArgument, NotFound {
	try {
	    return _getByCode(code);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isUniqueCode(final String code) throws IllegalArgument {
	return _isUniqueCode(code);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Bank getByBIN(String bin) throws IllegalArgument, NotFound {
	try {
	    return _getByBIN(bin);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	}
    }

    // PRIVATE

    private Bank _getByCode(final String code) throws IllegalArgumentException, NotFound {
	MyStrings.requireNonEmpty(code, "code");

	final CriteriaBuilder cb = em.getCriteriaBuilder();
	final CriteriaQuery<Bank> cq = cb.createQuery(Bank.class);
	final Root<Bank> root = cq.from(Bank.class);
	cq.select(root) //
		.where(cb.equal(root.get(Bank_.code), code));

	final TypedQuery<Bank> q = em.createQuery(cq);
	return signleResult(q);
    }

    private boolean _isUniqueCode(final String code) {
	try {
	    _getByCode(code);
	    return false;
	} catch (final NotFound e) {
	    return true;
	} catch (final IllegalArgumentException e) {
	    return false;
	}
    }

    private Bank _getByBIN(final String bin) throws IllegalArgumentException, NotFound {
	MyStrings.requireNonEmpty(bin, "bin");

	final CriteriaBuilder cb = em.getCriteriaBuilder();
	final CriteriaQuery<Bank> cq = cb.createQuery(Bank.class);
	final Root<Bank> root = cq.from(Bank.class);

	cq.select(root) //
		.where(cb.isMember(bin, root.get(Bank_.bins)));

	final TypedQuery<Bank> q = em.createQuery(cq);
	return signleResult(q);
    }

}

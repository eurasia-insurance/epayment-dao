package tech.lapsa.epayment.dao.web;

import java.io.Serializable;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import tech.lapsa.epayment.dao.beans.EntityManagerControlBean;

@Named("control")
@RequestScoped
@RolesAllowed("role-admin")
public class ControlCDIBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EntityManagerControlBean bean;

    public String doFlushAndClear() {
	bean.flushAndClear();
	return "home";
    }

    public String doPing() {
	bean.ping();
	return "home";
    }
}

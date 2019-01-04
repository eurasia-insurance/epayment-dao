package test.dao;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Currency;

import com.lapsa.international.localization.LocalizationLanguage;
import com.lapsa.international.phone.PhoneNumber;

import tech.lapsa.epayment.domain.Invoice;
import tech.lapsa.epayment.domain.Invoice.InvoiceBuilder;
import tech.lapsa.epayment.domain.QazkomOrder;
import tech.lapsa.epayment.domain.QazkomOrder.QazkomOrderBuilder;
import tech.lapsa.java.commons.io.MyResources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MyPrivateKeys;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public final class EntitiesHelper {

    private EntitiesHelper() {
    }

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "cert";

    private static X509Certificate merchantCert;
    private static PrivateKey merchantKey;

    static {
	final InputStream storeStream = MyResources.optAsStream(EntitiesHelper.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	final KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	merchantKey = MyPrivateKeys.from(keystore, ALIAS, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can't find key entry"));

	merchantCert = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));
    }

    public static Invoice invoice() {
	return invoiceBuilder() //
		.build();
    }

    public static InvoiceBuilder invoiceBuilder() {
	return Invoice.builder() //
		.withCurrency(Currency.getInstance("KZT")) //
		.withConsumerName("John Bull") //
		.withConsumerPreferLanguage(LocalizationLanguage.RUSSIAN) //
		.withConsumerTaxpayerNumber(TaxpayerNumber.of("800225000319"))
		.withConsumerPhone(PhoneNumber.of("+77019956587")) //
		.withConsumerEmail("john.bull@mail.com") //
		.withExternalId("123") //
		.withItem("Apple iPhone X", 1, 1000d) //
		.withItem("Apple MacBook Pro", 1, 1382.05d) //
	;
    }

    public static QazkomOrder qazkomOrder() {
	return qazkomOrder(invoice());
    }

    public static QazkomOrder qazkomOrder(final Invoice invoice) {
	return qazkomOrderBuilder(invoice) //
		.build();
    }

    public static QazkomOrderBuilder qazkomOrderBuilder(final Invoice invoice) {
	return QazkomOrder.builder() //
		.forInvoice(invoice) //
		.withNumber("617300137516891") //
		.withMerchant("92061103", "Test shop 3", merchantCert, merchantKey) //
	;
    }
}

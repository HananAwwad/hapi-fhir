package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.TestUtil;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerUsingOldTypesDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testReadProviderString() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new ReadProviderString());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
		}
	}

	@Test
	public void testReadProviderIdDt() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new ReadProviderIdDt());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
		}
	}

	@Test
	public void testOperationProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new OperationProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("Incorrect use of type"));
		}
	}

	public static class ReadProviderString implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient opTypeRetOldBundle(@IdParam String theId) {
			return null;
		}

	}

	public static class ReadProviderIdDt implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient opTypeRetOldBundle(@IdParam IdDt theId) {
			return null;
		}

	}

	public static class OperationProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "foo")
		public Patient opTypeRetOldBundle(@OperationParam(name = "foo") IntegerDt theId) {
			return null;
		}

	}

}

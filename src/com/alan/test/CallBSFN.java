package com.alan.test;
import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.ServerFailureException;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.spec.source.*;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.ApplicationException;
import com.jdedwards.system.connector.dynamic.callmethod.*;
public class CallBSFN {
	public CallBSFN() {
		super();
	}

	public static void main(String[] args) throws ServerFailureException, SpecFailureException {
		CallBSFN callBSFN = new CallBSFN();

		BSFNSpecSource specSource = null;
		// Step 1: Login
		int sessionID = Connector.getInstance().login("JDE", "JDEadmin", "JPY920", "*ALL");
		System.out.println("SessionID--->" + sessionID);
		// Pre-condition: create the SpecDictionary or BSFNSpecSource
		specSource = new OneworldBSFNSpecSource(sessionID);

		// Step 2: Lookup the BSFN method from SpecDictionary or BSFNSpecSource
		BSFNMethod bsfnMethod = (BSFNMethod) specSource.getBSFNMethod("GetEffectiveAddress");
			//System.out.println(specSource.getSpec("F0101")+"DDD");
		// Step 3: create the executable method from the BSFN metadata
		ExecutableMethod addressbook = bsfnMethod.createExecutable();
		try {

			// Step 4: Set parameter values
			addressbook.setValue("mnAddressNumber", "500");

			// Step 5: Execute the business function
			BSFNExecutionWarning warning = addressbook.execute(sessionID);

			// Step 6: Get return parameter values
			System.out.println("szNamealpha= " + addressbook.getValueString("szNamealpha"));
			System.out.println("mnAddressNumber= " + addressbook.getValueString("mnAddressNumber"));
		} catch (SystemException e) {
			// SystemException is thrown when system crash, this is a fatal
			// error and must be caught
			System.exit(1);
		} catch (ApplicationException e) {
			// ApplicationException is thrown when business function
			// execution fail, this is RuntimeException and thus can be
			// unchecked. But it is strongly recommend to catch this
			// exception
		} finally {
			// Log off and shut down connector if necessary
			Connector.getInstance().logoff(sessionID);
			Connector.getInstance().shutDown();
		}

	}
}

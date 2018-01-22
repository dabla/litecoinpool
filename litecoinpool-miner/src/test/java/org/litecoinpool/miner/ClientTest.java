package org.litecoinpool.miner;

import static org.smartwallet.stratum.StratumMessageBuilder.aStratumMessage;

import org.junit.Test;
import org.smartwallet.stratum.StratumMessage;

public class ClientTest extends AbstractTest {
	@Test
	public void execute() throws Exception {
		StratumMessage message = aStratumMessage().withId(1L).withMethod("mining.subscribe").build();
		Client client = Client.address("litecoinpool.org", 3333);
		client.message(message).execute();
		
		message = aStratumMessage().withId(2L).withMethod("mining.authorize").withParams("dabla.1","1").build();
		client.message(message).execute();
	}
}

package org.smartwallet.stratum;

import static be.dabla.asserters.Poller.aPoller;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bitcoinj.core.NetworkParameters.ID_MAINNET;
import static org.bitcoinj.core.NetworkParameters.fromID;

import java.net.InetSocketAddress;

import org.junit.Test;
import org.smartwallet.AbstractTest;
import org.unitils.inject.annotation.TestedObject;

import be.dabla.asserters.Assertion;

public class StratumClientTest extends AbstractTest {
	@TestedObject
	private StratumClient client = new StratumClient(fromID(ID_MAINNET), singletonList(new InetSocketAddress("litecoinpool.org", 3333)), false);
	
	@Test
	public void startAsync() throws Exception {
		client.startAsync();
		
		aPoller().doAssert(new Assertion() {
			@Override
			public void assertion() throws Exception {
				assertThat(client.isRunning()).isTrue();
			}
		});
		
		client.subscribeToHeaders();
		
		aPoller().doAssert(new Assertion() {
			@Override
			public void assertion() throws Exception {
				assertThat(client.isRunning()).isFalse();
			}
		});
	}
}

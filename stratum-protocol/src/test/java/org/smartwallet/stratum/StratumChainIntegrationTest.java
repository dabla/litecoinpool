package org.smartwallet.stratum;

import static be.dabla.asserters.Poller.aPoller;
import static java.io.File.createTempFile;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bitcoinj.core.NetworkParameters.ID_MAINNET;
import static org.bitcoinj.core.NetworkParameters.fromID;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.bitcoinj.core.NetworkParameters;
import org.junit.Before;
import org.junit.Test;
import org.smartwallet.AbstractTest;

import be.dabla.asserters.Assertion;


public class StratumChainIntegrationTest extends AbstractTest {
	private static final NetworkParameters PARAMETERS = fromID(ID_MAINNET);
	
	private HeadersStore store;
	private StratumClient client;
	private StratumChain chain;
	
	@Before
    public void setUp() throws IOException {
        client = new StratumClient(PARAMETERS, singletonList(new InetSocketAddress("litecoinpool.org", 3333)), false);
        store = new HeadersStore(PARAMETERS, createTempFile("stratum-chain", ".chain"), null, null);
        chain = new StratumChain(PARAMETERS, store, client);
    }
	 
	@Test
	public void startAsync() throws Exception {
		chain.startAsync();
		
		aPoller().doAssert(new Assertion() {
			@Override
			public void assertion() throws Exception {
				assertThat(chain.isRunning()).isTrue();
			}
		});
		
		chain.stopAsync();
		
		aPoller().doAssert(new Assertion() {
			@Override
			public void assertion() throws Exception {
				assertThat(chain.isRunning()).isFalse();
			}
		});
	}
}
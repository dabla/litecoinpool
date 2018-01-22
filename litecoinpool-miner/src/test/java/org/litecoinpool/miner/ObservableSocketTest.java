package org.litecoinpool.miner;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ObservableSocketTest extends AbstractTest {
	@Mock
	private Socket socket;
	@Mock
	private InputStream inputStream;
	
	@Before
	public void setUp() throws Exception {
		when(socket.getInputStream()).thenReturn(inputStream);
		doAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return null;
			}
		}).when(inputStream);
	}
	
	@Test
	public void read() throws Exception {
		ObservableSocket actual = ObservableSocket.connect(socket);
	}
}

/**

[21/01/2018 14:38:42:042 CET] main  WARN org.unitils.core.ConfigurationLoader: No custom configuration file unitils.properties found.
[21/01/2018 14:38:42:042 CET] main  INFO org.unitils.core.ConfigurationLoader: No local configuration file unitils-local.properties found.
[21/01/2018 14:38:42:042 CET] main  INFO org.litecoinpool.miner.ClientSocket: > {"id":1,"method":"mining.subscribe","params":[]}
[21/01/2018 14:38:45:045 CET] main  INFO org.litecoinpool.miner.ClientSocket: line: {"id":1,"error":null,"result":[[["mining.notify","22006f6716a30bcf"],["mining.set_difficulty","22006f6716a30bcf2"]],"22006f67",4]}
[21/01/2018 14:38:45:045 CET] main  INFO org.litecoinpool.miner.Client: < org.smartwallet.stratum.StratumMessage@5e5d171f
[21/01/2018 14:38:46:046 CET] main  INFO org.litecoinpool.miner.ClientSocket: line: {"id":null,"method":"mining.set_difficulty","params":[65536]}
[21/01/2018 14:38:46:046 CET] main  INFO org.litecoinpool.miner.Client: < org.smartwallet.stratum.StratumMessage@77f1baf5
[21/01/2018 14:38:46:046 CET] main  INFO org.litecoinpool.miner.ClientSocket: line: {"id":null,"method":"mining.notify","params":["9681","09a383b0c66cb58edbf91195ee397b370b2d9526ba409218d48cfac06f74a527","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff440340aa14045a6497db2cfabe6d6ddc6d7813358824cb83cc8c01d7011c8edfcead078582937d644facff553363234000000000000000042f4c502f08","ffffffff024bc73d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9eda0fb7c9aa2db1773ac0657f45b886c3bfd7f6109e3a37492c913d14dbfef689b00000000",["92e471384e46c0c60f8cca80c9d0d5d4f7fe69e3277ed97d42d71081468eb281","5900c1da5546fc3cbda270a245b9c1e27ca046fff066e6b976c8041724df24f4","d369fd47d3f9b642a44eba7d0fe0b27eb1f0a924bd8a00c81ca963e2b5d4d2ea","05deba6d014dff44d273e7f75faf367ab130eee3ca270543cdbfa0d4ab7bdfe4","ffc1c1c8337d63909062d174951c0a190b59cfcfb0ff66d1f52fbac3712ea7df","c0e2ede7d39b2b84016819b70681a23055cd269fcd993add4407e5a4e14ec6fc"],"20000000","1a048e6f","5a6497db",false]}
[21/01/2018 14:38:46:046 CET] main  INFO org.litecoinpool.miner.Client: < org.smartwallet.stratum.StratumMessage@41a2befb
[21/01/2018 14:39:07:007 CET] main  INFO org.litecoinpool.miner.ClientSocket: line: {"id":null,"method":"mining.notify","params":["9682","09a383b0c66cb58edbf91195ee397b370b2d9526ba409218d48cfac06f74a527","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff440340aa14045a6497f82cfabe6d6d130ee8d38d1d8f2941ce4932a5a9035ef2e6a7ed51ba91bf1f887141835ed4624000000000000000042f4c502f08","ffffffff024bc73d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9eda0fb7c9aa2db1773ac0657f45b886c3bfd7f6109e3a37492c913d14dbfef689b00000000",["92e471384e46c0c60f8cca80c9d0d5d4f7fe69e3277ed97d42d71081468eb281","5900c1da5546fc3cbda270a245b9c1e27ca046fff066e6b976c8041724df24f4","d369fd47d3f9b642a44eba7d0fe0b27eb1f0a924bd8a00c81ca963e2b5d4d2ea","05deba6d014dff44d273e7f75faf367ab130eee3ca270543cdbfa0d4ab7bdfe4","ffc1c1c8337d63909062d174951c0a190b59cfcfb0ff66d1f52fbac3712ea7df","c0e2ede7d39b2b84016819b70681a23055cd269fcd993add4407e5a4e14ec6fc"],"20000000","1a048e6f","5a6497f8",false]}
[21/01/2018 14:39:07:007 CET] main  INFO org.litecoinpool.miner.Client: < org.smartwallet.stratum.StratumMessage@6c40365c
*/
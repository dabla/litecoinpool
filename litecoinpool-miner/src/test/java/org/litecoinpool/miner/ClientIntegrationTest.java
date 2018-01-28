package org.litecoinpool.miner;

import static org.stratum.protocol.StratumMessageBuilder.aStratumMessage;
import static org.stratum.protocol.StratumMethod.MINING_AUTHORIZE;
import static org.stratum.protocol.StratumMethod.MINING_SUBSCRIBE;

import org.junit.Test;

public class ClientIntegrationTest extends AbstractTest {
	@Test
	public void listen() throws Exception {
		Client client = Client.address("litecoinpool.org", 3333);
		client.send(aStratumMessage().withMethod(MINING_SUBSCRIBE))
			  .send(aStratumMessage().withMethod(MINING_AUTHORIZE).withParams("dabla.1","1"))
			  .listen();
	}
	
	/**
	 * [23/01/2018 20:34:01:001 CET] main  WARN org.unitils.core.ConfigurationLoader: No custom configuration file unitils.properties found.
	 * [23/01/2018 20:34:01:001 CET] main  INFO org.unitils.core.ConfigurationLoader: No local configuration file unitils-local.properties found.
	 * [23/01/2018 20:34:02:002 CET] main  INFO org.litecoinpool.miner.ObservableSocket: > {"id":1,"method":"mining.subscribe","params":[],"result":null,"error":null}
	 * [23/01/2018 20:34:02:002 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":1,"error":null,"result":[[["mining.notify","16000a2a7fc09fff"],["mining.set_difficulty","16000a2a7fc09fff2"]],"16000a2a",4]}
	 * [23/01/2018 20:34:02:002 CET] main  INFO org.litecoinpool.miner.Client: < {"id":1,"method":null,"params":[],"result":[[["mining.notify","16000a2a7fc09fff"],["mining.set_difficulty","16000a2a7fc09fff2"]],"16000a2a",4],"error":null}
	 * [23/01/2018 20:34:03:003 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.set_difficulty","params":[65536]}
	 * [23/01/2018 20:34:03:003 CET] main  INFO org.litecoinpool.miner.Client: < {"id":null,"method":"mining.set_difficulty","params":[65536],"result":null,"error":null}
	 * [23/01/2018 20:34:06:006 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.notify","params":["6a9e","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e2d2cfabe6d6d73d8d81be9caae63e0520e1c0a8da19156f4c637050c1f1d1ae585d6a8247b674000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e2d",true]}
	 * [23/01/2018 20:34:06:006 CET] main  INFO org.litecoinpool.miner.Client: < {"id":null,"method":"mining.notify","params":["6a9e","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e2d2cfabe6d6d73d8d81be9caae63e0520e1c0a8da19156f4c637050c1f1d1ae585d6a8247b674000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e2d",true],"result":null,"error":null}
	 * [23/01/2018 20:34:07:007 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.notify","params":["6a9f","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e2f2cfabe6d6dcc7d07c1c9a567071e275830de7c3399c62be923dcef083de4d8e6447e15ef694000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e2f",false]}
	 * [23/01/2018 20:34:07:007 CET] main  INFO org.litecoinpool.miner.Client: < {"id":null,"method":"mining.notify","params":["6a9f","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e2f2cfabe6d6dcc7d07c1c9a567071e275830de7c3399c62be923dcef083de4d8e6447e15ef694000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e2f",false],"result":null,"error":null}
	 * [23/01/2018 20:34:46:046 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.notify","params":["6aa0","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e562cfabe6d6dce803504952b27e691a9fc94b70dec769ae37a0f32712c238e7737f5da8c7d064000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e56",true]}
	 * [23/01/2018 20:34:46:046 CET] main  INFO org.litecoinpool.miner.Client: < {"id":null,"method":"mining.notify","params":["6aa0","8a87d3a378333404cc4760dfbfa5f96261159438dcf7cdf0d13ce8addc1fe1e2","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033faf14045a678e562cfabe6d6dce803504952b27e691a9fc94b70dec769ae37a0f32712c238e7737f5da8c7d064000000000000000042f4c502f08","ffffffff0268e41d95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed52218afaf84b7392272c624581d43836c581d7dc3d54f923441dbe18e461c6e600000000",["913e9ac4c1ddf38f82030c1f9da0e054a61b0211673afd5ef19c16bd9e9a3757","2a250b9a37db4b7024b9fde5950927111c419e312da9293e31d1c98e51e3ab66","27a98561001b0e466f99ee866cae0a43a3eb72bba1166596f7e4f74f645aae39","1e64c69c1a674dc484f2be95354a672c87debbfce9f40246bb5bbd492d9624d5","ef06ed2044c00d146b9924c3d9130b25b7964827d43dc1feb8ed35ace07d788d"],"20000000","1a048442","5a678e56",true],"result":null,"error":null}
	 * 
	 * 
	 * 
	 * [28/01/2018 21:43:04:004 CET] main  WARN org.unitils.core.ConfigurationLoader: No custom configuration file unitils.properties found.
	 * [28/01/2018 21:43:04:004 CET] main  INFO org.unitils.core.ConfigurationLoader: No local configuration file unitils-local.properties found.
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.TargetMatcher: Target is 1.7668201048317172E72 (ffff00000000000000000000000000000000000000000000000000000000)
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.ObservableSocket: > {"id":1,"method":"mining.subscribe","params":[],"result":null,"error":null}
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.ObservableSocket: > {"id":2,"method":"mining.authorize","params":["dabla.1","1"],"result":null,"error":null}
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"result":[[["mining.notify","160059bc85bd651f"],["mining.set_difficulty","160059bc85bd651f2"]],"160059bc",4],"id":1,"error":null}
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.Client: stratumMethod: mining.notify
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.Client: stratumMethod: mining.set_difficulty
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.Client: difficulty: 25365851761070723570
	 * [28/01/2018 21:43:05:005 CET] main  INFO org.litecoinpool.miner.TargetMatcher: Target is 6.96534901123753E52 (ba2ada1756b710000000000000000000000000000000)
	 * [28/01/2018 21:43:26:026 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"params":["db29","971f8c4b610e73ee3646e1a8f405608572bdda2a82e3a7e94031b90ed3935b38","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff440339bb14045a6e35ed2cfabe6d6d0264f2f1d9d3c43fedf0006434a71ee8e852150497a600c9785a993d90879dc74000000000000000042f4c502f08","ffffffff02d0f6d795000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed82d229aa4debe07e54acfc8e426c4b1a603a7569feec671c0000e6dda83022aa00000000",["3f977297a84b209d5cb6419aac3038feab23b98f36dc599be6793d808f2daf65","0e8480b551abed07acdb58fff7f2a32f001e12baf5fd28939a0e71600be94d4d","160785b6ce63af856a6eab66559fbc154fb7be1f8c660a50027af87c7015f997","528430d40f614eff0e5e17dfa63fc75f6fdbe8fcc06adc9442388836eb6bdcdd","0aec4a437ef194d6fe0611b0ad62b423fbeacd9e6ba058ab43431d848ecfae2a","06e26aaeafe6f2e8c76c565c4b61be1519475dd4f17b7d75e2791a9b47c860da","72bca3bd231c381a2d92143ee40a490b8f100905c1ef98fcc2068a290b8676ff","4d6d6f421dddb7401f0ba5d9208a01bc0a1e18976b8ada7633f18aca449b5b08"],"20000000","1a047f14","5a6e35ed",true],"method":"mining.notify"}
	 * [28/01/2018 21:43:31:031 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"params":["db2a","971f8c4b610e73ee3646e1a8f405608572bdda2a82e3a7e94031b90ed3935b38","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff440339bb14045a6e35f32cfabe6d6db87db6e5bbdd0d17aa4b771bb4f1ab2098011b6a659c7dcb0225602c5204358f4000000000000000042f4c502f08","ffffffff02d0f6d795000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9ed82d229aa4debe07e54acfc8e426c4b1a603a7569feec671c0000e6dda83022aa00000000",["3f977297a84b209d5cb6419aac3038feab23b98f36dc599be6793d808f2daf65","0e8480b551abed07acdb58fff7f2a32f001e12baf5fd28939a0e71600be94d4d","160785b6ce63af856a6eab66559fbc154fb7be1f8c660a50027af87c7015f997","528430d40f614eff0e5e17dfa63fc75f6fdbe8fcc06adc9442388836eb6bdcdd","0aec4a437ef194d6fe0611b0ad62b423fbeacd9e6ba058ab43431d848ecfae2a","06e26aaeafe6f2e8c76c565c4b61be1519475dd4f17b7d75e2791a9b47c860da","72bca3bd231c381a2d92143ee40a490b8f100905c1ef98fcc2068a290b8676ff","4d6d6f421dddb7401f0ba5d9208a01bc0a1e18976b8ada7633f18aca449b5b08"],"20000000","1a047f14","5a6e35f3",false],"method":"mining.notify"}
	 * [28/01/2018 21:43:35:035 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.set_difficulty","params":[16384]}
	 * [28/01/2018 21:43:35:035 CET] main  INFO org.litecoinpool.miner.Client: difficulty: 16384
	 * [28/01/2018 21:43:35:035 CET] main  INFO org.litecoinpool.miner.TargetMatcher: Target is 1.0783814116404524E68 (3fffc0000000000000000000000000000000000000000000000000000)
	 * [28/01/2018 21:43:35:035 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"method":"mining.notify","params":["db2b","d144b688320ababaa9ae981138bfccab9980fe440a649c98a6fbc5660c410397","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033abb14045a6e35f72cfabe6d6d5b282669b422905db800b2f3a0504c0b782891bff0c102245fef4ffc262553fd4000000000000000042f4c502f08","ffffffff0100f90295000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac00000000",[],"20000000","1a047f14","5a6e35f7",true]}
	 * [28/01/2018 21:43:36:036 CET] RxComputationThreadPool-2  INFO org.litecoinpool.miner.ObservableSocket: > {"id":3,"method":"mining.submit","params":["dabla.1","db29","00000002","5a6e35ed","0000852f"],"result":null,"error":null}
	 * [28/01/2018 21:43:36:036 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"result":null,"id":3,"error":[23,"target-miss",null]}
	 * [28/01/2018 21:43:37:037 CET] RxComputationThreadPool-2  INFO org.litecoinpool.miner.ObservableSocket: > {"id":4,"method":"mining.submit","params":["dabla.1","db29","00000002","5a6e35ed","0000eb91"],"result":null,"error":null}
	 * [28/01/2018 21:43:37:037 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"result":null,"id":4,"error":[23,"target-miss",null]}
	 * [28/01/2018 21:43:40:040 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"id":null,"params":["db2c","d144b688320ababaa9ae981138bfccab9980fe440a649c98a6fbc5660c410397","01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff44033abb14045a6e35fc2cfabe6d6d7fb15e830c7e9a6b9f481d2a98ec26b218d3e6ae33abc466c493ee504d47fc264000000000000000042f4c502f08","ffffffff02594e0f95000000001976a91457757ed2ecf2052ff3126c397aca7defb9ca2cec88ac0000000000000000266a24aa21a9edc2598b722190845bfe5b8364a3b601ab149e92ccf3cf15061a92b43bddbd477f00000000",["c92dad664e552c8bb596632dbca073a248d31552e928453227f53f68711719cc","2c34bc0d329edcb20214773dcf3e3a7bbb95840770cb9d2650099cab3619f50f","73d62a47690a5c59a77335edc4e9912682d0e224cda93bbe95d71eef8d9f7b5f"],"20000000","1a047f14","5a6e35fc",true],"method":"mining.notify"}
	 * [28/01/2018 21:43:44:044 CET] RxComputationThreadPool-2  INFO org.litecoinpool.miner.ObservableSocket: > {"id":5,"method":"mining.submit","params":["dabla.1","db29","00000002","5a6e35ed","00008560"],"result":null,"error":null}
	 * [28/01/2018 21:43:44:044 CET] main  INFO org.litecoinpool.miner.ObservableSocket: < {"result":null,"id":5,"error":[23,"target-miss",null]}
	 * 
	 */
}

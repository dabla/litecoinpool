package org.litecoinpool.miner;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.MerkleBranchJoiner.on;

public class MerkleBranchJoinerTest {
   @Test
    public void join() throws DecoderException {
        assertThat(on(decodeHex("280b3927f6763b1ed90cae2a3cef4d27c743f6a7d91e3901dc3816a46acacf36"))
                .join("57351e8569cb9d036187a79fd1844fd930c1309efcd16c46af9bb9713b6ee734"))
                .isEqualTo("e6df228610b9f0e96a42a4877565627a3e1e133e984b6c46ff6e44b7dc9dc056");

        assertThat(on(decodeHex("e6df228610b9f0e96a42a4877565627a3e1e133e984b6c46ff6e44b7dc9dc056"))
                .join("936ab9c33420f187acae660fcdb07ffdffa081273674f0f41e6ecc1347451d23"))
                .isEqualTo("0b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d5422");

        assertThat(on(decodeHex("280b3927f6763b1ed90cae2a3cef4d27c743f6a7d91e3901dc3816a46acacf36"))
               .join("57351e8569cb9d036187a79fd1844fd930c1309efcd16c46af9bb9713b6ee734",
                       "936ab9c33420f187acae660fcdb07ffdffa081273674f0f41e6ecc1347451d23"))
               .isEqualTo("0b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d5422");
    }
}
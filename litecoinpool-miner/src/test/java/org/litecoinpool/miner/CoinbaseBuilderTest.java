package org.litecoinpool.miner;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.apache.commons.codec.binary.Hex.encodeHex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.CoinbaseBuilder.aCoinbase;

public class CoinbaseBuilderTest {
    @Test
    public void build() throws DecoderException {
        String actual = new String(encodeHex(aCoinbase()
                .withCoinbase1("01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff270362f401062f503253482f049b8f175308")
                .withExtranonce1("f8002c90")
                .withExtranonce2("00000002")
                .withCoinbase2("0d2f7374726174756d506f6f6c2f000000000100868591052100001976a91431482118f1d7504daf1c001cbfaf91ad580d176d88ac00000000")
                .build()));

        assertThat(actual).isEqualTo("280b3927f6763b1ed90cae2a3cef4d27c743f6a7d91e3901dc3816a46acacf36");
    }
}
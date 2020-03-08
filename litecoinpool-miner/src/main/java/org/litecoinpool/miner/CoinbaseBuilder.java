package org.litecoinpool.miner;

import org.apache.commons.codec.DecoderException;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripToNull;
import static org.litecoinpool.miner.Crypto.crypto;

class CoinbaseBuilder {
    private static final byte[] EMPTY_COINBASE = new byte[]{};

    private String coinbase1;
    private String extranonce1;
    private String extranonce2;
    private String coinbase2;

    private CoinbaseBuilder() {}

    static CoinbaseBuilder aCoinbase() {
        return new CoinbaseBuilder();
    }

    public CoinbaseBuilder withCoinbase1(String coinbase1) {
        this.coinbase1 = coinbase1;
        return this;
    }

    public CoinbaseBuilder withExtranonce1(String extranonce1) {
        this.extranonce1 = extranonce1;
        return this;
    }

    public CoinbaseBuilder withExtranonce2(String extranonce2) {
        this.extranonce2 = extranonce2;
        return this;
    }

    public CoinbaseBuilder withCoinbase2(String coinbase2) {
        this.coinbase2 = coinbase2;
        return this;
    }

    public byte[] build() throws DecoderException {
        String coinbase = stripToNull(join(coinbase1, extranonce1, extranonce2, coinbase2));

        if (coinbase != null) {
            return crypto().dsha256(decodeHex(coinbase));
        }

        return EMPTY_COINBASE;
    }
}

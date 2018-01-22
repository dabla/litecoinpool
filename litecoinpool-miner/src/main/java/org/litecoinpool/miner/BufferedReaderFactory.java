package org.litecoinpool.miner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferedReaderFactory {
	public BufferedReader create(InputStream inputStream) throws Exception {
		return new BufferedReader(new InputStreamReader(inputStream));
	}
}

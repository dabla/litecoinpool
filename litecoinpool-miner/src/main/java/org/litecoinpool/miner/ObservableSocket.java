package org.litecoinpool.miner;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static io.reactivex.Observable.empty;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.using;
import static io.reactivex.internal.functions.Functions.emptyConsumer;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ObservableSocket {
	private static final Logger LOGGER = getLogger(ObservableSocket.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final BufferedReaderFactory READER_FACTORY = new BufferedReaderFactory();
	
	static {
		MAPPER.configure(AUTO_CLOSE_TARGET, false);
	}
	
	private final Socket socket;
	
	private ObservableSocket(Socket socket) {
		this.socket = socket;
	}
	
	public static ObservableSocket from(SocketAddress address) throws IOException {
		Socket socket = new Socket();
		socket.connect(address);
    	return from(socket);
	}
	
	public static ObservableSocket from(Socket socket) {
    	return new ObservableSocket(socket);
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	public void disconnect() throws IOException {
		socket.close();
	}
	
    public void write(StratumMessage message) throws IOException {
    	LOGGER.info("> {}", MAPPER.writeValueAsString(message));
        	
    	OutputStream outputStream = socket.getOutputStream();
		MAPPER.writeValue(outputStream, message);
    	outputStream.write('\n');
    }
    
    public Observable<StratumMessage> read() {
    	return using(reader(socket), mapper(), emptyConsumer());
	}
    
    private static Callable<BufferedReader> reader(final Socket socket) {
		return new Callable<BufferedReader>() {
			@Override
			public BufferedReader call() throws Exception {
				return READER_FACTORY.create(socket.getInputStream());
			}
    	};
	}

	private static Function<BufferedReader, ObservableSource<StratumMessage>> mapper() {
		return new Function<BufferedReader,ObservableSource<StratumMessage>>() {
			@Override
			public ObservableSource<StratumMessage> apply(BufferedReader reader) throws Exception {
				String line = reader.readLine();

				if (isNotBlank(line)) {
					LOGGER.info("< {}", line);
					return just(MAPPER.readValue(line, StratumMessage.class));
				}
				
				return empty();
			}
    	};
	}
}

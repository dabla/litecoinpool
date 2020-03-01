package org.litecoinpool.miner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.slf4j.Logger;
import org.stratum.protocol.StratumMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static io.reactivex.Flowable.empty;
import static io.reactivex.Flowable.just;
import static io.reactivex.Flowable.using;
import static io.reactivex.internal.functions.Functions.emptyConsumer;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

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
    
    public Flowable<StratumMessage> read() {
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

	private static Function<BufferedReader, Flowable<StratumMessage>> mapper() {
		return new Function<BufferedReader,Flowable<StratumMessage>>() {
			@Override
			public Flowable<StratumMessage> apply(BufferedReader reader) throws Exception {
				String line = reader.readLine();

				if (isNotBlank(line)) {
					LOGGER.info("< {}", line);
					
					try {
						return just(MAPPER.readValue(line, StratumMessage.class));
					}
					catch(IOException e) {
						LOGGER.error("Reading failed: ", e);
						return empty();
					}
				}
				
				return empty();
			}
    	};
	}
}

package org.litecoinpool.miner;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class Client {
	private static final Logger LOGGER = getLogger(Client.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ObservableSocket socket;
	
	static {
		MAPPER.configure(AUTO_CLOSE_TARGET, false);
	}

	private Client(ObservableSocket socket) {
		this.socket = socket;
	}
	
	public static Client address(String host, int port) throws IOException {
		return new Client(ObservableSocket.from(new InetSocketAddress(host, port)));
	}
	
	public Client send(StratumMessage message) throws IOException {
		socket.write(message);
		return this;
	}

	public void listen() throws IOException {
        socket.read()
        	  .takeWhile(isConnected())
        	  .repeat()
        	  .map(log())
        	  .blockingSubscribe();
	}

	private static Function<StratumMessage,String> log() {
		return new Function<StratumMessage,String>() {
			@Override
			public String apply(StratumMessage message) throws Exception {
				LOGGER.info("< {}", MAPPER.writeValueAsString(message));
				return message.toString();
			}
		};
	}
	
	private Predicate<StratumMessage> isConnected() {
        return new Predicate<StratumMessage>() {
            @Override
            public boolean test(StratumMessage message) throws Exception {
                return socket.isConnected();
            }
        };
    }
}
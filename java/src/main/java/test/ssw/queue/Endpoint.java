package test.ssw.queue;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Abstracts the connection with a queue
 * 
 * @author rogersole
 *
 */
public abstract class Endpoint {

    protected Channel    channel;
    protected Connection connection;
    //
    protected String     host;
    protected int        port;
    protected String     username;
    protected String     password;
    protected String     endpointName;

    public Endpoint(String host, int port, String username, String password, String endpointName)
                    throws QueueException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.endpointName = endpointName;

        init();
    }

    /**
     * Initializes the endpoint.
     * 
     * @throws QueueException
     */
    private void init() throws QueueException {

        try {
            // Create a connection factory
            ConnectionFactory factory = new ConnectionFactory();

            // connection properties of the rabbitmq server
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setPort(port);

            // getting a connection
            connection = factory.newConnection();

            // creating a channel
            channel = connection.createChannel();

            // declaring a queue for this channel. If queue does not exist,
            // it will be created on the server.
            channel.queueDeclare(endpointName, false, false, false, null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new QueueException("Captured " + ex.getClass().getName() + " when creating the queue '"
                            + endpointName + "': " + ex.toString());
        }
    }


    /**
     * Close channel and connection. Not necessary as it happens implicitly any way.
     * 
     * @throws IOException
     */
    public void close() throws QueueException {
        try {
            this.channel.close();
            this.connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new QueueException("Captured " + ex.getClass().getName() + " when closing the queue '" + endpointName
                            + "': " + ex.toString());
        }
    }
}

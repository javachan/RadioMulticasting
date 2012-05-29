import org.jgroups.JChannel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        //Create new control channel
        String controlClusterName = "RadioOzuControl";
        String dataClusterName = "RadioOzu";
        JChannel channel =  new JChannel("channel-udp.xml");
        channel.connect(controlClusterName);

        List<IStreamSink> sinks = new ArrayList<IStreamSink>();
        sinks.add(new LocalSink("127.0.0.1",
                Integer.parseInt(System.getProperty("radioListenPort"))));

        if(channel.getView().getMembers().size() == 1){
            sinks.add(new BroadcastSink(dataClusterName));
        }

        //Create sources
        String url = "http://205.188.215.229:8028";
        IStreamSource source = !(channel.getView().getMembers().size() > 1) ?
                new InternetSource(new URL(url), sinks):
                new BroadcastSource(dataClusterName, sinks);
    }
}

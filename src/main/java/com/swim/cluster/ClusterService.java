package com.swim.cluster;

import io.scalecube.cluster.Cluster;
import io.scalecube.transport.Address;
import io.scalecube.transport.Message;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class ClusterService {
    /**
     * Initialize cluster and listen to incoming messages
     * */
    public Cluster startCluster(String name){
        Map<String, String> metadata = Collections.singletonMap("name", name);
        Cluster cluster = Cluster.joinAwait(metadata);
        this.listenGossip(cluster,name);
        this.listenMessage(cluster,name);
        return cluster;
    }
    /**
     * Join Cluster and Send message
     * **/
    public Cluster joinCluster(String name, String host, int port){
        Map<String, String> metadata = Collections.singletonMap("name", name);
        Address seed =  Address.create(host,port);
        Cluster cluster = Cluster.joinAwait(metadata, seed);
//        Message greetingMsg = Message.fromData("Greetings from "+name);
        cluster.spreadGossip(Message.fromData("Gossip from "+name))
                .doOnError(System.err::println)
                .subscribe();
        return cluster;
    }


    // listen to gossip messages
    private void listenGossip(Cluster cluster,String name){
        cluster.listenGossips().subscribe(gossip -> {
            System.out.println(name+" heard gossip: " + gossip.data());
        });
    }

    // listen for greetings messages
    private void listenMessage(Cluster cluster,String name){
        cluster.listen().
                subscribe(
                        msg -> {
                            System.out.println(name+" received greetings: " + msg.data());
                            cluster.send(msg.sender(), Message.fromData("Greetings from "+name));
        });
    }

}

package com.swim.cluster.web.controller;

import com.swim.cluster.ClusterService;
import io.scalecube.cluster.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClusterControoller {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "/heartBeat", method = RequestMethod.GET)
    public String getHeartBeat() {

        return "Application is running...";
    }

    @GetMapping(value = "/join/{name}")
    public String  startCluster(@PathVariable String name) {
        Cluster cs = clusterService.startCluster(name);
        return "Cluster initialisation successful. Host:  "+ cs.member().address().host() + ", port: "+cs.member().address().port();
    }

    @GetMapping(value = "/join/{name}/{host}/{port}")
    public String  joinCluster(@PathVariable String name,@PathVariable String host, @PathVariable int port) {
        Cluster cs = clusterService.joinCluster(name,host,port);
        return "Cluster join successful. Host:  "+ cs.member().address().host() + ", port: "+cs.member().address().port();
    }

}

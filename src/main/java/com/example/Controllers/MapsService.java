package com.example.Controllers;


import com.example.Entities.Maps;
import com.example.Entities.Statistics;
import com.example.Repositories.MapsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/maps")
public class MapsService {

    @Autowired
    private MapsRepository mapsRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Maps> getAllMaps() {
        List<Maps> maps= mapsRepository.findAll();
        maps.sort(Comparator.comparing(Maps::getId));

        ArrayList<Maps> mapsFinal= new ArrayList<Maps>();

        for (int i=maps.size()-1;i>maps.size()-16;i--){
            mapsFinal.add(maps.get(i));
        }


        return mapsFinal;
    }
}

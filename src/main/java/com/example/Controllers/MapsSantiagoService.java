package com.example.Controllers;


import com.example.Entities.MapsSantiago;
import com.example.Repositories.MapsSantiagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/maps-santiago")
public class MapsSantiagoService {

    @Autowired
    private MapsSantiagoRepository mapsSantiagoRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<MapsSantiago> getAllMaps() {
        List<MapsSantiago> maps= mapsSantiagoRepository.findAll();
        maps.sort(Comparator.comparing(MapsSantiago::getIdComuna));

        ArrayList<MapsSantiago> mapsFinal= new ArrayList<MapsSantiago>();

        for (int i=maps.size()-1;i>maps.size()-53;i--){
            mapsFinal.add(maps.get(i));
        }


        return mapsFinal;
    }


}

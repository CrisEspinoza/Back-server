package com.example.Controllers;

import com.example.Entities.Maps;
import com.example.Entities.NeoInfluential;
import com.example.Repositories.NeoInfluentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/neo4j")
public class NeoInfluentialService {

    @Autowired
    private NeoInfluentialRepository neoInfluentialRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<NeoInfluential> getAllNeoInfluential() {
        List<NeoInfluential> neo = neoInfluentialRepository.findAll();
        neo.sort(Comparator.comparing(NeoInfluential::getIdUser));

        ArrayList<NeoInfluential> neoFinal= new ArrayList<NeoInfluential>();

        for (int i=neo.size()-1;i>neo.size()-5;i--){
            neoFinal.add(neo.get(i));
        }

        return neoFinal;
    }
}

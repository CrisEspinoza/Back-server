package com.example.Controllers;

import com.example.Analyzer.Neo4j;
import com.example.Entities.Club;
import com.example.Entities.Maps;
import com.example.Entities.NeoInfluential;
import com.example.Entities.UsuarioInfluyente;
import com.example.Repositories.ClubRepository;
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
    @Autowired
    private ClubRepository clubRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<NeoInfluential> getAllNeoInfluential() {
        List<NeoInfluential> neo = neoInfluentialRepository.findAll();
        for (int i=0;i<neo.size();i++) {
            if(neo.get(i).getId() == 17){
                neo.remove(i);
            }
            neo.get(i).setSegInfluyentes();

        }
        return neo;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public NeoInfluential getIdNeoInfluential(@PathVariable("id") Long id) {

        Club club = clubRepository.findClubById(id);
//        System.out.println("%%%%%%%%%%%%%el club solicitdo es :"+club.getName());
//        NeoInfluential neoI= neoInfluentialRepository.findNeoInfluentialById(club.getNeonInfluential().getIdNeo());
//        System.out.println(neoI.getLastUpdate());

        return club.getNeonInfluential();
    }



    @GetMapping("/mayor-seguidores")
    @ResponseBody
    public ArrayList<UsuarioInfluyente> getMayorSeguidores() {
        Neo4j neo = new Neo4j();
        neo.connect("bolt://178.62.215.252","neo4j","TBDG7");
        ArrayList<UsuarioInfluyente> respuesta = neo.getUsuariosMasInfluyentes();

        return respuesta;
    }
}

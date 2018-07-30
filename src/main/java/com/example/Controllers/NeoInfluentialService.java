package com.example.Controllers;

import com.example.Analyzer.Neo4j;
import com.example.Entities.*;
import com.example.Repositories.ClubRepository;
import com.example.Repositories.NeoInfluentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static jdk.nashorn.internal.objects.NativeFunction.function;

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



    @GetMapping("/grafo")
    @ResponseBody
    public   Map<String,Object> getGrafo(){
        System.out.println("****definiendo variables*****");
        List<Club>  equipos= clubRepository.findAll();
        ArrayList<Nodo> nodos = new ArrayList<Nodo>();
        ArrayList<Link> links = new ArrayList<Link>();

        int i =1;
        int indiceEquipo=1;
        int count=0;
        int countEquipo=0;
        System.out.println("****iniciando bucle*****");
        for (Club equipo: equipos) {
            if(equipo.getId()<17) {


                //AGREGO EQUIPO

                nodos.add(new Nodo(equipo.getName(), i, 8, equipo.getNeonInfluential().getStatistic_r(), equipo.getUrl()));
                countEquipo=count;
                System.out.println("****agregando equipo*****");
                count++;
                i++;
                int y = 1;
                for (UsuarioInfluyente user : equipo.getNeonInfluential().getUsuariosInfluyentes()) {

                    Nodo nuevoNodo = new Nodo(user.getName(), indiceEquipo, y, Math.round(user.getFollowers() / 3327729.8865619544), null);
//                int pos=nodos.indexOf(nuevoNodo);
                    int pos = Nodo.buscarNodo(nodos, nuevoNodo);
                    if (pos < 0) {


                        nodos.add(nuevoNodo);
                        System.out.println("****agregando usuario*****");

                        links.add(new Link(countEquipo, count));
                        System.out.println("****agregando link*****");
                        y++;
                        count++;
                    } else {
                        links.add(new Link(countEquipo, pos));
                    }

                }
                indiceEquipo++;

            }
        }
        System.out.println("****generando salida*****");

        Map<String,Object> salida = new HashMap<String,Object>();
        salida.put("nodos",nodos);
        salida.put("links",links);

        return salida;
    }
}

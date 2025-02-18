package com.example.demo;

import com.example.Analyzer.Classifier;
import com.example.Analyzer.Neo4j;
import com.example.Repositories.*;

import com.example.Analyzer.Indice;
import com.example.Entities.*;
import com.mongodb.*;

import com.mongodb.client.model.Updates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.stripAccents;


@Transactional
@Component
public class    schedulerAnalisis {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private StatisticRepository statisticsRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MapsRepository mapsRepository;

    @Autowired
    private MapsSantiagoRepository  mapsSantiagoRepository;

    @Autowired
    private Classifier classifier;

    @Autowired
    private NeoInfluentialRepository neoInfluentialRepository;



    @Scheduled(fixedRate = 1000000000)
    public void analizador() throws IOException, InterruptedException {

//        this.analisisGrafo();
//        this.analisisGeneral();

        this.analisisEspecifico();
//        Thread.sleep(900000000);


    }


    public void analisisGrafo() throws InterruptedException {
        System.out.println("INICIOOOOOOOOOO");
        Neo4j neo = new Neo4j();
        neo.connect("bolt://178.128.60.200","neo4j");
        neo.deleteAll();
//        System.out.println("CONECTADOO");


      neo.crearNodosEquipos(clubRepository.findAll());
//        System.out.println("NODO EQUIPO CREADO");
        neo.crearNodoUsuarios();
        neo.relacionarTweet(clubRepository.findAll());
        System.out.println("TERMINOOOOOO");
        //Thread.sleep(900000000);
    }



    public void analisisGeneral() throws IOException {



        MongoCredential credential = MongoCredential.createCredential("TBDG7A", "TBDG7-A", "antihackers3.0".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("178.128.222.125", 18117), Arrays.asList(credential));

        DB db = mongoClient.getDB("TBDG7-A");
        DBCollection collection  = db.getCollection("futbol");


        DBCursor cursor = collection.find();

        ArrayList<Commune> comunas = (ArrayList<Commune>) communeRepository.findAll();
        ArrayList<Region> regiones= (ArrayList<Region>) regionRepository.findAll();

        List<Commune> comunasMetropolitana =  regiones.get(6).getCommune();

        ArrayList<Maps> maps= new ArrayList<Maps>();
        ArrayList<MapsSantiago> mapsSantiago= new ArrayList<MapsSantiago>();

        Date date = new Date();
        long time = date.getTime();
        for (int i=0;i<regiones.size();i++){
            maps.add(new Maps());
            maps.get(i).setFirstName(regiones.get(i).getFirstName());
            maps.get(i).setId(regiones.get(i).getIdMaps());
            maps.get(i).setNegative_value((long) 0);
            maps.get(i).setPositive_value((long) 0);
            maps.get(i).setLastUpdate(new Timestamp(time));

        }
        for (int i=0; i<comunasMetropolitana.size();i++){
            mapsSantiago.add (new MapsSantiago());
            mapsSantiago.get(i).setFirstName(comunasMetropolitana.get(i).getFirstName());
            mapsSantiago.get(i).setId(comunasMetropolitana.get(i).getIdMaps());
            mapsSantiago.get(i).setNegative_value((long) 0);
            mapsSantiago.get(i).setPositive_value((long) 0);
            mapsSantiago.get(i).setLastUpdate(new Timestamp(time));

        }
        int[] acumulador= new int[3];
        acumulador[0]=0;
        acumulador[1]=0;
        acumulador[2]=0;

        while (cursor.hasNext()) {
            DBObject tweet = cursor.next();

//            Respaldo bd mongo
//            DBCursor cur2= collection2.find(new BasicDBObject("id", tweet.get("id")));
//            if(!cur2.hasNext()){
//                System.out.println("insertando nuevo dato");
//                BasicDBObject tweet2;
//                tweet2 = new BasicDBObject("id", tweet.get("id"))
//                        .append("text", tweet.get("text"))
//                        .append("like", tweet.get("like"))
//                        .append("geoLocation", tweet.get("geoLocation"))
//                        .append("retweet", tweet.get("retweet"))
//                        .append("locationUser", tweet.get("locationUser"))
//                        .append("name", tweet.get("name"))
//                        .append("followers", tweet.get("followers"));
//                collection2.insert(tweet2);
//            }






            int region=0;
//            System.out.println(">>>>>"+tweet.get("locationUser").toString());
            String location = stripAccents(tweet.get("locationUser").toString().split(",")[0]).toLowerCase();
            HashMap<String,Double> resultado = classifier.classify(tweet.get("text").toString());
            for (Commune c: comunas) {

                if(location.equals(stripAccents(c.getFirstName()).toLowerCase())){
                    region=c.getRegion().getId().intValue()-1;
                    if (resultado.get("positive")> resultado.get("negative")){

                        maps.get(region).setPositive_value(maps.get(region).getPositive_value()+1);
                    }
                    else if(resultado.get("positive")< resultado.get("negative")){

                        maps.get(region).setNegative_value(maps.get(region).getNegative_value()+1);
                    }



                    break;
                }



            }

            for (int i=0 ; i< comunasMetropolitana.size();i++){
                Commune comuna= comunasMetropolitana.get(i);
                if(location.equals(stripAccents(comuna.getFirstName()).toLowerCase())){
                    //System.out.println("Esta comuna es :"+ location);
                    if (resultado.get("positive")> resultado.get("negative")){

                        mapsSantiago.get(i).setPositive_value(mapsSantiago.get(i).getPositive_value()+1);
                    }
                    else if(resultado.get("positive")< resultado.get("negative")){

                        mapsSantiago.get(i).setNegative_value(mapsSantiago.get(i).getNegative_value()+1);
                    }
                }

            }


            if (resultado.get("positive")> resultado.get("negative")){

                acumulador[0]+=1;

            }
            else if(resultado.get("positive")< resultado.get("negative")){
                acumulador[1]+=1;

            }
            else {
                acumulador[2]+=1;
            }
            //System.out.println("la ubicacon de este tweet es :" +location);


        }
//        System.out.println(">>>>llegue aca2");
//        for (Maps m: maps) {
//            m.setLastUpdate(new Timestamp(time));
//            mapsRepository.save(m);
//
//        }
////        System.out.println(">>>>llegue aca3");
//        for(MapsSantiago mapsS:  mapsSantiago){
//            mapsS.setLastUpdate(new Timestamp(time));
//            mapsSantiagoRepository.save(mapsS);
//
//        }

        Club equipo = clubRepository.findClubById( Long.valueOf(17));

        // se crea una fecha tipo timestamp para el registro historico


        System.out.println("#################################################################");
        System.out.println("#################################################################");
        System.out.println("#################################################################");
        System.out.println("#################################################################");
        System.out.println("El resultado es :"+acumulador[0]+","+acumulador[1]+","+acumulador[2]);
        System.out.println("#################################################################");
        System.out.println("#################################################################");
        System.out.println("#################################################################");

        mongoClient.close();

        // se crea una clase statistics y se guardan en la bd
        Statistics statistics = new Statistics();
        statistics.setPositive_value(acumulador[0]);
        statistics.setNegative_value(acumulador[1]);
        statistics.setNeutro_value(acumulador[2]);


        statistics.setLastUpdate(new Timestamp(time));
        statistics.setName_statics("estadistica de generales");

        equipo.getStatistics().add(statistics);
        equipo.setMapasComunas(mapsSantiago);
        equipo.setMapasRegion(maps);
        clubRepository.save(equipo);
    }
    public void analisisEspecifico(){
        Date date = new Date();
        long time = date.getTime();

        List<Club>  clubs= clubRepository.findAll();
        Indice indice = new Indice();
//        indice.indexar();
        Neo4j neo = new Neo4j();
        neo.connect("bolt://178.128.60.200","neo4j");
        float mayorP=0;
        float mayorN=0;
        ArrayList<Commune> comunas = (ArrayList<Commune>) communeRepository.findAll();
        ArrayList<Region> regiones= (ArrayList<Region>) regionRepository.findAll();

        List<Commune> comunasMetropolitana =  regiones.get(6).getCommune();

        for (Club equipo: clubs) {
            int[] acumulador = new int[3];
            acumulador[0] = 0;
            acumulador[1] = 0;
            acumulador[2] = 0;


            if (equipo.getId() != 17) {


//                Maps[] maps= new Maps[15];
                ArrayList<Maps> maps= new ArrayList<Maps>();
               ArrayList<MapsSantiago> mapsSantiago= new ArrayList<MapsSantiago>();
                List<Map<String, Object>> influyentes = neo.getUsuariosInfluyentes(equipo.getName());
//                System.out.println("obtenidos, el tamaño es : "+ influyentes.size());
                ArrayList<Tweet> tweets;
                for (int i=0;i<regiones.size();i++){
                    maps.add(new Maps());
                    maps.get(i).setFirstName(regiones.get(i).getFirstName());
                    maps.get(i).setId(regiones.get(i).getIdMaps());
                    maps.get(i).setNegative_value((long) 0);
                    maps.get(i).setPositive_value((long) 0);
                    maps.get(i).setLastUpdate(new Timestamp(time));

                }
                for (int i=0; i<comunasMetropolitana.size();i++){
                    mapsSantiago.add (new MapsSantiago());
                    mapsSantiago.get(i).setFirstName(comunasMetropolitana.get(i).getFirstName());
                    mapsSantiago.get(i).setId(comunasMetropolitana.get(i).getIdMaps());
                    mapsSantiago.get(i).setNegative_value((long) 0);
                    mapsSantiago.get(i).setPositive_value((long) 0);
                    mapsSantiago.get(i).setLastUpdate(new Timestamp(time));

                }
                String busqueda = equipo.getName();
                for (Keyword apodo : equipo.getKeywords()) {
                    busqueda = busqueda + " OR  " + apodo.getName_keyword();
                }

                System.out.println("%%%%% " + busqueda + "%%%%%%%");
                tweets = indice.buscar(busqueda);

                for (Tweet tweet : tweets) {
                    HashMap<String, Double> resultado = classifier.classify(tweet.getText());
                    String location = stripAccents(tweet.getLocation().split(",")[0]).toLowerCase();
//                    System.out.println(location);
                    int i = buscar(tweet.getName(), influyentes);
                    int region =0;
//                    System.out.println("el indice vale:"+i);
                    for (Commune c: comunas) {

                        if (location.equals(stripAccents(c.getFirstName()).toLowerCase())) {
                            region = c.getRegion().getId().intValue() - 1;
                            if (resultado.get("positive") > resultado.get("negative")) {

                                maps.get(region).setPositive_value( maps.get(region).getPositive_value() + 1);
                            } else if (resultado.get("positive") < resultado.get("negative")) {

                                maps.get(region).setNegative_value( maps.get(region).getNegative_value() + 1);
                            }


                            break;
                        }
                    }

                    for (int j=0 ; j< comunasMetropolitana.size();j++){
                        Commune comuna= comunasMetropolitana.get(j);
                        if(location.equals(stripAccents(comuna.getFirstName()).toLowerCase())){
                            //System.out.println("Esta comuna es :"+ location);
                            if (resultado.get("positive")> resultado.get("negative")){

                                mapsSantiago.get(j).setPositive_value(mapsSantiago.get(j).getPositive_value()+1);
                            }
                            else if(resultado.get("positive")< resultado.get("negative")){

                                mapsSantiago.get(j).setNegative_value(mapsSantiago.get(j).getNegative_value()+1);
                            }
                        }

                    }


                    double influencia;
                    if (i > -1) {
//                        System.out.println("entro");
//                            influencia= 2.0;
                        if (resultado.get("positive") > resultado.get("negative")) {

                            acumulador[0] += 1.0 + (((Double) influyentes.get(i).get("seguidores") / 3327729.8865619544) * 0.7 + ((Double) influyentes.get(i).get("cantidad")) / 2000) * 0.3;
                        } else if (resultado.get("positive") < resultado.get("negative")) {
                            acumulador[1] += 1.0 + (((Double) influyentes.get(i).get("seguidores") /3327729.8865619544) * 0.7 + ((Double) influyentes.get(i).get("cantidad")) / 2000) * 0.3;
                        } else {
                            acumulador[2] += 1.0 + (((Double) influyentes.get(i).get("seguidores") / 3327729.8865619544) * 0.7 + ((Double) influyentes.get(i).get("cantidad")) / 2000) * 0.3;
                        }


                    } else {
//                        System.out.println(" no entro");
//                        influencia=1.0;
                        if (resultado.get("positive") > resultado.get("negative")) {

                            acumulador[0] += 1.0;
                        } else if (resultado.get("positive") < resultado.get("negative")) {
                            acumulador[1] += 1.0;
                        } else {
                            acumulador[2] += 1.0;
                        }
                    }


                }

//                for (Map<String, Object> inf: influyentes) {
//
//                }



                Statistics statistics = new Statistics();
                statistics.setPositive_value(acumulador[0]);
                statistics.setNegative_value(acumulador[1]);
                statistics.setNeutro_value(acumulador[2]);

                statistics.setLastUpdate(new Timestamp(time));
                statistics.setName_statics("estadistica de equipos");
                NeoInfluential neoI =  new NeoInfluential();
                neoI.setStatistic_x(acumulador[0]);
                neoI.setStatistic_y( acumulador[1]);
                neoI.setNameClub(equipo.getName());
                equipo.setNeonInfluential(neoI);

                equipo.getStatistics().add(statistics);
                equipo.setMapasRegion(maps);
                equipo.setMapasComunas(mapsSantiago);
//
//
//                equipo.getNeonInfluential().setStatistic_x(acumulador[0]);
//                equipo.getNeonInfluential().setStatistic_y(acumulador[1]);
//                equipo.getNeonInfluential().setLastUpdate(new Timestamp(time));

//                clubRepository.save(equipo);

                System.out.println("***************" + equipo.getName() + "******************************");
                System.out.println("*********************************************");
                System.out.println("El resultado es :" + acumulador[0] + "," + acumulador[1] + "," + acumulador[2]);
                System.out.println("*********************************************");
                System.out.println("*********************************************");


                if (acumulador[0] > mayorP) {
                    mayorP = ((float) acumulador[0]);
                }

                if (acumulador[1] > mayorN) {
                    mayorN = ((float) acumulador[1]);
                }

            }


        }





        for (Club equipo:clubs) {

            if (equipo.getId() < 17){
                List<UsuarioInfluyente> influencias = neo.getInfluyentesEquipo(equipo, 5);
            System.out.println("$$$$$$$$$$$$$el tamaño de influencias es :" + influencias.size());
            float razon = (float) (((float) equipo.getNeonInfluential().getStatistic_x()) + ((float) equipo.getNeonInfluential().getStatistic_y())) / (mayorP + mayorN);
                System.out.println("razon es:"+razon+" x: "+equipo.getNeonInfluential().getStatistic_x()+" y:"+equipo.getNeonInfluential().getStatistic_y());
            String busqueda = equipo.getName();
            
            for (Keyword apodo : equipo.getKeywords()) {
                busqueda = busqueda + " OR " + apodo.getName_keyword();
            }
                equipo.getNeonInfluential().setStatistic_r(razon);
                equipo.getNeonInfluential().setLastUpdate(new Timestamp(time));
            for (UsuarioInfluyente influencia : influencias) {
                int[] cantidades=getRazon(busqueda, influencia.getName());
                influencia.setCantidadPositivos(cantidades[0]);
                influencia.setCantidadNegativos(cantidades[1]);
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + influencia.getRazon() + " " + influencia.getName());
                equipo.getNeonInfluential().getUsuariosInfluyentes().add(influencia);
            }
                System.out.println("llegue aca3");

            clubRepository.save(equipo);

        }

        }

        System.out.println("*********************************************");
        System.out.println("*********************************************");
        System.out.println("Finalizoooooooo");
        System.out.println("*********************************************");
        System.out.println("*********************************************");

    }

    public int  buscar(String name,   List<Map<String, Object>> influyentes) {
//        Neo4j neo= new Neo4j();
        for (int i = 0; i < influyentes.size(); i++) {
//           System.out.println("nombre es :"+ influyentes.get(i).get("name")+" buscado es :"+name);
            if (influyentes.get(i).get("name").toString().equals(name)) {
                return i;
            }

        }
        return -1;
    }


    public  int[] getRazon(String equipo, String name) {
        int positivos=0;
        int negativos=0;
        int neutros=0;
        Indice indice= new Indice();
        ArrayList<Tweet> tweets=indice.buscarUsuario(name,equipo);
        System.out.println("buscando :"+name);
        for (Tweet tweet: tweets) {
//            System.out.println("encontrado :"+tweet.getName());
            HashMap<String, Double> resultado = classifier.classify(tweet.getText());

            if (resultado.get("positive") > resultado.get("negative")) {

                positivos += 1;
            } else if (resultado.get("positive") < resultado.get("negative")) {
                negativos += 1;
            } else {
                neutros += 1;
            }
        }
        int[] salida= new int[2];
        salida[0]=positivos;
        salida[1]=negativos;
//        if(positivos>negativos && positivos>neutros){
//            return "positivo";
//        }
//        else if(negativos>positivos && negativos>neutros){
//            return "negativo";
//        }
//        else{
//            return "neutro";
//        }
        return salida;

    }


}

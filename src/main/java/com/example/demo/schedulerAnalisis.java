package com.example.demo;

import com.example.Analyzer.Classifier;
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



    @Scheduled(fixedRate = 10000)
    public void analizador() throws IOException {

        this.actualizarTweet();
//        this.analisisGeneral();
//
//        this.analisisEspecifico();

    }


    public void actualizarTweet(){
        Verificador buscador=new Verificador();
        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
        DB database = mongoo.getDB("TBDG7");
        DBCollection collection = database.getCollection("futbol");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject tweet = cursor.next();
            System.out.println("rtActual:"+ tweet.get("retweet").toString());
            String id = tweet.get("id").toString();
            System.out.println("llegue aca 1");
            Status status= buscador.buscar(id);
            System.out.println("llegue aca 2");
            DBObject updated = new BasicDBObject().append("$set", new BasicDBObject().append("retweet",status.getRetweetCount()));
            collection.update(tweet, updated);
            System.out.println("llegue aca 3");
            System.out.println("rtNuevo:"+ tweet.get("retweet").toString());
         }

    }



    public void analisisGeneral() throws IOException {

        MongoClient mongoClient= new MongoClient("138.197.128.130",27017);
        MongoClient mongoClient2= new MongoClient();
        DB db = mongoClient.getDB("twitter7");
        DB db2= mongoClient2.getDB("twitter7");
        DBCollection collection = db.getCollection("futbol");
        DBCollection collection2=db2.getCollection("futbol");

        DBCursor cursor = collection.find();
        System.out.println("llegue 1");
        ArrayList<Commune> comunas = (ArrayList<Commune>) communeRepository.findAll();
        ArrayList<Region> regiones= (ArrayList<Region>) regionRepository.findAll();
        System.out.println("llegue 2");
       List<Commune> comunasMetropolitana =  regiones.get(6).getCommune();
        System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
        System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
        System.out.println("region es :"+ regiones.get(6).getFirstName());

        System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
        System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
        Maps[] maps= new Maps[15];
        MapsSantiago[] mapsSantiago= new MapsSantiago[52];
        System.out.println("llegue aca1");
        Date date = new Date();
        long time = date.getTime();
        for (int i=0; i<comunasMetropolitana.size();i++){
            mapsSantiago[i]= new MapsSantiago();
            mapsSantiago[i].setFirstName(comunasMetropolitana.get(i).getFirstName());
            mapsSantiago[i].setId(comunasMetropolitana.get(i).getIdMaps());
            mapsSantiago[i].setNegative_value((long) 0);
            mapsSantiago[i].setPositive_value((long) 0);

        }
        for (int i=0;i<regiones.size();i++){
            maps[i]= new Maps();
            maps[i].setFirstName(regiones.get(i).getFirstName());
            maps[i].setId(regiones.get(i).getIdMaps());
            maps[i].setNegative_value((long) 0);
            maps[i].setPositive_value((long) 0);

        }
        int[] acumulador= new int[3];
        acumulador[0]=0;
        acumulador[1]=0;
        acumulador[2]=0;

        while (cursor.hasNext()) {
            DBObject tweet = cursor.next();

//            Respaldo bd mongo
           DBCursor cur2= collection2.find(new BasicDBObject("id", tweet.get("id")));
          if(!cur2.hasNext()){
              System.out.println("insertando nuevo dato");
              BasicDBObject tweet2;
              tweet2 = new BasicDBObject("id", tweet.get("id"))
                      .append("text", tweet.get("text"))
                      .append("like", tweet.get("like"))
                      .append("geoLocation", tweet.get("geoLocation"))
                      .append("retweet", tweet.get("retweet"))
                      .append("locationUser", tweet.get("locationUser"))
                      .append("name", tweet.get("name"))
                      .append("followers", tweet.get("followers"));
              collection2.insert(tweet2);
          }






            int region=0;
            // System.out.println(">>>>>"+tweet.get("locationUser").toString());
            String location = stripAccents(tweet.get("locationUser").toString().split(",")[0]).toLowerCase();
            HashMap<String,Double> resultado = classifier.classify(tweet.get("text").toString());
            for (Commune c: comunas) {

                if(location.equals(stripAccents(c.getFirstName()).toLowerCase())){
                       region=c.getRegion().getId().intValue()-1;
                    if (resultado.get("positive")> resultado.get("negative")){

                        maps[region].setPositive_value(maps[region].getPositive_value()+1);
                    }
                    else if(resultado.get("positive")< resultado.get("negative")){

                        maps[region].setNegative_value(maps[region].getNegative_value()+1);
                    }



                    break;
                }



            }

            for (int i=0 ; i< comunasMetropolitana.size();i++){
                Commune comuna= comunasMetropolitana.get(i);
                if(location.equals(stripAccents(comuna.getFirstName()).toLowerCase())){
                    //System.out.println("Esta comuna es :"+ location);
                    if (resultado.get("positive")> resultado.get("negative")){

                        mapsSantiago[i].setPositive_value(mapsSantiago[i].getPositive_value()+1);
                    }
                    else if(resultado.get("positive")< resultado.get("negative")){

                        mapsSantiago[i].setNegative_value(mapsSantiago[i].getNegative_value()+1);
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
        System.out.println(">>>>llegue aca2");
        for (Maps m: maps) {
            m.setLastUpdate(new Timestamp(time));
            mapsRepository.save(m);

        }
        System.out.println(">>>>llegue aca3");
        for(MapsSantiago mapsS:  mapsSantiago){
            mapsS.setLastUpdate(new Timestamp(time));
            mapsSantiagoRepository.save(mapsS);

        }

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
        clubRepository.save(equipo);
    }

    public void analisisEspecifico(){

        Iterable<Club>  clubs= clubRepository.findAll();
        Indice indice = new Indice();
        indice.indexar();

        for (Club equipo: clubs) {
            int[] acumulador= new int[3];
            acumulador[0]=0;
            acumulador[1]=0;
            acumulador[2]=0;


            if (equipo.getId() != 17){
                ArrayList<String> tweets;
                String busqueda = equipo.getName();
                for (Keyword apodo: equipo.getKeywords()) {
                    busqueda =busqueda+" "+apodo.getName_keyword();
                }

                System.out.println("%%%%% " + busqueda + "%%%%%%%");
                tweets = indice.buscar(busqueda);

                for (String tweet : tweets) {
                    HashMap<String, Double> resultado = classifier.classify(tweet);

                    if (resultado.get("positive") > resultado.get("negative")) {
                        acumulador[0] += 1;
                    } else if (resultado.get("positive") < resultado.get("negative")) {
                        acumulador[1] += 1;
                    } else {
                        acumulador[2] += 1;
                    }

                }

                Date date = new Date();
                long time = date.getTime();

                Statistics statistics = new Statistics();
                statistics.setPositive_value(acumulador[0]);
                statistics.setNegative_value(acumulador[1]);
                statistics.setNeutro_value(acumulador[2]);

                statistics.setLastUpdate(new Timestamp(time));
                statistics.setName_statics("estadistica de equipos");

                equipo.getStatistics().add(statistics);
                clubRepository.save(equipo);

                System.out.println("***************" + equipo.getName() + "******************************");
                System.out.println("*********************************************");
                System.out.println("El resultado es :" + acumulador[0] + "," + acumulador[1] + "," + acumulador[2]);
                System.out.println("*********************************************");
                System.out.println("*********************************************");
            }

        }
        System.out.println("*********************************************");
        System.out.println("*********************************************");
        System.out.println("Finalizoooooooo");
        System.out.println("*********************************************");
        System.out.println("*********************************************");
    }
}

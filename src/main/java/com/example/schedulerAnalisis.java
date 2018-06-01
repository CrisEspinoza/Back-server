package com.example;

import com.example.Analyzer.Classifier;
import com.example.Repositories.*;

import com.example.Analyzer.Indice;
import com.example.Entities.*;
import com.mongodb.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.stripAccents;


@Transactional
@Component
public class schedulerAnalisis {

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
    private Classifier classifier;


    @Scheduled(cron="0 0 * * * *")
    public void analizador() throws IOException {
        this.analisisGeneral();

        this.analisisEspecifico();

    }

    public void analisisGeneral() throws IOException {

        MongoClient mongoClient= new MongoClient("138.197.128.130",27017);
        DB db = mongoClient.getDB("twitter7");
        DBCollection collection = db.getCollection("futbol");
        DBCursor cursor = collection.find();

        ArrayList<Commune> comunas = (ArrayList<Commune>) communeRepository.findAll();
        ArrayList<Region> regiones= (ArrayList<Region>) regionRepository.findAll();
        Maps[] maps= new Maps[15];
        System.out.println("llegue aca1");
        Date date = new Date();
        long time = date.getTime();
        for (int i=0;i<regiones.size();i++){
            maps[i]= new Maps();
            maps[i].setFirstName(regiones.get(i).getFirstName());
            maps[i].setIdRegion(regiones.get(i).getId());
            maps[i].setNegative_value((long) 0);
            maps[i].setPositive_value((long) 0);

        }
        int[] acumulador= new int[3];
        acumulador[0]=0;
        acumulador[1]=0;
        acumulador[2]=0;

        while (cursor.hasNext()) {
            DBObject tweet = cursor.next();
            int region=0;
            // System.out.println(">>>>>"+tweet.get("text").toString());
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

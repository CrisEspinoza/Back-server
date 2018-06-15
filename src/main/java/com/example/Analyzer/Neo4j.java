package com.example.Analyzer;

import com.example.Entities.Club;
import com.example.Entities.Keyword;
import com.example.Entities.Tweet;
import com.mongodb.*;
import org.neo4j.driver.v1.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Neo4j {
    private Driver driver;
    private Session session;


    public void connect(String uri, String username, String password) {
        /*
            uriConnection = bolt://localhost
            username = neo4j
            password = root -> cambiar contraseña si usaron otra.
        */
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        this.session = driver.session();
    }

    public void disconnect() {
        session.close();
        driver.close();
    }

    public void deleteAll() {
        this.session.run("match (a)-[r]->(b) delete r");
        this.session.run("match (n) delete n");
    }

    public void  crearNodosEquipos( List<Club> equipos){

        for (Club club:equipos) {
            System.out.println("el equipo es :"+ club.getName());
            session.run("create (a:Club {name:'"+club.getName()+"'})");
        }

    }

    public void crearNodoUsuarios(){
        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
        DB database = mongoo.getDB("TBDG7");
        DBCollection collection = database.getCollection("futbol");


        ArrayList<String> registro = (ArrayList<String>) collection.distinct("name");
//
        for(String nombre: registro){


//            if (!registro.contains(nombre)) {
//                registro.add(nombre);
                nombre=nombre.replace("'","");
                nombre=nombre.replace("/","");
                nombre=nombre.replace("\"","");
                nombre=nombre.replace("_","");
                nombre=nombre.replace("¯(ツ)¯","");
                nombre=nombre.replace("|","");
                nombre=nombre.replace("°","");
                nombre=nombre.replace("¬","");
                nombre=nombre.replace("!","");
                nombre=nombre.replace("#","");
                nombre=nombre.replace("$","");
                nombre=nombre.replace("%","");
                nombre=nombre.replace("&","");
                nombre=nombre.replace("/","");
                nombre=nombre.replace("(","");
                nombre=nombre.replace(")","");
                nombre=nombre.replace("=","");
                nombre=nombre.replace("?","");
                nombre=nombre.replace("\\","");
                nombre=nombre.replace("¡","");
                nombre=nombre.replace("¿","");
                nombre=nombre.replace("@","");
                nombre=nombre.replace("*","");
                nombre=nombre.replace("+","");
                nombre=nombre.replace("~","");
                nombre=nombre.replace("{","");
                nombre=nombre.replace("}","");
                nombre=nombre.replace("[","");
                nombre=nombre.replace("]","");
                nombre=nombre.replace(";","");
                nombre=nombre.replace(",","");
                nombre=nombre.replace(":","");
                nombre=nombre.replace(".","");
                nombre=nombre.replace("_","");
                nombre=nombre.replace("-","");

                session.run("create (a:Usuario {name:'"+nombre+"'})");
//            }
        }
        System.out.println("Usuarios agregados");
        mongoo.close();
    }


    public String limpiar(String nombre){
        nombre=nombre.replace("'","");
        nombre=nombre.replace("/","");
        nombre=nombre.replace("\"","");
        nombre=nombre.replace("_","");
        nombre=nombre.replace("¯(ツ)¯","");
        nombre=nombre.replace("|","");
        nombre=nombre.replace("°","");
        nombre=nombre.replace("¬","");
        nombre=nombre.replace("!","");
        nombre=nombre.replace("#","");
        nombre=nombre.replace("$","");
        nombre=nombre.replace("%","");
        nombre=nombre.replace("&","");
        nombre=nombre.replace("/","");
        nombre=nombre.replace("(","");
        nombre=nombre.replace(")","");
        nombre=nombre.replace("=","");
        nombre=nombre.replace("?","");
        nombre=nombre.replace("\\","");
        nombre=nombre.replace("¡","");
        nombre=nombre.replace("¿","");
        nombre=nombre.replace("@","");
        nombre=nombre.replace("*","");
        nombre=nombre.replace("+","");
        nombre=nombre.replace("~","");
        nombre=nombre.replace("{","");
        nombre=nombre.replace("}","");
        nombre=nombre.replace("[","");
        nombre=nombre.replace("]","");
        nombre=nombre.replace(";","");
        nombre=nombre.replace(",","");
        nombre=nombre.replace(":","");
        nombre=nombre.replace(".","");
        nombre=nombre.replace("_","");
        nombre=nombre.replace("-","");
        return nombre;
    }

    public void relacionarTweet( Iterable<Club>  clubs){

        Indice indice = new Indice();
       indice.indexar();
        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
        DB database = mongoo.getDB("TBDG7");
        DBCollection collection = database.getCollection("futbol");


        ArrayList<String> registro = (ArrayList<String>) collection.distinct("name");
        System.out.println("registros listos");
        for (Club equipo: clubs) {
            int[] cantidades= new int[registro.size()];
            String[] followers= new String[registro.size()];
            System.out.println("llegue aca ");

            if (equipo.getId() != 17) {
                ArrayList<Tweet> tweets;
                String busqueda = equipo.getName();
                for (Keyword apodo : equipo.getKeywords()) {
                    busqueda = busqueda + " " + apodo.getName_keyword();
                }

                System.out.println("%%%%% " + busqueda + "%%%%%%%");
                tweets = indice.buscar(busqueda);
                for (Tweet tweet : tweets) {
                    int i= registro.indexOf(tweet.getName());
                    cantidades[i]=cantidades[i]+1;
                    followers[i]=tweet.getFollowers();

                }
                for (int i=0; i<registro.size();i++){
                    if(cantidades[i]>0){
                        String query = "match (a:Usuario) where a.name='" + limpiar(registro.get(i)) + "' "
                                + "  match (b:Club) where b.name='" + equipo.getName() + "' "
                                + "  create (a)-[r:Tweet {texto:'" + cantidades[i] + "'" + ", followers:'" +followers[i] + "'}]->(b)";
                        session.run(query);
                    }
                }

            }
        }
        System.out.println("club terminado");

    }

    public int[]  getInfluencia(String usuario,String club ){
            String query ="MATCH p=(u:Usuario)-[r:Tweet]->(c:Club) where u.name='"+limpiar(usuario)+"' and c.name='"+club+"' " +
                    "RETURN r.followers as seguidores, r.texto as cantidad";
            StatementResult nodo=session.run(query);
            int[] resultado = new int[2];
            if(nodo.hasNext()){
                Record record = nodo.next();
                String seguidores=  record.get("seguidores").asString();
                String cantidad= record.get("cantidad").asString();

                resultado[0] = Integer.parseInt(seguidores);
                resultado[1]= Integer.parseInt(cantidad);


            }else{
                resultado[0] =1;
                resultado[1]= 1;
            }

            return resultado;


    }
}

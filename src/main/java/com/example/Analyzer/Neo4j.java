package com.example.Analyzer;

import com.example.Entities.*;
import com.mongodb.*;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.constraints.Null;
import java.util.*;

public class Neo4j {
    private Driver driver;
    private Session session;
    @Autowired
    private Classifier classifier;

    public void connect(String uri, String username) {
        /*
            uriConnection = bolt://localhost
            username = neo4j
            password = root -> cambiar contraseña si usaron otra.
        */
        this.driver = GraphDatabase.driver(uri);
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
        MongoCredential credential = MongoCredential.createCredential("TBDG7A", "TBDG7-A", "antihackers3.0".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("178.128.222.125", 18117), Arrays.asList(credential));

        DB database = mongoo.getDB("TBDG7-A");
        DBCollection collection = database.getCollection("futbol");


        DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$name")
                .append("seguidores", new BasicDBObject("$avg", "$followers")));

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("seguidores", -1));
        DBObject limit= new BasicDBObject("$limit",1000);
        AggregationOutput output = collection.aggregate(group,sort,limit);
        int cantidad =output.hashCode();
        int i=0;
        for (DBObject result : output.results()) {
//            System.out.println(result);
            i++;
            session.run("create (a:Usuario {name:'"+limpiar(result.get("_id").toString())+"', followers:"+result.get("seguidores")+"})");
        }
//

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
        nombre=nombre.replace("AND","(and)");
        if(nombre.equals("AND Noticias")){
            nombre=nombre.replace("AND","aanndd");
        }
        return nombre;
    }

    public void relacionarTweet( Iterable<Club>  clubs){

        Indice indice = new Indice();
        indice.indexar();
//        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
//        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
//        DB database = mongoo.getDB("TBDG7");
//        DBCollection collection = database.getCollection("futbol");
        StatementResult nodo=session.run("MATCH (a:Usuario) RETURN a.name as name ");
        ArrayList<String> registro = new ArrayList<String>();
        while(nodo.hasNext()){
            Record record = nodo.next();
            String name=  record.get("name").asString();
            registro.add(name);



        }

        System.out.println("registros listos");



        for (Club equipo: clubs) {
            int[] cantidades= new int[registro.size()];

            System.out.println("llegue aca ");

            if (equipo.getId() != 17) {
                ArrayList<Tweet> tweets;
                String busqueda = equipo.getName();
                for (Keyword apodo : equipo.getKeywords()) {
                    busqueda = busqueda + " OR " + apodo.getName_keyword();
                }

                System.out.println("%%%%% " + busqueda + "%%%%%%%");
                for (int i=0; i<registro.size();i++){
                    if(registro.get(i).length()>0){
                        tweets = indice.buscarUsuario(registro.get(i),busqueda);
                        cantidades[i]=tweets.size();
                    }

//                    System.out.println("%%%%% Estoy buscando tweets para "+ registro.get(i)+" y eqipo"+equipo.getName()+"%%%%%%%%%%%%%");



                }
                System.out.println("%%%%% catidades listas $$$$");


                for (int i=0; i<registro.size();i++){
                    if(cantidades[i]>0){
                        String query = "match (a:Usuario) where a.name='" + registro.get(i) + "' "
                                + "  match (b:Club) where b.name='" + equipo.getName() + "' "
                                + "  create (a)-[r:Tweet {texto:" + cantidades[i]+ "}]->(b)";
                        session.run(query);
                    }
                }

            }
            System.out.println("equipo terminado");
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

    private Map<String, Object> mapTriple(String key1, Object value1, String key2, Object value2,String key3, Object value3) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }

    private Map<String, Object> mapQuadruple(String key1, Object value1, String key2, Object value2,String key3, Object value3,String key4,Object value4) {
        Map<String, Object> result = new HashMap<String, Object>(4);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        return result;
    }




    public List<Map<String, Object>> getUsuariosInfluyentes(String equipo){

        List<Map<String, Object>> lista = new ArrayList<>();
        String query ="MATCH p=(u:Usuario)-[r:Tweet]->(c:Club) where  c.name='"+equipo+"' " +
                "RETURN u.name as name, u.followers as seguidores, r.texto as cantidad";
        StatementResult nodo=session.run(query);

        while(nodo.hasNext())
        {
            Record record = nodo.next();

            lista.add(mapTriple("name", record.get("name").asString(), "seguidores", Double.parseDouble(String.valueOf(record.get("seguidores"))), "cantidad", Double.parseDouble(String.valueOf(record.get("cantidad"))) ));


        }

        return lista;
    }

    public List<UsuarioInfluyente> getInfluyentesEquipo(Club equipo,int limit){
        List<UsuarioInfluyente> lista = new ArrayList<UsuarioInfluyente>();
        String query ="MATCH p=(u:Usuario)-[r:Tweet]->(c:Club) where  c.name='"+equipo.getName()+"' " +
                "RETURN u.name as name, u.followers as seguidores, r.texto as cantidad ORDER BY r.texto DESC LIMIT "+limit;
        StatementResult nodo=session.run(query);



        while(nodo.hasNext())
        {

            Record record = nodo.next();
            double inf=  ((  Double.parseDouble(String.valueOf(record.get("seguidores")))/3327729.8865619544)*0.7+( Double.parseDouble(String.valueOf(record.get("cantidad")))/2000)*0.3);
            UsuarioInfluyente newNeoInf= new UsuarioInfluyente();
            newNeoInf.setName(record.get("name").asString());
            newNeoInf.setFollowers(Double.parseDouble(String.valueOf(record.get("seguidores"))));
//            newNeoInf.setCantidad(Double.parseDouble(String.valueOf(record.get("cantidad"))));
            newNeoInf.setInfluencia(inf);
            newNeoInf.setRazon("no definida");
            lista.add(newNeoInf);


        }
//
//        Collections.sort(lista, new Comparator<Map<String, Object>> () {
//            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
//                return ((Integer) m1.get("num")).compareTo((Integer) m2.get("num")); //ascending order
//                //return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num")); //descending order
//            }
//        });
        lista.sort(Comparator.comparing(UsuarioInfluyente::getInfluencia));
        for (UsuarioInfluyente ida:lista
             ) {
            System.out.println("se creo :"+ida.getName());
            System.out.println("se creo :"+ida.getRazon());
            System.out.println("se creo :"+ida.getFollowers());

        }

        return lista;
    }


    public ArrayList<UsuarioInfluyente> getUsuariosMasInfluyentes(){

        ArrayList<UsuarioInfluyente>lista = new ArrayList<UsuarioInfluyente>();
        String query ="MATCH (u:Usuario) RETURN u.name as name, u.followers as seguidores  ORDER BY u.followers DESC LIMIT 10";
        StatementResult nodo=session.run(query);

        while(nodo.hasNext())
        {
            Record record = nodo.next();
//            double inf=  1.0+((  Double.parseDouble(String.valueOf(record.get("seguidores")))/3335299.440625)*0.7+( Double.parseDouble(String.valueOf(record.get("cantidad")))/15)*0.3);
            UsuarioInfluyente newNeoInf= new UsuarioInfluyente();
            newNeoInf.setName(record.get("name").asString());
            newNeoInf.setFollowers(Double.parseDouble(String.valueOf(record.get("seguidores"))));
//            newNeoInf.setCantidad(Double.parseDouble(String.valueOf(record.get("cantidad"))));
//            newNeoInf.setInfluencia(inf);
            lista.add(newNeoInf);

        }

        return lista;
    }



}
# Taller2

GRUPO 7 - INTEGRANTES: 
- Veronica Calas
- Cristobal Donoso
- Maximiliano Espinosa
- Cristian Espinoza
- Jesus Mura
- Sebastian Pinto	

* Tenemos que este taller contiene dos carpetas por un lado tenemos que contiene Taller2-TBD y por el otro lado tenemos a Taller2-TBD-Front.

	* En primer lugar la carpeta Taller2-TBD:

		* Esta carpeta se encarga de contener el backend de la aplicación.
		* Para poder ejecutarla basta con colocar el comando "gradle bootrun".
		* Ahora si desea verifcar los servicios por serapado del front-end, basta con colocar estas url en el Postman: 
 
- Metodos Get 

	* http://localhost:8081/actors/x/films -> Este metodo retorna las peliculas que participa el actor x.
	* http://localhost:8081/films/x/actors -> Este metodo retorna los actores que participan en la pelicula x.

 - Metodos Post

	* http://localhost:8081/actors/x/films/y -> Este metodo relaciona al actor x con la pelicula y, en la tabla intermedia que se genero. 
	* http://localhost:8081/films/x/actors/y -> Este metodo relaciona a la pelicula x con el actor y, en la tabla intermedia que se genero.

* Por ultimo, tenemos la carpeta Taller2-TBD-Front:
	* En esta carpeta tenemos localizado el front-end del taller 2.
	* Para poder ejecutar esta aplicación tenemos que seguir esta secuencia de pasos:
		* Entrar a la carpeta plantilla-vue
		* Instalar las dependencias con el siguiente comando: npm install
		* Ejecutar la aplicación con el comando: npm run dev.



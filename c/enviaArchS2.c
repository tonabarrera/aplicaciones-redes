#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>//exit
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <libgen.h>//basename() dirname()
#define PUERTO "8000"

// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }

    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}  


  int main(int argc, char* argv[]){
  int sd,n,v=1,rv,op=0;
  socklen_t ctam;
  char s[INET6_ADDRSTRLEN], hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];
  //struct sockaddr_in serverADDRESS;
  //struct hostent *hostINFO;


  FILE *f,*f1;
 struct addrinfo hints, *servinfo, *p;
 struct sockaddr_storage their_addr; // connector's address 
 struct stat st;
 ctam= sizeof(their_addr);
 memset(&hints, 0, sizeof (hints));  //indicio
 hints.ai_family = AF_INET6;    /* Allow IPv4 or IPv6  familia de dir*/
 hints.ai_socktype = SOCK_STREAM;
 hints.ai_flags = AI_PASSIVE; // use my IP
 hints.ai_protocol = 0;          /* Any protocol */
 hints.ai_canonname = NULL;
 hints.ai_addr = NULL;
 hints.ai_next = NULL;

 if ((rv = getaddrinfo(NULL, PUERTO, &hints, &servinfo)) != 0) {
     fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
     return 1;
 }//if

    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }

        if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &v,sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }

	if (setsockopt(sd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1) {
            perror("setsockopt   no soporta IPv6");
            exit(1);
        }

        if (bind(sd, p->ai_addr, p->ai_addrlen) == -1) {
            close(sd);
            perror("server: bind");
            continue;
        }
	 printf("Servicio iniciado... esperando cliente\n");
        break;
    }

    freeaddrinfo(servinfo); // all done with this structure

    if (p == NULL)  {
        fprintf(stderr, "servidor: error en bind\n");
        exit(1);
    }
            listen(sd, 5);
            socklen_t clientADDRESSLENGTH;
            struct sockaddr_in clientADDRESS;
	  for(;;){  
	    int cd,enviados=0;
            clientADDRESSLENGTH = sizeof(clientADDRESS);
            cd = accept(sd, (struct sockaddr *)&their_addr, &ctam);
            if (cd == -1) {
              perror("accept");
              exit(1);
             }//if
            if (getnameinfo((struct sockaddr *)&their_addr, sizeof(their_addr), hbuf, sizeof(hbuf), sbuf,sizeof(sbuf), NI_NUMERICHOST | NI_NUMERICSERV) == 0)
               printf("cliente conectado desde %s:%s\n", hbuf,sbuf);
  	    f1= fdopen(cd,"w+"); 
	    struct linger linger;
            linger.l_onoff = 1;
            linger.l_linger = 30;
            if(setsockopt(cd,SOL_SOCKET, SO_LINGER,(const char *) &linger,sizeof(linger))==-1){
               perror("setsockopt(...,SO_LINGER,...)");
            }//if    
	    //char *arch1="/home/escom/Escritorio/aplicaciones/flujo/duke1.png", *arch2="duke2.png";
	    char *arch1="/home/usuario/Escritorio/duke1.png";
            char buffer[100];
            memset((char *)&buffer,0,sizeof(buffer));
	    char* ts1 = strdup(arch1);
            char* ts2 = strdup(arch1);
            char* dir = dirname(ts1);
            char* nombre = basename(ts2);
            if (f = fopen(arch1, "r+"))//rt
             {   //fread
              if(stat(arch1,&st)!=0){
                 printf("TTam arch: %ld \n", st.st_size);
	      }//if
	      long sz = st.st_size;
	      //int prev=ftell(f);//medimos el tamaño del archivo
              //fseek(f,0L,SEEK_END);
              //long sz = (long)ftell(f);
              //fseek(f,prev,SEEK_SET);
              printf("El archivo a leer mide %ld bytes\n",sz);
  	      char msj[50];
	      memset(&msj,0,sizeof(msj));
	      sprintf(msj,"1_%s;%ld",nombre,sz);
              enviados=write(cd,msj,strlen(msj)+1);
	      fflush(f1);
	      //printf("Presiona enter para continuar...\n");
	      //getchar();
              printf("Se escribio %s \n",msj);
              if(enviados<0){
                perror("No se pudo mandar el tamanio del archivo..\n");
                return 1;
              }//if
	      printf("Se envio cadena de %d bytes\n",enviados);
              int l;
	      char ok[3];
	      bzero(ok, sizeof(ok));
	      n=read(cd, ok, sizeof(ok));
              long leidos=0, escritos=0;
              while(leidos<sz){
                 memset((char *)&buffer,0,sizeof(buffer));
                 l=fread(buffer, 1, sizeof(buffer), f);
                 leidos = leidos + l;
	         int es=write(cd,buffer,l);
	         if(es<0){
		   perror("No se pudo mandar el buffer de datos\n");
		   return 1;
	         }//if
	         escritos = escritos + es;
	         fflush(f1);
	         printf("\rEnviados: %ld de %ld bytes..",escritos,sz);
             }//while
             fclose(f1);
             fclose(f);
	     printf("\nArchivo enviado.. \n");
	     close(cd);
        }//if fread
	}//for;;    
            close(sd);

return 0;
}//main

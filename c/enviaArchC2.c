#include <netdb.h>  //getaddrinfo() getnameinfo() freeaddrinfo()
#include <netinet/in.h> //htons
#include <string.h>  //bzero
#include <stdio.h>   //printf perror
#include <stdlib.h> //atoi() exit()
#include <sys/types.h>
#include <unistd.h>  //exit



  
  int main(int argc, char* argv[]){
  int cd,n,rv,op=0;
  char *PUERTO="8000";
  long tam;
  struct sockaddr_in serverADDRESS;
  struct hostent *hostINFO;
  char *srv="2001::1234:0001";
  FILE *f;  

int status;
struct addrinfo hints, *servinfo, *p;

    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;    /* Allow IPv4 or IPv6  familia de dir*/
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = 0;

    if ((rv = getaddrinfo(srv, PUERTO, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((cd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("client: socket");
            continue;
        }

	/*if (setsockopt(cd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1) {
            perror("setsockopt   no soporta IPv6");
            exit(1);
        }*/

        if (connect(cd, p->ai_addr, p->ai_addrlen) == -1) {
            close(cd);
            perror("client: connect");
            continue;
        }

        break;
    }//for

    if (p == NULL) {
        fprintf(stderr, "client: error al conectar con el servidor\n");
        return 2;
    }

    freeaddrinfo(servinfo); // all done with this structure





printf("\n Conexion establecida.. recibiendo datos..\n");   
     	    char nombre[100];
	    memset(nombre,0,sizeof (nombre));
	    int recibidos=0;
	    char ta[50];
	   memset(ta,0,sizeof(ta));
            recibidos=read(cd,ta,sizeof(ta));
            if(recibidos<0){
             perror("No se pudo leer el tamanio del archivo..\n");
             return 1;
            }//if
	   int ttt = strlen(ta);
	   printf("Se recibio cadena de %d bytes  %d\n",recibidos,ttt);
	   char *tok=strtok(ta,";");
	   char *tok1=strtok(NULL,";");
	   printf("\nt1: %s\n",tok);
           printf("t1: %s\n",tok1);
	   tam=atol(tok1);
          // printf("\nTamanio del archivo: %d,  %s \n",taa,tok);
	   printf("Recibiendo archivo...\n");
	  int porcentaje=0;
          long a=0;
	  char buffer[100];
	  char *ok="ok";
	  n= write(cd,ok,strlen(ok)+1);
        if (f = fopen(tok, "w+"))//rt
        { 

	  while(a<tam){
	    memset((char *)&buffer,0,sizeof(buffer));
	    int ll=read(cd,buffer,sizeof(buffer));
           if(ll<0){
             perror("No se pudieron leer los datos del archivo..");
             return 1;
            }//if
	   a = a + ll;
           fwrite(buffer,1,ll,f);
            fflush(f);
	   porcentaje =(int)((a*100)/tam);
	   printf("\rrecibidos: %ld   tam: %ld  porcentaje: %d\r",a,tam,porcentaje);

	  }//while
	  printf("\n");
          fclose(f);
          close(cd);
}//fopen
return 0;
}//main

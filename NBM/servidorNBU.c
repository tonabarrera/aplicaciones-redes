/**sudo ifconfig eno1 inet6 add 2004::1234:1 */
#include <fcntl.h>
#include <stdio.h> // 
#include <string.h>  //
#include <unistd.h>  //  
#include <netinet/in.h> // 
#include <sys/types.h> //  
#include <sys/socket.h>  //
#include <stdlib.h>//exit
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()
#define puerto "9930"  
#define MAXBUF 1024  

void error(const char * msj){
 perror(msj);
 exit (1);
}//error

struct dato{
 char nombre[30];
 char apellido[25];
 int edad;
};

/*************************** 
**server for multi-client  
**PF_SETSIZE=1024 
****************************/  
int main(int argc, char** argv)  {  
 FILE *f;
 int sd,cd,n,n1,v=1,rv,op=0,i,nready,maxi = -1,sockfd,maxfd=-1;   
 socklen_t ctam;
 char hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];
 struct addrinfo hints, *servinfo, *p;
 struct sockaddr_storage emisor; 
 ctam= sizeof(emisor);
 struct timeval tv;
 fd_set a1,a,b1,b;   
 char buf[1000];
 //CLIENT client[FD_SETSIZE];  
 memset(&hints, 0, sizeof (hints));  //indicio
 hints.ai_family = AF_INET6;    /* Allow IPv4 or IPv6  familia de dir*/
 hints.ai_socktype = SOCK_DGRAM;
 hints.ai_flags = AI_PASSIVE; // use my IP
 hints.ai_protocol = 0;          /* Any protocol */
 hints.ai_canonname = NULL;
 hints.ai_addr = NULL;
 hints.ai_next = NULL;

 if ((rv = getaddrinfo(NULL, puerto, &hints, &servinfo)) != 0) {
     fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
     return 1;
 }//if

    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }//if

        if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &v,sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }//if

		if (setsockopt(sd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1) {
            perror("setsockopt   no soporta IPv6");
            exit(1);
        }//if

        if (bind(sd, p->ai_addr, p->ai_addrlen) == -1) {
            close(sd);
            perror("server: bind");
            continue;
        }//if

        break;
    }//for

    freeaddrinfo(servinfo);
    if (p == NULL)  {
        fprintf(stderr, "servidor: error en bind\n");
        exit(1);
    }//if
    
    int flags = fcntl(sd, F_GETFL, 0);
    fcntl(sd, F_SETFL, flags | O_NONBLOCK);
    FD_ZERO(&a);
    FD_ZERO(&a1);
    //FD_ZERO(&b);
    //FD_ZERO(&b1);             
    FD_SET(sd, &a);    
    maxfd = sd;    
    printf("Servidor listo para recibir datagramas...\n");
	      
    while (1){         
        a1 = a;
		b1 = b;            
        tv.tv_sec = 1;  
        tv.tv_usec = 0;  
        nready = select(maxfd + 1, &a1, &b1, NULL, &tv);  
        if(nready == 0){
        	continue;	
		}  
        else if(nready < 0){  
            printf("error en funcion select()!\n");  
            break;  
        }
		else{  
            if(FD_ISSET(sd,&a1)){ // nuevo datagrama               
                if((n = recvfrom(sd,buf,sizeof(buf),0,(struct sockaddr *)&emisor,&ctam)) == -1){  
                    perror("recvfrom() error\n");  
                    continue;  
                }//if
				
				struct dato *o2 = (struct dato *)buf;
  				i = getnameinfo((struct sockaddr *) &emisor,ctam, hbuf, NI_MAXHOST,sbuf, NI_MAXSERV, NI_NUMERICSERV|NI_DGRAM);
       			
				if (i == 0){
            	printf(" %ld bytes recibidos desde %s puerto%s\n datos:\n",(long) n, hbuf, sbuf);
       			printf("Este es el mensaje:");
       			printf("\nNombre: %s",o2->nombre);
       			printf("\nApellido: %s",o2->apellido);
       			printf("\nEdad: %d\n\n",ntohl(o2->edad));
       			}
				else{
		     		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(i));
        	     	exit(EXIT_FAILURE);
       			}//else
	    	}//if
      	}//else      
    }//while  
    close(sd);
    return 0;  
}//main  

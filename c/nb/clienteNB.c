#include <fcntl.h>
#include <stdio.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/socket.h>  
#include <resolv.h>  
#include <stdlib.h>  
#include <netinet/in.h>  
#include <arpa/inet.h>  
#include <unistd.h>  
#include <sys/time.h>  
#include <sys/types.h>  
  
#define MAXBUF 1024  
// sudo ifconfig inet6 add 2001::1234:1
int main(int argc, char **argv)  
{  
        int sockfd, len;  
        struct sockaddr_in dest;  
        char buf[MAXBUF + 1];  
        fd_set rfds;  
        struct timeval tv;  
    int retval, maxfd = -1;  
  
    if (argc != 3) {  
            printf("Usage: %s IP Port",argv[0]);  
            exit(0);  
        }  
  
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {  
            perror("Socket");  
            exit(errno);  
        }  
  
    int val=1;
   int op = setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &val, sizeof(val));
   if(op<0){
   perror("No se modifico opcion de socket\n");
   }
    //int flags = fcntl(sockfd, F_GETFL, 0);
    //fcntl(sockfd, F_SETFL, flags | O_NONBLOCK);

    bzero(&dest, sizeof(dest));  
        dest.sin_family = AF_INET;  
        dest.sin_port = htons(atoi(argv[2]));  
        if (inet_aton(argv[1], (struct in_addr *) &dest.sin_addr.s_addr) == 0) {  
            perror(argv[1]);  
            exit(errno);  
        }  
  
    if(connect(sockfd, (struct sockaddr *) &dest, sizeof(dest)) != 0) {  
            perror("Connect ");  
            exit(errno);  
        }  
  
    printf("connect to server...\n");         
    while (1)   
    {  
  
                    FD_ZERO(&rfds);             
                    FD_SET(0, &rfds);  
                    maxfd = 0;  
              
                    FD_SET(sockfd, &rfds);  
                    if (sockfd > maxfd)  
                        maxfd = sockfd;  
              
                    tv.tv_sec = 1;  
                    tv.tv_usec = 0;  
            
                    retval = select(maxfd + 1, &rfds, NULL, NULL, &tv);  
  
            if (retval == -1)   
            {  
                        printf("select error! %s", strerror(errno));                
                break;  
                } else if (retval == 0) {  
                        //printf("no msg,no key, and continue to wait……\n");   
                        continue;  
                    } else {  
                if (FD_ISSET(0, &rfds))   
                {                  
                                bzero(buf, MAXBUF + 1);  
                                fgets(buf, MAXBUF, stdin);                
                                if (!strncasecmp(buf, "quit", 4))   
                    {  
                                    printf("request terminal chat!\n");  
                                    break;  
                                }  
                                len = send(sockfd, buf, strlen(buf) - 1, 0);  
                                if (len > 0)  
                                    printf("msg:%s send successful,totalbytes: %d!\n", buf, len);  
                                else {  
                                    printf("msg:'%s  failed!\n", buf);  
                                    break;  
                                }  
                        }  
                        else if (FD_ISSET(sockfd, &rfds))   
                {   
                                bzero(buf, MAXBUF + 1);  
                                len = recv(sockfd, buf, MAXBUF, 0);  
                                if (len > 0)  
                                    printf("recv:'%s, total: %d \n", buf, len);  
                                else    
                    {  
                        if (len < 0)   
                                            printf("recv failed!errno:%d,error msg: '%s'\n", errno, strerror(errno));  
                                    else  
                                            printf("other exit,terminal chat\n");  
                                    break;  
                    }  
                            }                 
            }  
    }  
  
    close(sockfd);  
    return 0;  
} 

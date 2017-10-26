#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <pthread.h>

int fd[2];//File descriptor for creating a pipe

//This function continously reads fd[0] for any input data byte
//If available, prints

void *reader()
{
    while(1){
       char    ch;
       int     result;

       result = read (fd[0],&ch,1);
       if (result != 1) {
            perror("read");
            exit(3);
    }    printf ("Reader: %c\n", ch);  }
}

//This function continously writes Capital Alphabet into fd[1]
//Waits if no more space is available

void *writer_ABC()
{
     int     result;
     char    ch='A';

     while(1){
            // Si regrea 0  esta cerrado =(
           result = write (fd[1], &ch,1);
           if (result != 1){
               perror ("write");
               exit (2);
           }

           printf ("Writer_ABC: %c\n", ch);
           if(ch == 'Z')
              ch = 'A'-1;

           ch++;
      }
}

//This function continously writes small Alphabet into fd[1]
//Waits if no more space is available

void *writer_abc()
{
  int     result;  char    ch='a';

  while(1){
      result = write (fd[1], &ch,1);
      if (result != 1){
            perror ("write");
            exit (2);
      }

      printf ("Writer_abc: %c\n", ch);
      if(ch == 'z')
            ch = 'a'-1;

     ch++;
  }
}

int main()
{
   pthread_t       tid1,tid2,tid3;
   int             result;

   // pipe(arreglo de descriptores)
   result = pipe (fd);
   if (result < 0){
       perror("pipe ");
       exit(1);
   }

 pthread_create(&tid1,NULL,reader,NULL);
 pthread_create(&tid2,NULL,writer_ABC,NULL);
 pthread_create(&tid3,NULL,writer_abc,NULL);

 pthread_join(tid1,NULL);
 pthread_join(tid2,NULL);
 pthread_join(tid3,NULL);
}
Railfence Cypherbreaker by Niks Gurins, G00315379.


This application was developed as a projcet to decypher a railfence encrypted 
text using brute force.


It works as follows:


User input or a file are read and are encrypted by a key defined in the Runner class.

The cypherbreaker is instantiated and a worker thread is made for every possible key,
ranging from 2 (has to atleast or text wont be encrypted) all the way up until
the length of the cypher text/2 (has to have atleast one V shape).

Each worker thread (decryptor) will decrypt the code for a key that it is given and 
score it using the TextScorer class which rates how "englishy" is the decyphered text
and score it accordingly. The higher the score, the more english sounding the text.

The worker thread will then put the decrypted text, key and score in a result object
which is then put onto the blocking queue cypherbreakers queue thread.

In the thread holding the queue, a result is popped from the queue and its score compared
with the local result variable in cypherbreaker. If the new score is higher, the local variable
is replaced. 

When a result is taken off the queue, an increment method is called which checks if all the
results produced are taken off the queue and a poison object is added to kill it (return).
Once the poison object is found, the highest scoring result will be output along with the key
and its plaintext, which is hopefully the same text and key that was encrypted at the start.




I decided to create a StringTokenizer for the encryption of the text as it would allow for 
many symbols to be in the text as well as let the text be lower case and still work.
I tried encrypting and breaking the cypher for a book that's included (3 little pigs) which
had to create approx. 3000 threads and was rather slow. I tried to optimize the whole process 
by having the threads put into a queue of a max of 10 at once, which didn't help.


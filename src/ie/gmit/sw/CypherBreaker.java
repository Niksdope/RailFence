package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

public class CypherBreaker {
	private static final int MAX_QUEUE_SIZE = 100;
	private BlockingQueue<Resultable> queue;
	private Map<String, Double> map = new ConcurrentHashMap<String, Double>();
	private String cypherText;
	private int counter = 2;
	private Object lock = new Object();
	private Resultable result = new Result("", 0, -10000.00);
	
	public CypherBreaker(String cypherText, Map<String, Double> map){
		queue = new ArrayBlockingQueue<Resultable>(MAX_QUEUE_SIZE);
		this.map = map;
		this.cypherText = cypherText;
		init();
	}
	
	public void increment() {
		synchronized (lock) // Synchronized code block using an object lock
		{
			counter++;
			if(counter == cypherText.length()/2) // Once every produced result taken from queue, add poison
			{
				try {
					queue.put(new PoisonResult());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	// A method used to print the highest scoring result at the end
	public void printHighestScore(){
		System.out.println("Highest score: " + result.getScore());
		System.out.println("Plaintext for highest score: " + result.getPlainText());
		System.out.println("Key for highest score: " + result.getKey());
	}
	
	public void init(){
		for(int i = 2; i <= cypherText.length()/2; i++){
			new Thread(new Decryptor(queue, cypherText, i, map)).start();
		}
		
		new Thread(new Runnable() {
			public void run(){
				
				while(!queue.isEmpty()){
					try {
						Thread.sleep(200); // Make the thread wait before a result is taken from the queue
						Resultable r = queue.take();
						System.out.println("Score for key " + r.getKey() + " is " + r.getScore());
						//Thread.sleep(200);
						if (r instanceof PoisonResult)
						{
							// Once PoisonResult is taken from queue, exit and print highest score
							printHighestScore();
							return;
						}
						
						if (result.getScore() < r.getScore()) // If new result has higher score than old, replace it
						{
							result = r;
							System.out.println("New highestScore is " + result.getScore() + " achieved with the key " + result.getKey());
						}
						
						increment(); // Keep track of each result taken from queue
					} catch (InterruptedException e){
						e.printStackTrace();
					}
				}
				
				System.out.println("Exiting queue thread ");
			}
		}).start();
	}
}

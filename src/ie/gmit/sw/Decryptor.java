package ie.gmit.sw;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Decryptor implements Runnable {
	private BlockingQueue<Resultable> queue;
	private String cypherText;
	private int key;
	private Map<String, Double> map = new ConcurrentHashMap<String, Double>();
	
	public Decryptor(BlockingQueue<Resultable> queue, String cypherText, int key, Map<String, Double> map) {
		super();
		this.map = map;
		this.queue = queue;
		this.cypherText = cypherText;
		this.key = key;
	}

	public void run(){
		String plainText = new RailFence().decrypt(cypherText, key);
		TextScorer ts = new TextScorer(map);
		double score = ts.getScore(plainText); // Score how englishy the decyphered text is
		
		System.out.println("Started thread for key " + key);
		Resultable r = new Result(plainText, key, score); // Make a result of all the info
		try {
			queue.put(r); // Put the result in the BlockingQueue
			System.out.println("Result for key " + r.getKey() + " put into queue");
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.println("Thread for key " + key + " completed");
	}

}

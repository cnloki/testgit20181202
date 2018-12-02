package testInterview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;

public class test {

	public static void main(String[] args) {
		Queue<String> buffer=new LinkedList<String>(); 
		int maxSize=3;
		
		Producer p=new Producer(buffer,maxSize,"Producer");
		customer c=new customer(buffer, maxSize, "customer");
		p.start();
		c.start();
	}
}

class Producer extends Thread{

	private Queue<String> queue;
	private int maxSize;
	private String threadName;
	public Producer(Queue<String> buffer,int maxSize,String threadName) {
		this.queue=buffer;
		this.maxSize=maxSize;
		this.threadName=threadName;
	}
	
	@Override
	public void run() {
		while(true) {
			synchronized(queue) {
				if(queue.size()==maxSize) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//读数据
				try {
					BufferedReader br =new BufferedReader(new FileReader("C:\\Users\\Administrator\\Desktop\\read.txt"));
					String s;
					while((s=br.readLine())!=null) {
						System.out.println("数据装入仓库....."+s);
						queue.add(s);
					}
					br.close();
					queue.notifyAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}

class customer extends Thread{

	private Queue<String> queue;
	private int maxSize;
	private String threadName;
	
	public customer(Queue<String> buffer,int maxSize,String threadName) {
		this.queue=buffer;
		this.maxSize=maxSize;
		this.threadName=threadName;
	}
	@Override
	public void run() {
		while(true) {
			synchronized(queue) {
				if(queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//写数据
				try {
					BufferedWriter br =new BufferedWriter(new FileWriter("C:\\Users\\Administrator\\Desktop\\write.txt"));
					while(queue.peek() != null) {
						System.out.println("写.......");
						br.write(queue.poll());
						br.newLine();
						br.flush();
					}
					br.close();
					queue.notifyAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}

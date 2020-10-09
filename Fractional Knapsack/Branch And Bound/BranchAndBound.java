package knapsack.Integral;

import java.util.Arrays;
import java.util.Scanner;
import java.util.PriorityQueue;

class Item implements Comparable<Item>{
	long weight;
	long value;
	double ratio;
	
	static public long maxValue(Item[] list, long totalWeight, int index) {
		long current = 0;
		long currentWeight = 0;
		for(int i = index; i < list.length; ++i) {
			if(currentWeight + list[i].weight >= totalWeight) {
				current += (long)((totalWeight - currentWeight) * list[i].ratio);
				break;
			}
			current += list[i].value;
			currentWeight += list[i].weight;
		}
		return current;
	}
	
	public int compareTo(Item item) {
		if(this.ratio > item.ratio) {
			return -1;
		}
		else if(this.ratio < item.ratio) {
			return 1;
		}
		else {
			return 0;
		}
	}
}

class Node implements Comparable<Node>{
	long weight;
	long value;
	long upperBound;
	int index;
	
	Node(long w, long v, long u, int i){
		weight = w;
		value = v;
		upperBound = u;
		index = i;
	}
	
	public int compareTo(Node n) {
		if(this.upperBound > n.upperBound) {
			return -1;
		}
		else if(this.upperBound < n.upperBound) {
			return 1;
		}
		else {
			return 0;
		}
	}
}

public class BranchAndBound {
	static int count = 1;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Number of elements : ");
		int n = Integer.parseInt(scanner.next());
		Item[] itemList = new Item[n];
		
		System.out.print("Value : ");
		for(int i = 0; i < n; ++i) {
			itemList[i] = new Item();
			itemList[i].value = Long.parseLong(scanner.next());
		}
		
		System.out.print("Weights : ");
		for(int i = 0; i < n; ++i) {
			itemList[i].weight = Long.parseLong(scanner.next());
			itemList[i].ratio = (double)itemList[i].value/(double)itemList[i].weight;
		}
		
		System.out.print("Weight of knapsack : ");
		long weight = Long.parseLong(scanner.next());
		
		Arrays.sort(itemList);
		long result = branchAndBound(itemList, weight);
		
		System.out.println("Maximum value of the knapsack is : " + result);
		scanner.close();
	}
	
	static long branchAndBound(Item[] list, long totalWeight) {
		//declaring the class here as it is not useful outside this function
		//put it outside because it became quite big
		
		long upperBound = Item.maxValue(list, totalWeight, 0);
		long maxResult = -1;
		Node root = new Node(0, 0, upperBound, 0);
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(root);
		
		while(!queue.isEmpty()) {
			Node top = queue.poll();
			//printState(top);
			if(top.upperBound > maxResult) {
				if(top.index == list.length) {
					maxResult = top.value;
				}
				else {
					Item element = list[top.index];
					if(top.weight + element.weight <= totalWeight) {
						queue.add(new Node(top.weight + element.weight, top.value + element.value, top.upperBound, top.index + 1));
					}
					long newBound = Item.maxValue(list, totalWeight - top.weight, top.index + 1) + top.value;
					queue.add(new Node(top.weight, top.value, newBound, top.index + 1));
				}
			}
			//discard otherwise
		}
		return maxResult;
	}
	
	static void printState(Node e) {
		System.out.println("Node " + count++ + " : ");
		System.out.println("Weight = " + e.weight);
		System.out.println("Value = " + e.value);
		System.out.println("UpperBound = " + e.upperBound);
		System.out.println("Level = " + e.index + 1);
		System.out.println();
	}
}

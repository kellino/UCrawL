package searchEngine;

import java.util.ArrayList;

public class PostingList {
	//int wordID;
	private String word;
	private int totalfrequency;
	private ArrayList<Post> postList;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getTotalfrequency() {
		return totalfrequency;
	}
	public void setTotalfrequency(int totalfrequency) {
		this.totalfrequency = totalfrequency;
	}
	public ArrayList<Post> getPostList() {
		return postList;
	}
	public void setPostList(ArrayList<Post> postList) {
		this.postList = postList;
	}
	public PostingList(String word){
		this.word = word;
		this.totalfrequency = 0;
		postList = new ArrayList<Post>();
		
	}
	public void addpost(Post post){
		postList.add(post);
	}
	
	
	
	
}

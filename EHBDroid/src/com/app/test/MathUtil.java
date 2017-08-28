package com.app.test;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
	
	//对n个元素进行排列，获取m个排列。例如5个元素总共有120个全排列，当N取5时，随机返回5个排列。
	public static List<List<Integer>> getNPermutation(int N, int elements){
		ArrayList<List<Integer>> ll = new ArrayList<List<Integer>>();
		List<List<Integer>> sortInt = sortInt(elements);
		List<Integer> random = random(N,sortInt.size());
		for(Integer integer:random)
			ll.add(sortInt.get(integer));
		return ll;
	}
	
	//获得n个数中m个数。比如输入(5,100)，返回100以内的随机5个数。 
	private static List<Integer> random(int m, int n){
		ArrayList arr = new ArrayList<Integer>();
		int min = m<n?m:n;
		for(int t = 0;arr.size()<min;t++){
			int index = (int) (Math.random()*n);
			if(!arr.contains(index))
				arr.add(index);
		}
		return arr;
	}
	
	//获得[0,i)的全排列
	public static List<List<Integer>> sortInt(int i){
		ArrayList<Integer> integers = new ArrayList<Integer>();
		for(int t=0;t<i;t++){
			integers.add(t);
		}
		return sort(integers);
	}
	
	//获得列表list的全排列
	public static List<List<Integer>> sort(ArrayList<Integer> list){
		ArrayList<List<Integer>> ll = new ArrayList<List<Integer>>();
		if(list.size()==1){
			ll.add(list);
			return ll;
		}
		for(Integer i:list){
			ArrayList clone = (ArrayList)list.clone();
			clone.remove(i);
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(i);	
			for(List<Integer> list2:sort(clone)){
				ArrayList<Integer> clone2 = (ArrayList<Integer>)arrayList.clone();
				clone2.addAll(list2);
				ll.add(clone2);
			}
		}
		return ll;
	}
}

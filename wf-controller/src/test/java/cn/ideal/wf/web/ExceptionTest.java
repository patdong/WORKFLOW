package cn.ideal.wf.web;

import cn.ideal.wfpf.exception.WfNotFoundException;

public class ExceptionTest {
	
	public static void main(String[] args) {
		int[] b = new int[]{1,2,3};
		String c = "kkkk";
		System.out.println(c.hashCode());
		for(int i=0;i<b.length;i++){
			if(b[i] >1){
				try{
					throw new WfNotFoundException("");
				}catch(WfNotFoundException e){
					throw new WfNotFoundException("");
				}
			}
			
			System.out.println(b[i]);
		}
	}

}

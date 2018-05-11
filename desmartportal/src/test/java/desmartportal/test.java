/**
 * 
 */
package desmartportal;

/**  
* <p>Title: test</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年5月10日  
*/
public class test {
	public static void main(String[] args) {
		Integer num = 0;
		long starttime = System.currentTimeMillis();
		for(int i=0; i<1000000000; i++) {
		//	String.valueOf(num); //261
			num.toString(); // 5503
		}
		
		long endtime = System.currentTimeMillis();
		
		System.out.println(endtime-starttime);
	}
}

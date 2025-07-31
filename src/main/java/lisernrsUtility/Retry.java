package lisernrsUtility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{

    private int retryCount = 0;
    private final int maxRetryCount = 3;
    
	@Override
	public boolean retry(ITestResult result) {
		 if (retryCount < maxRetryCount) {
	            retryCount++;
	            
	            // Retry the test
	            return true;  
	        }
		 
		 	// Do not retry further
	        return false;    
	 }	
}

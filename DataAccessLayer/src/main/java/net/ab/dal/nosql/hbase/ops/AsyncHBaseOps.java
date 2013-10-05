package net.ab.dal.nosql.hbase.ops;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.ab.dal.nosql.hbase.config.HBaseProperties;

import org.apache.log4j.Logger;

public class AsyncHBaseOps {
	static Logger log = Logger.getLogger(AsyncHBaseOps.class);
	static AsyncHBaseOps asyncOps = null;
	static ExecutorService exServ = null;
	public static AsyncHBaseOps getInstance() {
		if(asyncOps==null){
			int aTH = 2;
			try{
				aTH = Integer.parseInt(HBaseProperties.getInstance().getProperty("async.threadpool.size"));
			} catch( Exception e){
				// the thread count is defaulted to 2 
				aTH = 2;
			}
			exServ = Executors.newFixedThreadPool(aTH);
			asyncOps = new AsyncHBaseOps();
			
			// Adding a Shutdown Hook So this ThreadServr is terminated. 
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() {
			    	exServ.shutdown();
			        try {
						if (!exServ.awaitTermination(20,TimeUnit.SECONDS)) { 
						    log.warn("Executor did not terminate in the specified time."); 
						    List<Runnable> droppedTasks = exServ.shutdownNow(); 
						    log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); 
						} else {
							log.info("Executor Service Terminated gracefully.");
						}
					} catch (InterruptedException e) {
						log.error("Error Terminating Executor Service",e);
					}
			    }
			});
		}
		return asyncOps;
	}
	
	public void destroy(){
		if(exServ!=null){
			if(!exServ.isTerminated()){
				exServ.shutdownNow();
			}
		}
	}
	
	public void submitJob(Runnable worker){
		if(!exServ.isShutdown())
			exServ.submit(worker);
	}
	
	public boolean isShoutDown(){
		return exServ.isShutdown();
	}
	
	public boolean isTerminated(){
		return exServ.isTerminated();
	}
	
	public void shutdown(){
		if(null != exServ && !exServ.isShutdown())
		exServ.shutdown();
	}
}

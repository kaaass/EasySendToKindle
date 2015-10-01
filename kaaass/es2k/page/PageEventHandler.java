package kaaass.es2k.page;

public abstract class PageEventHandler {
	
	/**
	 * We'll run this method when you click "next" button.
	 */
	public abstract void onNext();
	
	/**
	 * We'll run this method when you click "last" button.
	 */
	public abstract void onLast();
}
